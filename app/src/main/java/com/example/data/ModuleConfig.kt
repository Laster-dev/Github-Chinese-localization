package com.example.data

import android.content.Context
import android.content.SharedPreferences

data class ConfigState(
    val enabled: Boolean = true,
    val translateRepoDesc: Boolean = true,
    val translateReadme: Boolean = true,
    val translateIssues: Boolean = true,
    val translateCommits: Boolean = true,
    val translateUI: Boolean = true,
    val bilingualMode: Boolean = false,
    val engine: String = ENGINE_GOOGLE,
    val targetLang: String = LANG_ZH_CN,
    val apiKey: String = "",
    val apiSecret: String = "",
    val enableCache: Boolean = true
) {
    companion object {
        const val ENGINE_GOOGLE = "google"
        const val ENGINE_MLKIT = "mlkit"
        const val ENGINE_BAIDU = "baidu"
        const val ENGINE_DEEPL = "deepl"
        const val ENGINE_OFFLINE = "offline"

        const val LANG_ZH_CN = "zh-CN"
        const val LANG_ZH_TW = "zh-TW"
    }
}

class ModuleConfigRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "github_trans_prefs"

        const val KEY_ENABLED = "enabled"
        const val KEY_TRANSLATE_REPO_DESC = "translate_repo_desc"
        const val KEY_TRANSLATE_README = "translate_readme"
        const val KEY_TRANSLATE_ISSUES = "translate_issues"
        const val KEY_TRANSLATE_COMMITS = "translate_commits"
        const val KEY_TRANSLATE_UI = "translate_ui"
        const val KEY_BILINGUAL_MODE = "bilingual_mode"
        const val KEY_ENGINE = "engine"
        const val KEY_TARGET_LANG = "target_lang"
        const val KEY_API_KEY = "api_key"
        const val KEY_API_SECRET = "api_secret"
        const val KEY_ENABLE_CACHE = "enable_cache"
    }

    fun loadConfig(): ConfigState {
        return ConfigState(
            enabled = prefs.getBoolean(KEY_ENABLED, true),
            translateRepoDesc = prefs.getBoolean(KEY_TRANSLATE_REPO_DESC, true),
            translateReadme = prefs.getBoolean(KEY_TRANSLATE_README, true),
            translateIssues = prefs.getBoolean(KEY_TRANSLATE_ISSUES, true),
            translateCommits = prefs.getBoolean(KEY_TRANSLATE_COMMITS, true),
            translateUI = prefs.getBoolean(KEY_TRANSLATE_UI, true),
            bilingualMode = prefs.getBoolean(KEY_BILINGUAL_MODE, false),
            engine = prefs.getString(KEY_ENGINE, ConfigState.ENGINE_GOOGLE) ?: ConfigState.ENGINE_GOOGLE,
            targetLang = prefs.getString(KEY_TARGET_LANG, ConfigState.LANG_ZH_CN) ?: ConfigState.LANG_ZH_CN,
            apiKey = prefs.getString(KEY_API_KEY, "") ?: "",
            apiSecret = prefs.getString(KEY_API_SECRET, "") ?: "",
            enableCache = prefs.getBoolean(KEY_ENABLE_CACHE, true)
        )
    }

    fun saveConfig(config: ConfigState) {
        prefs.edit().apply {
            putBoolean(KEY_ENABLED, config.enabled)
            putBoolean(KEY_TRANSLATE_REPO_DESC, config.translateRepoDesc)
            putBoolean(KEY_TRANSLATE_README, config.translateReadme)
            putBoolean(KEY_TRANSLATE_ISSUES, config.translateIssues)
            putBoolean(KEY_TRANSLATE_COMMITS, config.translateCommits)
            putBoolean(KEY_TRANSLATE_UI, config.translateUI)
            putBoolean(KEY_BILINGUAL_MODE, config.bilingualMode)
            putString(KEY_ENGINE, config.engine)
            putString(KEY_TARGET_LANG, config.targetLang)
            putString(KEY_API_KEY, config.apiKey)
            putString(KEY_API_SECRET, config.apiSecret)
            putBoolean(KEY_ENABLE_CACHE, config.enableCache)
            apply()
        }
    }
}
