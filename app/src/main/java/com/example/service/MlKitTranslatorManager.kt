package com.example.service

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object MlKitTranslatorManager {

    private var translatorInstance: Translator? = null
    private var isInitialized = false

    private val modelManager by lazy { RemoteModelManager.getInstance() }
    private val chineseModel by lazy {
        TranslateRemoteModel.Builder(TranslateLanguage.CHINESE).build()
    }

    @Synchronized
    fun getTranslator(): Translator {
        val existing = translatorInstance
        if (existing != null) return existing

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.CHINESE)
            .build()
        val newTranslator = Translation.getClient(options)
        translatorInstance = newTranslator
        return newTranslator
    }

    suspend fun isModelDownloaded(): Boolean = withContext(Dispatchers.IO) {
        try {
            val task = modelManager.isModelDownloaded(chineseModel)
            Tasks.await(task, 4, TimeUnit.SECONDS)
        } catch (_: Throwable) {
            false
        }
    }

    suspend fun downloadModel(
        requireWifi: Boolean = false,
        onProgress: (Boolean) -> Unit = {}
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val translator = getTranslator()
            val conditionsBuilder = DownloadConditions.Builder()
            if (requireWifi) {
                conditionsBuilder.requireWifi()
            }
            val conditions = conditionsBuilder.build()
            val task = translator.downloadModelIfNeeded(conditions)
            Tasks.await(task, 90, TimeUnit.SECONDS)
            isInitialized = true
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteModel(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val task = modelManager.deleteDownloadedModel(chineseModel)
            Tasks.await(task, 10, TimeUnit.SECONDS)
            translatorInstance?.close()
            translatorInstance = null
            isInitialized = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun translate(text: String): String = withContext(Dispatchers.IO) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return@withContext ""

        val translator = getTranslator()
        val task = translator.translate(trimmed)
        Tasks.await(task, 8, TimeUnit.SECONDS)
    }

    fun translateBlocking(text: String, timeoutSec: Long = 4): String? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return ""
        return try {
            val translator = getTranslator()
            val task = translator.translate(trimmed)
            Tasks.await(task, timeoutSec, TimeUnit.SECONDS)
        } catch (_: Throwable) {
            null
        }
    }
}
