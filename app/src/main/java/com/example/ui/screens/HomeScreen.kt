package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.components.SwitchSettingRow
import com.example.ui.theme.GitHubCyan
import com.example.ui.theme.GitHubGreen
import com.example.ui.theme.GitHubGreenBright
import com.example.ui.theme.GitHubOrange
import com.example.ui.theme.GitHubPurple
import com.example.util.ModuleStatusHelper

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToGuide: () -> Unit
) {
    val context = LocalContext.current
    val config by viewModel.configState.collectAsState()
    val cacheCount by viewModel.cacheCount.collectAsState()
    val isModuleActive = viewModel.isModuleActive
    val isGitHubInstalled = viewModel.isGitHubInstalled
    val gitHubVersion = viewModel.gitHubVersion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Module Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isModuleActive) Color(0xFF238636).copy(alpha = 0.12f)
                else Color(0xFFD29922).copy(alpha = 0.12f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isModuleActive) GitHubGreenBright.copy(alpha = 0.2f)
                            else GitHubOrange.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isModuleActive) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isModuleActive) GitHubGreenBright else GitHubOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isModuleActive) "LSPosed 模块已激活" else "LSPosed 模块未激活",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isModuleActive) GitHubGreenBright else GitHubOrange
                        )
                        StatusBadge(
                            text = if (isModuleActive) "ACTIVE" else "PENDING",
                            isPositive = isModuleActive
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isModuleActive)
                            "模块已正常注入并在后台提供翻译服务，已准备接管 GitHub 界面。"
                        else
                            "请在 LSPosed 管理器中启用本模块，勾选 GitHub 作用域并重启 GitHub。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!isModuleActive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = onNavigateToGuide,
                                modifier = Modifier.testTag("btn_guide")
                            ) {
                                Text("查看激活指南", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { ModuleStatusHelper.launchLSPosedManager(context) },
                                colors = ButtonDefaults.buttonColors(containerColor = GitHubOrange),
                                modifier = Modifier.testTag("btn_open_lsposed")
                            ) {
                                Text("打开 LSPosed", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Target GitHub App Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GitHubPurple.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = GitHubPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "目标应用: GitHub App",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "包名: com.github.android (${if (isGitHubInstalled) "已安装 v$gitHubVersion" else "未检测到安装"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { ModuleStatusHelper.launchGitHub(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("btn_launch_github")
                ) {
                    Icon(imageVector = Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("启动", fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Core Features Section
        SectionHeader(
            title = "核心功能控制",
            subtitle = "控制 GitHub 客户端各界面的实时汉化与翻译行为"
        )

        SwitchSettingRow(
            title = "启用 GitHub 汉化助手",
            description = "总开关：开启后将自动接管 GitHub 界面并进行汉化处理",
            checked = config.enabled,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(enabled = it) } },
            icon = Icons.Default.PowerSettingsNew,
            testTag = "switch_master"
        )

        SwitchSettingRow(
            title = "基础 UI 静态词条即时汉化",
            description = "对导航栏、仓库按钮、状态标记、统计数据等进行 0ms 极速本地汉化",
            checked = config.translateUI,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(translateUI = it) } },
            icon = Icons.Default.Translate,
            enabled = config.enabled,
            testTag = "switch_translate_ui"
        )

        SwitchSettingRow(
            title = "仓库描述与主题标签实时翻译",
            description = "在浏览仓库列表与详情页时，自动对接翻译 API 翻译英文仓库简介",
            checked = config.translateRepoDesc,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(translateRepoDesc = it) } },
            icon = Icons.Default.Description,
            enabled = config.enabled,
            testTag = "switch_translate_desc"
        )

        SwitchSettingRow(
            title = "README 自述文件翻译",
            description = "自动翻译项目 README 渲染内容中的英文段落与概述",
            checked = config.translateReadme,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(translateReadme = it) } },
            icon = Icons.Default.Language,
            enabled = config.enabled,
            testTag = "switch_translate_readme"
        )

        SwitchSettingRow(
            title = "Issue 与 PR 议题讨论翻译",
            description = "实时翻译 Issues、Pull Requests 标题与长文评论回复",
            checked = config.translateIssues,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(translateIssues = it) } },
            icon = Icons.Default.QuestionAnswer,
            enabled = config.enabled,
            testTag = "switch_translate_issues"
        )

        SwitchSettingRow(
            title = "提交记录 (Commits) 翻译",
            description = "实时翻译 Git Commit Message 提交说明",
            checked = config.translateCommits,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(translateCommits = it) } },
            icon = Icons.Default.Commit,
            enabled = config.enabled,
            testTag = "switch_translate_commits"
        )

        SwitchSettingRow(
            title = "双语对照模式",
            description = "保留英文原文，下方附带中文译文对照显示（适合开发者对照阅读）",
            checked = config.bilingualMode,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(bilingualMode = it) } },
            icon = Icons.Default.Speed,
            enabled = config.enabled,
            testTag = "switch_bilingual"
        )

        SwitchSettingRow(
            title = "本地高速缓存加速",
            description = "自动保存已翻译结果至本地数据库，避免重复请求并提升浏览流畅度",
            checked = config.enableCache,
            onCheckedChange = { viewModel.updateConfig { c -> c.copy(enableCache = it) } },
            icon = Icons.Default.Speed,
            enabled = config.enabled,
            testTag = "switch_cache"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cache Management Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "本地翻译缓存",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "已缓存 $cacheCount 条翻译结果",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.clearCache() },
                    modifier = Modifier.testTag("btn_clear_cache")
                ) {
                    Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("清空缓存", fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Diagnostic & Connectivity Test Card
        val playgroundState by viewModel.playgroundState.collectAsState()
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = GitHubGreenBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "真机翻译通道与延迟诊断",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "点击测试当前手机网络下的翻译通道响应速度与中文译文效果：",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.executePlaygroundTranslation("Features\n• utilizes changed API/module name dynamic resolution hashes\n• reflective loader properly restores section memory protections") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_test_readme"),
                        colors = ButtonDefaults.buttonColors(containerColor = GitHubGreen),
                        enabled = !playgroundState.isTranslating
                    ) {
                        Text(if (playgroundState.isTranslating) "正在测试..." else "测试 README 汉化", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.executePlaygroundTranslation("This is a fork of Cobalt Strike's User-Defined Reflective Loader with lightweight evasions.") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_test_sentence"),
                        enabled = !playgroundState.isTranslating
                    ) {
                        Text("测试长句翻译", fontSize = 12.sp)
                    }
                }

                if (playgroundState.translatedResult != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "通道: ${playgroundState.engineUsed ?: "高速通道"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GitHubGreenBright
                                )
                                Text(
                                    text = "耗时: ${playgroundState.latencyMs}ms",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = playgroundState.translatedResult ?: "",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else if (playgroundState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "测试异常: ${playgroundState.errorMessage}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
