package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ConfigState
import com.example.ui.MainViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.theme.GitHubCyan
import com.example.ui.theme.GitHubGreenBright
import com.example.ui.theme.GitHubOrange
import com.example.ui.theme.GitHubPurple

@Composable
fun EngineConfigScreen(viewModel: MainViewModel) {
    val config by viewModel.configState.collectAsState()
    val apiTestState by viewModel.apiTestState.collectAsState()
    val mlKitModelDownloaded by viewModel.mlKitModelDownloaded.collectAsState()
    val isDownloadingMlKit by viewModel.isDownloadingMlKit.collectAsState()
    val mlKitStatusMessage by viewModel.mlKitStatusMessage.collectAsState()

    var showKey by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "翻译引擎选择",
            subtitle = "选择用于浏览 GitHub 仓库与内容时的翻译服务提供商"
        )

        val engines = listOf(
            Triple(
                ConfigState.ENGINE_GOOGLE,
                "Google 翻译 (推荐 / 国内免Key直连)",
                "自动多通道直连，无需任何密钥或注册，响应迅速，翻译质量高。"
            ),
            Triple(
                ConfigState.ENGINE_MLKIT,
                "Google ML Kit (端侧 AI 离线模型)",
                "约 30MB 轻量 Transformer 端侧离线神经网络。下载后 100% 离线运行，零流量、零延迟、免 Key。"
            ),
            Triple(
                ConfigState.ENGINE_BAIDU,
                "百度翻译开放平台",
                "国内直连稳定性好，提供每月数十万字免费额度，需要配置 AppID 和密钥。"
            ),
            Triple(
                ConfigState.ENGINE_DEEPL,
                "DeepL 翻译",
                "业界高水准翻译引擎，支持 DeepL Free 或 Pro API Key。"
            ),
            Triple(
                ConfigState.ENGINE_OFFLINE,
                "仅使用本地内置词典",
                "完全断网离线工作，零流量消耗，仅翻译 GitHub 标准界面词条与按钮。"
            )
        )

        engines.forEach { (engineKey, title, desc) ->
            val isSelected = config.engine == engineKey
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        viewModel.updateConfig { c -> c.copy(engine = engineKey) }
                    }
                    .testTag("engine_card_$engineKey"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // ML Kit Offline Model Management Card
        if (config.engine == ConfigState.ENGINE_MLKIT) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mlkit_management_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Google ML Kit 端侧 AI 语言模型",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "采用轻量 Transformer 端侧神经机器翻译架构。下载到本地后，翻译过程 100% 在手机本地推理，不消耗任何数据流量，无延迟、断网离线可用。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (mlKitModelDownloaded) GitHubGreenBright.copy(alpha = 0.15f)
                                else GitHubOrange.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (mlKitModelDownloaded) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (mlKitModelDownloaded) GitHubGreenBright else GitHubOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (mlKitModelDownloaded) "端侧 AI 语言模型已就绪 (离线运行中)" else "尚未下载离线语言包 (请点击下方下载)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = if (mlKitModelDownloaded) GitHubGreenBright else GitHubOrange
                        )
                    }

                    if (mlKitStatusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mlKitStatusMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.downloadMlKitModel() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_download_mlkit"),
                            enabled = !isDownloadingMlKit,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isDownloadingMlKit) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("下载中...")
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (mlKitModelDownloaded) "重新下载模型" else "下载离线模型 (约30MB)")
                            }
                        }

                        if (mlKitModelDownloaded) {
                            OutlinedButton(
                                onClick = { viewModel.deleteMlKitModel() },
                                modifier = Modifier.testTag("btn_delete_mlkit")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("删除", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Target Language Selection
        SectionHeader(
            title = "目标语言",
            subtitle = "翻译输出的中文方言选项"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val isZhCN = config.targetLang == ConfigState.LANG_ZH_CN
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.updateConfig { it.copy(targetLang = ConfigState.LANG_ZH_CN) } }
                    .testTag("lang_zh_cn"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isZhCN) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (isZhCN) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "简体中文 (zh-CN)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isZhCN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "中国大陆开发者通用", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            val isZhTW = config.targetLang == ConfigState.LANG_ZH_TW
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.updateConfig { it.copy(targetLang = ConfigState.LANG_ZH_TW) } }
                    .testTag("lang_zh_tw"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isZhTW) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (isZhTW) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "繁体中文 (zh-TW)",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isZhTW) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "港澳台开发者通用", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // External API Keys Section if needed
        if (config.engine == ConfigState.ENGINE_BAIDU || config.engine == ConfigState.ENGINE_DEEPL) {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "API 凭证配置",
                subtitle = if (config.engine == ConfigState.ENGINE_BAIDU) "填写百度翻译开放平台的 AppID 与 密钥"
                else "填写 DeepL API Key"
            )

            if (config.engine == ConfigState.ENGINE_BAIDU) {
                OutlinedTextField(
                    value = config.apiKey,
                    onValueChange = { viewModel.updateConfig { c -> c.copy(apiKey = it) } },
                    label = { Text("百度 App ID") },
                    placeholder = { Text("例如: 20230101000000000") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_baidu_appid"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = config.apiSecret,
                    onValueChange = { viewModel.updateConfig { c -> c.copy(apiSecret = it) } },
                    label = { Text("百度 密钥 (Secret Key)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_baidu_secret"),
                    singleLine = true,
                    visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showKey = !showKey }) {
                            Icon(
                                imageVector = if (showKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )
            } else if (config.engine == ConfigState.ENGINE_DEEPL) {
                OutlinedTextField(
                    value = config.apiKey,
                    onValueChange = { viewModel.updateConfig { c -> c.copy(apiKey = it) } },
                    label = { Text("DeepL API Key") },
                    placeholder = { Text("例如: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx:fx") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_deepl_key"),
                    singleLine = true,
                    visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showKey = !showKey }) {
                            Icon(
                                imageVector = if (showKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Test Connection Button & Result
        Button(
            onClick = { viewModel.testApiConnection() },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_test_connection"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = !apiTestState.isTesting
        ) {
            if (apiTestState.isTesting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("正在测试接口连通性...")
            } else {
                Icon(imageVector = Icons.Default.NetworkCheck, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("测试当前引擎连通性")
            }
        }

        if (apiTestState.message != null) {
            Spacer(modifier = Modifier.height(10.dp))
            val isSuccess = apiTestState.success == true
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccess) GitHubGreenBright.copy(alpha = 0.15f)
                    else Color(0xFFF85149).copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (isSuccess) GitHubGreenBright else Color(0xFFF85149),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = apiTestState.message.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSuccess) GitHubGreenBright else Color(0xFFF85149)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
