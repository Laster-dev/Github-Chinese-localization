package com.example.service

import android.content.Context
import android.util.LruCache
import com.example.data.ConfigState
import com.example.data.GitHubDictionary
import com.example.data.db.AppDatabase
import com.example.data.db.TranslationCacheEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

sealed class TranslationResult {
    data class Success(
        val translatedText: String,
        val fromCache: Boolean,
        val engineUsed: String,
        val latencyMs: Long
    ) : TranslationResult()

    data class Error(
        val message: String,
        val latencyMs: Long
    ) : TranslationResult()
}

class TranslationService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    // Fast in-memory cache for ultra-smooth UI rendering
    private val memoryCache = LruCache<String, String>(500)

    private val db by lazy { AppDatabase.getInstance(context) }

    suspend fun translate(
        text: String,
        config: ConfigState
    ): TranslationResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val trimmed = text.trim()

        if (trimmed.isEmpty()) {
            return@withContext TranslationResult.Success("", fromCache = true, "local", 0)
        }

        // 1. Check offline dictionary first (instant zero-latency)
        if (config.translateUI) {
            val localMatch = GitHubDictionary.translateLocal(trimmed)
            if (localMatch != null) {
                return@withContext TranslationResult.Success(
                    translatedText = localMatch,
                    fromCache = true,
                    engineUsed = "离线词典 (0ms)",
                    latencyMs = System.currentTimeMillis() - startTime
                )
            }
        }

        // 2. Check memory cache
        val cacheKey = "${config.targetLang}_$trimmed"
        val memCached = memoryCache.get(cacheKey)
        if (memCached != null) {
            return@withContext TranslationResult.Success(
                translatedText = memCached,
                fromCache = true,
                engineUsed = "内存缓存",
                latencyMs = System.currentTimeMillis() - startTime
            )
        }

        // 3. Check Room DB cache
        if (config.enableCache) {
            try {
                val dbCached = db.translationCacheDao().getCached(trimmed, config.targetLang)
                if (dbCached != null) {
                    memoryCache.put(cacheKey, dbCached.translatedText)
                    return@withContext TranslationResult.Success(
                        translatedText = dbCached.translatedText,
                        fromCache = true,
                        engineUsed = "数据库缓存",
                        latencyMs = System.currentTimeMillis() - startTime
                    )
                }
            } catch (_: Exception) {}
        }

        // 4. Request Translation with automatic Multi-Channel Fallback
        var translatedText: String? = null
        var engineName = "国内直连高速通道"
        var errorMsg: String? = null

        // Channel 0: Google ML Kit (端侧 AI 本地离线模型，约 30MB，极速且离线)
        if (config.engine == ConfigState.ENGINE_MLKIT || config.engine == ConfigState.ENGINE_OFFLINE) {
            try {
                val mlKitRes = MlKitTranslatorManager.translate(trimmed)
                if (mlKitRes.isNotBlank()) {
                    translatedText = mlKitRes
                    engineName = "Google ML Kit (端侧离线AI)"
                }
            } catch (e: Exception) {
                errorMsg = "ML Kit 翻译失败 (可能未下载离线语言包): ${e.message}"
            }
        }

        // Channel A: User's custom Baidu Key
        if (translatedText == null && config.engine == ConfigState.ENGINE_BAIDU && config.apiKey.isNotBlank() && config.apiSecret.isNotBlank()) {
            try {
                translatedText = callBaiduTranslate(trimmed, config)
                engineName = "百度翻译开放平台"
            } catch (e: Exception) {
                errorMsg = "百度翻译失败: ${e.message}"
            }
        }

        // Channel B: User's custom DeepL Key
        if (translatedText == null && config.engine == ConfigState.ENGINE_DEEPL && config.apiKey.isNotBlank()) {
            try {
                translatedText = callDeepLTranslate(trimmed, config)
                engineName = "DeepL API"
            } catch (e: Exception) {
                errorMsg = "DeepL 翻译失败: ${e.message}"
            }
        }

        // Channel C: Youdao POST High-Speed Domestic Endpoint (Default & Instant for mainland China)
        if (translatedText == null) {
            try {
                translatedText = callYoudaoTranslate(trimmed)
                engineName = "有道高速通道 (免Key直连)"
            } catch (e: Exception) {
                errorMsg = "有道通道不可用: ${e.message}"
            }
        }

        // Channel D: MyMemory Global API
        if (translatedText == null) {
            try {
                translatedText = callMyMemoryTranslate(trimmed)
                engineName = "MyMemory 全球通道"
            } catch (e: Exception) {
                errorMsg = "MyMemory 通道不可用: ${e.message}"
            }
        }

        // Channel E: Google Translate Endpoints
        if (translatedText == null) {
            try {
                translatedText = callGoogleTranslate(trimmed, config.targetLang)
                engineName = "Google 翻译通道"
            } catch (e: Exception) {
                errorMsg = "Google 翻译超时或被阻断: ${e.message}"
            }
        }

        val latency = System.currentTimeMillis() - startTime

        if (translatedText != null) {
            memoryCache.put(cacheKey, translatedText)

            if (config.enableCache) {
                try {
                    db.translationCacheDao().insertOrUpdate(
                        TranslationCacheEntity(
                            sourceText = trimmed,
                            translatedText = translatedText,
                            engine = engineName,
                            targetLang = config.targetLang
                        )
                    )
                } catch (_: Exception) {}
            }

            TranslationResult.Success(
                translatedText = translatedText,
                fromCache = false,
                engineUsed = engineName,
                latencyMs = latency
            )
        } else {
            TranslationResult.Error(
                message = errorMsg ?: "所有翻译通道均未能返回结果，请检查网络连接",
                latencyMs = latency
            )
        }
    }

    private fun callYoudaoTranslate(text: String): String {
        val formBody = FormBody.Builder()
            .add("q", text)
            .add("from", "Auto")
            .add("to", "Auto")
            .build()

        val request = Request.Builder()
            .url("https://aidemo.youdao.com/trans")
            .post(formBody)
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 14; Mobile; rv:128.0)")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body?.string() ?: throw IllegalStateException("响应体为空")
            val json = JSONObject(body)
            if (json.optString("errorCode", "") != "0") throw IllegalStateException("接口返回错误码 ${json.optString("errorCode")}")
            val transArr = json.optJSONArray("translation") ?: throw IllegalStateException("未返回 translation 数组")

            val sb = StringBuilder()
            for (i in 0 until transArr.length()) {
                sb.append(transArr.optString(i))
                if (i < transArr.length() - 1) sb.append("\n")
            }
            val res = sb.toString().trim()
            if (res.isEmpty()) throw IllegalStateException("翻译结果为空")
            return res
        }
    }

    private fun callMyMemoryTranslate(text: String): String {
        val encodedText = URLEncoder.encode(text, "UTF-8")
        val url = "https://api.mymemory.translated.net/get?q=$encodedText&langpair=en|zh-CN"
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 14)")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body?.string() ?: throw IllegalStateException("响应体为空")
            val json = JSONObject(body)
            val responseData = json.optJSONObject("responseData") ?: throw IllegalStateException("未找到 responseData")
            val translated = responseData.optString("translatedText")
            if (translated.isBlank()) throw IllegalStateException("未解析出译文")
            return translated.trim()
        }
    }

    private fun callGoogleTranslate(text: String, targetLang: String): String {
        val tl = if (targetLang == ConfigState.LANG_ZH_TW) "zh-TW" else "zh-CN"
        val encodedText = URLEncoder.encode(text, "UTF-8")
        val url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=$tl&dt=t&q=$encodedText"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 14)")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body?.string() ?: throw IllegalStateException("响应体为空")
            val rootArray = JSONArray(body)
            val segmentsArray = rootArray.getJSONArray(0)
            val sb = StringBuilder()
            for (i in 0 until segmentsArray.length()) {
                val segment = segmentsArray.getJSONArray(i)
                sb.append(segment.getString(0))
            }
            val result = sb.toString().trim()
            if (result.isEmpty()) throw IllegalStateException("未能解析出译文")
            return result
        }
    }

    private fun callBaiduTranslate(text: String, config: ConfigState): String {
        val appid = config.apiKey.trim()
        val secret = config.apiSecret.trim()
        val salt = System.currentTimeMillis().toString()
        val sign = md5(appid + text + salt + secret)
        val toLang = if (config.targetLang == ConfigState.LANG_ZH_TW) "cht" else "zh"
        val encodedQ = URLEncoder.encode(text, "UTF-8")
        val url = "https://fanyi-api.baidu.com/api/trans/vip/translate?q=$encodedQ&from=auto&to=$toLang&appid=$appid&salt=$salt&sign=$sign"

        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body?.string() ?: throw IllegalStateException("响应体为空")
            val json = JSONObject(body)
            if (json.has("error_code")) throw IllegalStateException("百度 API 错误: ${json.getString("error_msg")}")
            val results = json.getJSONArray("trans_result")
            val sb = StringBuilder()
            for (i in 0 until results.length()) {
                sb.append(results.getJSONObject(i).getString("dst"))
            }
            return sb.toString()
        }
    }

    private fun callDeepLTranslate(text: String, config: ConfigState): String {
        val authKey = config.apiKey.trim()
        val isFreeApi = authKey.endsWith(":fx")
        val host = if (isFreeApi) "api-free.deepl.com" else "api.deepl.com"
        val url = "https://$host/v2/translate"

        val jsonBody = JSONObject().apply {
            put("text", JSONArray().put(text))
            put("target_lang", "ZH")
        }

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "DeepL-Auth-Key $authKey")
            .header("Content-Type", "application/json")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body?.string() ?: throw IllegalStateException("响应体为空")
            val json = JSONObject(body)
            val translations = json.getJSONArray("translations")
            if (translations.length() > 0) {
                return translations.getJSONObject(0).getString("text")
            }
            throw IllegalStateException("DeepL 未返回翻译数据")
        }
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
