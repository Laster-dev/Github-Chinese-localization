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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionHeader
import com.example.ui.theme.GitHubCyan
import com.example.ui.theme.GitHubGreenBright
import com.example.ui.theme.GitHubPurple
import com.example.util.ModuleStatusHelper

@Composable
fun GuideScreen() {
    val context = LocalContext.current

    val steps = listOf(
        Triple(
            "1",
            "安装并配置 LSPosed 框架",
            "确保您的设备已获得 Root 权限（推荐 Magisk 24+、KernelSU 或 APatch），并在模块中成功刷入 LSPosed (Zygisk 变体)。"
        ),
        Triple(
            "2",
            "在 LSPosed 中勾选本模块",
            "打开「LSPosed 管理器」，进入「模块」列表，找到并点开「GitHub 汉化助手」，将右上角的「启用模块」开关拨到开启。"
        ),
        Triple(
            "3",
            "确认作用域包含 GitHub",
            "在推荐作用域中，必须勾选「GitHub」(包名: com.github.android)。本模块已预置作用域声明，一般会自动勾选。"
        ),
        Triple(
            "4",
            "重启 GitHub 客户端",
            "前往系统设置 -> 应用管理，对 GitHub 客户端执行「强制停止」，然后重新打开 GitHub 客户端。模块即刻自动注入接管！"
        )
    )

    val faqs = listOf(
        "为什么之前「探索 (Explore)」与仓库动态界面会空白或加载不出来？" to "GitHub 的探索流和列表由现代 RecyclerView 与组件池复用构建。此前模块拦截了部分内部标识与搜索输入框，导致数据加载中断。现已加入防空安全白名单机制：严格保护 EditText 输入组件、仓库路径（如 owner/repo）、Git Hash 与代码段，确保探索和列表流畅加载。",
        "为什么浏览仓库时英文没有立刻变成中文？" to "模块对动态文本（如仓库介绍、README）采用异步非阻塞网络翻译，通常在打开页面后 100~300ms 内完成请求并无感刷新，不会造成界面卡顿。",
        "网络请求失败或翻译超时？" to "Google 翻译接口需要正常的国际网络连接。如果您处于国内网络环境，建议在「翻译引擎」设置中切换为「百度翻译开放平台」，提供国内高速直连。",
        "是否支持双语对照阅读？" to "支持！在首页开关中开启「双语对照模式」，模块会在保留英文原文的同时，在下方优雅显示中文翻译，方便核对专业词汇。",
        "如何释放翻译缓存占用的存储空间？" to "在首页底部的「本地翻译缓存」卡片中，点击「清空缓存」即可重置所有已缓存的翻译数据。"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SectionHeader(
            title = "LSPosed 模块激活指南",
            subtitle = "按照以下 4 个步骤即可让 GitHub App 拥有丝滑的汉化与实时翻译体验"
        )

        steps.forEach { (num, title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = num,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action shortcuts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { ModuleStatusHelper.launchLSPosedManager(context) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("guide_btn_lsposed"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("打开 LSPosed", fontSize = 13.sp)
            }

            Button(
                onClick = { ModuleStatusHelper.launchGitHub(context) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("guide_btn_github"),
                colors = ButtonDefaults.buttonColors(containerColor = GitHubPurple)
            ) {
                Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("启动 GitHub", fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(
            title = "常见问题与解答 (FAQ)",
            subtitle = "使用过程中的关键技巧与排查建议"
        )

        faqs.forEach { (q, a) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = q,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = a,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
