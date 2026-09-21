package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ConfigState
import com.example.data.ModuleConfigRepository
import com.example.data.db.AppDatabase
import com.example.service.TranslationResult
import com.example.service.TranslationService
import com.example.util.ModuleStatusHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PlaygroundState(
    val inputText: String = "A modern Android client for GitHub with real-time translation and clean localized UI.",
    val isTranslating: Boolean = false,
    val translatedResult: String? = null,
    val engineUsed: String? = null,
    val latencyMs: Long = 0,
    val errorMessage: String? = null,
    val fromCache: Boolean = false
)

data class ApiTestState(
    val isTesting: Boolean = false,
    val success: Boolean? = null,
    val message: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ModuleConfigRepository(application)
    private val translationService = TranslationService(application)
    private val db = AppDatabase.getInstance(application)

    private val _configState = MutableStateFlow(repository.loadConfig())
    val configState: StateFlow<ConfigState> = _configState.asStateFlow()

    val cacheCount: StateFlow<Int> = db.translationCacheDao().getCacheCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val isModuleActive: Boolean
        get() = ModuleStatusHelper.isModuleActive()

    val isGitHubInstalled: Boolean
        get() = ModuleStatusHelper.isGitHubInstalled(getApplication())

    val gitHubVersion: String
        get() = ModuleStatusHelper.getGitHubVersion(getApplication())

    private val _playgroundState = MutableStateFlow(PlaygroundState())
    val playgroundState: StateFlow<PlaygroundState> = _playgroundState.asStateFlow()

    private val _apiTestState = MutableStateFlow(ApiTestState())
    val apiTestState: StateFlow<ApiTestState> = _apiTestState.asStateFlow()

    private val _mlKitModelDownloaded = MutableStateFlow(false)
    val mlKitModelDownloaded: StateFlow<Boolean> = _mlKitModelDownloaded.asStateFlow()

    private val _isDownloadingMlKit = MutableStateFlow(false)
    val isDownloadingMlKit: StateFlow<Boolean> = _isDownloadingMlKit.asStateFlow()

    private val _mlKitStatusMessage = MutableStateFlow<String?>(null)
    val mlKitStatusMessage: StateFlow<String?> = _mlKitStatusMessage.asStateFlow()

    init {
        checkMlKitModelStatus()
    }

    fun checkMlKitModelStatus() {
        viewModelScope.launch {
            val downloaded = com.example.service.MlKitTranslatorManager.isModelDownloaded()
            _mlKitModelDownloaded.value = downloaded
        }
    }

    fun downloadMlKitModel() {
        _isDownloadingMlKit.value = true
        _mlKitStatusMessage.value = "正在下载端侧 AI 语言模型 (约30MB)..."
        viewModelScope.launch {
            val res = com.example.service.MlKitTranslatorManager.downloadModel()
            _isDownloadingMlKit.value = false
            if (res.isSuccess) {
                _mlKitModelDownloaded.value = true
                _mlKitStatusMessage.value = "模型下载完成！已支持 100% 离线端侧神经网络翻译。"
            } else {
                _mlKitStatusMessage.value = "模型下载失败: ${res.exceptionOrNull()?.message}"
            }
        }
    }

    fun deleteMlKitModel() {
        viewModelScope.launch {
            val res = com.example.service.MlKitTranslatorManager.deleteModel()
            if (res.isSuccess) {
                _mlKitModelDownloaded.value = false
                _mlKitStatusMessage.value = "离线模型已清除。"
            } else {
                _mlKitStatusMessage.value = "删除失败: ${res.exceptionOrNull()?.message}"
            }
        }
    }

    fun updateConfig(update: (ConfigState) -> ConfigState) {
        val newConfig = update(_configState.value)
        _configState.value = newConfig
        repository.saveConfig(newConfig)
    }

    fun clearCache() {
        viewModelScope.launch {
            db.translationCacheDao().clearAll()
        }
    }

    fun updatePlaygroundInput(text: String) {
        _playgroundState.value = _playgroundState.value.copy(inputText = text)
    }

    fun executePlaygroundTranslation(customText: String? = null) {
        val textToTranslate = customText ?: _playgroundState.value.inputText
        if (textToTranslate.isBlank()) return

        _playgroundState.value = _playgroundState.value.copy(
            isTranslating = true,
            errorMessage = null,
            translatedResult = null
        )

        viewModelScope.launch {
            val result = translationService.translate(textToTranslate, _configState.value)
            when (result) {
                is TranslationResult.Success -> {
                    _playgroundState.value = _playgroundState.value.copy(
                        isTranslating = false,
                        translatedResult = result.translatedText,
                        engineUsed = result.engineUsed,
                        latencyMs = result.latencyMs,
                        fromCache = result.fromCache,
                        errorMessage = null
                    )
                }
                is TranslationResult.Error -> {
                    _playgroundState.value = _playgroundState.value.copy(
                        isTranslating = false,
                        errorMessage = result.message,
                        latencyMs = result.latencyMs,
                        translatedResult = null
                    )
                }
            }
        }
    }

    fun testApiConnection() {
        _apiTestState.value = ApiTestState(isTesting = true)
        viewModelScope.launch {
            val sampleText = "Hello GitHub"
            val result = translationService.translate(sampleText, _configState.value)
            when (result) {
                is TranslationResult.Success -> {
                    _apiTestState.value = ApiTestState(
                        isTesting = false,
                        success = true,
                        message = "连接成功！引擎「${result.engineUsed}」耗时 ${result.latencyMs}ms，译文: ${result.translatedText}"
                    )
                }
                is TranslationResult.Error -> {
                    _apiTestState.value = ApiTestState(
                        isTesting = false,
                        success = false,
                        message = "连接测试失败: ${result.message}"
                    )
                }
            }
        }
    }
}
