package com.example.hook

import android.content.Context
import android.net.Uri
import com.example.BuildConfig
import com.example.data.ConfigState
import com.example.data.ModuleConfigRepository
import de.robv.android.xposed.XposedBridge

/**
 * Safely loads module configuration in the hooked process via ContentProvider.
 * Caches configuration with a TTL to avoid IPC overhead on every text view update.
 */
object RemoteConfigManager {

    private const val TAG = "GitHubTranslator"
    private val PROVIDER_URI = Uri.parse("content://${BuildConfig.APPLICATION_ID}.provider")

    @Volatile
    private var cachedConfig: ConfigState = ConfigState()
    @Volatile
    private var lastFetchTime = 0L
    private const val CACHE_DURATION_MS = 15_000L // Refresh every 15s

    fun getConfig(context: Context?): ConfigState {
        val now = System.currentTimeMillis()
        if (now - lastFetchTime < CACHE_DURATION_MS) {
            return cachedConfig
        }

        if (context == null) return cachedConfig

        try {
            val bundle = context.contentResolver.call(
                PROVIDER_URI,
                "getConfig",
                null,
                null
            )
            if (bundle != null) {
                cachedConfig = ConfigState(
                    enabled = bundle.getBoolean(ModuleConfigRepository.KEY_ENABLED, true),
                    translateRepoDesc = bundle.getBoolean(ModuleConfigRepository.KEY_TRANSLATE_REPO_DESC, true),
                    translateReadme = bundle.getBoolean(ModuleConfigRepository.KEY_TRANSLATE_README, true),
                    translateIssues = bundle.getBoolean(ModuleConfigRepository.KEY_TRANSLATE_ISSUES, true),
                    translateCommits = bundle.getBoolean(ModuleConfigRepository.KEY_TRANSLATE_COMMITS, true),
                    translateUI = bundle.getBoolean(ModuleConfigRepository.KEY_TRANSLATE_UI, true),
                    bilingualMode = bundle.getBoolean(ModuleConfigRepository.KEY_BILINGUAL_MODE, false),
                    engine = bundle.getString(ModuleConfigRepository.KEY_ENGINE, ConfigState.ENGINE_GOOGLE) ?: ConfigState.ENGINE_GOOGLE,
                    targetLang = bundle.getString(ModuleConfigRepository.KEY_TARGET_LANG, ConfigState.LANG_ZH_CN) ?: ConfigState.LANG_ZH_CN,
                    apiKey = bundle.getString(ModuleConfigRepository.KEY_API_KEY, "") ?: "",
                    apiSecret = bundle.getString(ModuleConfigRepository.KEY_API_SECRET, "") ?: "",
                    enableCache = bundle.getBoolean(ModuleConfigRepository.KEY_ENABLE_CACHE, true)
                )
                lastFetchTime = now
            }
        } catch (t: Throwable) {
            // ContentProvider may not be ready or security restriction; fallback gracefully
            XposedBridge.log("$TAG: Could not query RemoteConfigManager: ${t.message}")
        }

        return cachedConfig
    }
}
