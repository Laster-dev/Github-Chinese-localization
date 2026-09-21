package com.example.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle

class TranslationConfigProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val ctx = context ?: return null
        val repo = ModuleConfigRepository(ctx)
        val config = repo.loadConfig()

        val response = Bundle()
        when (method) {
            "getConfig" -> {
                response.putBoolean(ModuleConfigRepository.KEY_ENABLED, config.enabled)
                response.putBoolean(ModuleConfigRepository.KEY_TRANSLATE_REPO_DESC, config.translateRepoDesc)
                response.putBoolean(ModuleConfigRepository.KEY_TRANSLATE_README, config.translateReadme)
                response.putBoolean(ModuleConfigRepository.KEY_TRANSLATE_ISSUES, config.translateIssues)
                response.putBoolean(ModuleConfigRepository.KEY_TRANSLATE_COMMITS, config.translateCommits)
                response.putBoolean(ModuleConfigRepository.KEY_TRANSLATE_UI, config.translateUI)
                response.putBoolean(ModuleConfigRepository.KEY_BILINGUAL_MODE, config.bilingualMode)
                response.putString(ModuleConfigRepository.KEY_ENGINE, config.engine)
                response.putString(ModuleConfigRepository.KEY_TARGET_LANG, config.targetLang)
                response.putString(ModuleConfigRepository.KEY_API_KEY, config.apiKey)
                response.putString(ModuleConfigRepository.KEY_API_SECRET, config.apiSecret)
                response.putBoolean(ModuleConfigRepository.KEY_ENABLE_CACHE, config.enableCache)
            }
            "ping" -> {
                response.putString("status", "ok")
            }
        }
        return response
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}
