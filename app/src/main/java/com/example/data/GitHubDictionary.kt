package com.example.data

data class DictionaryItem(
    val category: String,
    val en: String,
    val zh: String
)

object GitHubDictionary {

    // Comprehensive dictionary for GitHub Android app UI
    val EXACT_MAP = linkedMapOf(
        // === Bottom Navigation & Top Level ===
        "Home" to "首页",
        "Inbox" to "收件箱",
        "Explore" to "探索",
        "Copilot" to "Copilot 智能助手",
        "GitHub Copilot" to "GitHub 智能助手",
        "Chat" to "AI 问答",
        "Notifications" to "通知",
        "Profile" to "个人主页",
        "Search" to "搜索",
        "Search GitHub" to "搜索 GitHub",
        "My Work" to "我的工作台",
        "Favorites" to "收藏夹",
        "Shortcuts" to "快捷方式",
        "Recent" to "最近浏览",
        "Activity" to "动态",
        "All activity" to "全部动态",

        // === Repository Tabs & Navigation ===
        "Overview" to "概览",
        "Repositories" to "代码仓库",
        "Repository" to "仓库",
        "Top Repositories" to "常用仓库",
        "Projects" to "项目看板",
        "Packages" to "软件包",
        "Stars" to "标星 (Stars)",
        "Starred" to "已加星",
        "Sponsoring" to "正在赞助",
        "Sponsors" to "赞助者",
        "Code" to "代码",
        "Issues" to "议题 (Issues)",
        "Pull requests" to "合并请求 (PR)",
        "Pull Requests" to "合并请求 (PR)",
        "Pull Request" to "合并请求",
        "Discussions" to "讨论区",
        "Actions" to "工作流 (Actions)",
        "Security" to "安全",
        "Insights" to "洞察与统计",
        "Settings" to "设置",
        "Wiki" to "维基百科",
        "Releases" to "发布版本",
        "Release" to "发布版本",
        "Branches" to "分支",
        "Branch" to "分支",
        "Tags" to "标签",
        "Tag" to "标签",
        "Contributors" to "贡献者",
        "Commits" to "提交历史",
        "Commit" to "提交",
        "Deployments" to "部署",
        "Environments" to "环境",
        "Workflows" to "工作流程",
        "Runs" to "运行记录",

        // === Repository Details & Headers ===
        "About" to "关于项目",
        "Readme" to "自述文件 (README)",
        "README" to "自述文件 (README)",
        "README.md" to "自述文件 (README.md)",
        "License" to "开源许可证",
        "No license" to "无开源协议",
        "MIT license" to "MIT 开源协议",
        "Apache-2.0 license" to "Apache 2.0 协议",
        "GPL-3.0 license" to "GPL 3.0 协议",
        "Latest commit" to "最新提交",
        "History" to "历史记录",
        "Branches & tags" to "分支与标签",
        "Default branch" to "默认分支",
        "Topics" to "主题标签",
        "Languages" to "编程语言",
        "Used by" to "依赖于此项目",
        "Public" to "公开",
        "Private" to "私有",
        "Internal" to "内部",
        "Archived" to "已归档",
        "Template" to "模板仓库",
        "Forked" to "已派生",
        "Directory" to "目录",
        "Files" to "文件列表",
        "File" to "文件",
        "Raw" to "原始内容",
        "Blame" to "追溯 (Blame)",
        "Tree" to "目录树",

        // === Actions, Menus & Buttons ===
        "Follow" to "关注",
        "Following" to "已关注",
        "Followers" to "关注者",
        "Star" to "标星",
        "Starred" to "已标星",
        "Unstar" to "取消标星",
        "Fork" to "派生 (Fork)",
        "Forks" to "派生 (Forks)",
        "Watch" to "关注动态",
        "Watching" to "正在关注",
        "Unwatch" to "取消关注",
        "Pin" to "置顶",
        "Pinned" to "已置顶",
        "Unpin" to "取消置顶",
        "Edit" to "编辑",
        "Delete" to "删除",
        "Share" to "分享",
        "Clone" to "克隆",
        "Download" to "下载",
        "Download ZIP" to "下载 ZIP 压缩包",
        "Open in browser" to "在浏览器中打开",
        "Open in GitHub" to "在 GitHub 打开",
        "Copy link" to "复制链接",
        "Copy URL" to "复制网址",
        "Copy SHA" to "复制 SHA 码",
        "Filter" to "筛选",
        "Filters" to "筛选条件",
        "Sort" to "排序",
        "Sort by" to "排序方式",
        "Clear filter" to "清除筛选",
        "Clear all" to "清空全部",
        "Apply" to "应用",
        "Cancel" to "取消",
        "Save" to "保存",
        "Done" to "完成",
        "Refresh" to "刷新",
        "View all" to "查看全部",
        "See all" to "查看全部",
        "Show more" to "展开更多",
        "Show less" to "收起",
        "Load more" to "加载更多",
        "Subscribe" to "订阅通知",
        "Unsubscribe" to "取消订阅",
        "Create" to "创建",
        "Add" to "添加",
        "Remove" to "移除",
        "Close" to "关闭",
        "Sign in" to "登录",
        "Sign out" to "退出登录",

        // === Issue & PR Statuses & Details ===
        "Open" to "开启中 (Open)",
        "Closed" to "已关闭 (Closed)",
        "Merged" to "已合并 (Merged)",
        "Draft" to "草稿 (Draft)",
        "New issue" to "新建议题",
        "New Issue" to "新建议题",
        "New pull request" to "新建合并请求",
        "New Pull Request" to "新建合并请求",
        "Author" to "作者",
        "Assignees" to "经办人",
        "Assignee" to "经办人",
        "Reviewers" to "审查人",
        "Reviewer" to "审查人",
        "Labels" to "标签",
        "Label" to "标签",
        "Milestone" to "里程碑",
        "Milestones" to "里程碑",
        "No milestone" to "无里程碑",
        "No labels" to "无标签",
        "No assignee" to "无经办人",
        "No reviewers" to "无审查人",
        "Conversation" to "对话",
        "Commits" to "提交记录",
        "Checks" to "自动化检查",
        "Files changed" to "变更文件",
        "Comment" to "发表评论",
        "Comments" to "评论",
        "Write" to "编写",
        "Preview" to "预览",
        "Close issue" to "关闭议题",
        "Reopen issue" to "重新开启议题",
        "Close pull request" to "关闭合并请求",
        "Reopen pull request" to "重新开启合并请求",
        "Merge pull request" to "合并该请求",
        "Confirm merge" to "确认合并",
        "Squash and merge" to "压缩并合并",
        "Rebase and merge" to "变基并合并",
        "Approve" to "批准 (Approve)",
        "Request changes" to "请求修改",
        "Review changes" to "审查变更",

        // === Explore & Feed ===
        "Trending" to "热度飙升 (Trending)",
        "Trending repositories" to "热门趋势仓库",
        "Trending developers" to "热门开发者",
        "Featured" to "精选推荐",
        "Popular" to "热门推荐",
        "Today" to "今日",
        "This week" to "本周",
        "This month" to "本月",
        "All languages" to "所有编程语言",
        "Spoken language" to "交流自然语言",
        "Discover" to "发现",
        "Lists" to "收藏列表",
        "Topics you might like" to "你可能感兴趣的主题",
        "Repositories you might like" to "你可能喜欢的仓库",
        "Recommended based on your activity" to "根据您的浏览偏好推荐",

        // === Notifications & Settings ===
        "Inbox" to "收件箱",
        "Unread" to "未读",
        "Participating" to "已参与",
        "Saved" to "已保存",
        "All notifications" to "全部通知",
        "Mark as read" to "标记为已读",
        "Mark all as read" to "全部标记为已读",
        "Theme" to "主题风格",
        "Appearance" to "外观设置",
        "System default" to "跟随系统",
        "Dark mode" to "深色模式",
        "Light mode" to "浅色模式",
        "Accounts" to "账号管理",
        "Sign Out" to "退出登录",
        "Feedback" to "意见反馈",
        "Help" to "帮助与支持",
        "Privacy Policy" to "隐私政策",
        "Terms of Service" to "服务条款",
        "Version" to "版本号",

        // === Common README & Markdown Headings ===
        "Features" to "功能特性 (Features)",
        "Feature" to "功能特性",
        "Installation" to "安装指南 (Installation)",
        "Install" to "安装说明",
        "Usage" to "使用说明 (Usage)",
        "Getting Started" to "快速上手 (Getting Started)",
        "Quick Start" to "快速开始",
        "Requirements" to "运行要求 (Requirements)",
        "Prerequisites" to "前置准备",
        "Architecture" to "系统架构",
        "Dependencies" to "依赖项",
        "Configuration" to "配置说明",
        "Screenshots" to "界面预览 (Screenshots)",
        "Documentation" to "参考文档",
        "Documentation & Guides" to "文档与使用指南",
        "Roadmap" to "路线规划 (Roadmap)",
        "Contributing" to "贡献指南 (Contributing)",
        "Contributors" to "贡献者列表",
        "License" to "开源许可证 (License)",
        "Changelog" to "更新日志 (Changelog)",
        "Release Notes" to "发布说明",
        "Troubleshooting" to "常见问题排查",
        "FAQ" to "常见问题解答",
        "Support" to "支持与赞助",
        "Credits" to "鸣谢",
        "Acknowledgments" to "致谢",
        "Disclaimer" to "免责声明",
        "Author" to "作者",
        "Authors" to "作者团队",
        "Security" to "安全策略",
        "Examples" to "使用示例",
        "Example" to "示例",
        "Build" to "构建指南",
        "How to use" to "如何使用",
        "How it works" to "工作原理",
        "Table of contents" to "目录索引",
        "Table of Contents" to "目录索引",

        // === Common Status & Descriptions ===
        "No description provided." to "未提供仓库描述。",
        "No description, website, or topics provided." to "未提供仓库描述、网址或主题标签。",
        "There aren't any open issues." to "当前没有开启中的议题。",
        "There aren't any closed issues." to "当前没有已关闭的议题。",
        "There aren't any pull requests." to "当前没有合并请求。",
        "There aren't any releases." to "暂无已发布的版本。",
        "No commit history found." to "未找到提交历史记录。",
        "No branches found." to "未找到分支。",
        "No tags found." to "未找到标签。",
        "No contributors found." to "未找到贡献者列表。",
        "Failed to load content." to "内容加载失败，请下拉重试。",
        "Retry" to "重试",
        "Something went wrong." to "出现未知错误。",
        "Loading..." to "正在加载...",
        "Pull to refresh" to "下拉刷新"
    )

    // Regex patterns for dynamic sentences and badges
    private val REGEX_REPLACEMENTS = listOf(
        Regex("""^Forked from (.+)$""", RegexOption.IGNORE_CASE) to "派生自 $1",
        Regex("""^Updated (just now|\d+\s+[a-zA-Z]+\s+ago)$""", RegexOption.IGNORE_CASE) to "更新于 $1",
        Regex("""^Created (\d+\s+[a-zA-Z]+\s+ago)$""", RegexOption.IGNORE_CASE) to "创建于 $1",
        Regex("""^Released (\d+\s+[a-zA-Z]+\s+ago)$""", RegexOption.IGNORE_CASE) to "发布于 $1",
        Regex("""^Merged (\d+\s+[a-zA-Z]+\s+ago)$""", RegexOption.IGNORE_CASE) to "合并于 $1",
        Regex("""^Closed (\d+\s+[a-zA-Z]+\s+ago)$""", RegexOption.IGNORE_CASE) to "关闭于 $1",
        Regex("""^(\d+)\s+stars?$""", RegexOption.IGNORE_CASE) to "$1 标星",
        Regex("""^(\d+)\s+forks?$""", RegexOption.IGNORE_CASE) to "$1 派生",
        Regex("""^(\d+)\s+watching$""", RegexOption.IGNORE_CASE) to "$1 人正在关注",
        Regex("""^(\d+)\s+commits?$""", RegexOption.IGNORE_CASE) to "$1 次提交",
        Regex("""^(\d+)\s+branches?$""", RegexOption.IGNORE_CASE) to "$1 个分支",
        Regex("""^(\d+)\s+releases?$""", RegexOption.IGNORE_CASE) to "$1 个发行版",
        Regex("""^(\d+)\s+contributors?$""", RegexOption.IGNORE_CASE) to "$1 位贡献者",
        Regex("""^(\d+)\s+issues?$""", RegexOption.IGNORE_CASE) to "$1 个议题",
        Regex("""^(\d+)\s+pull requests?$""", RegexOption.IGNORE_CASE) to "$1 个合并请求",
        Regex("""^(\d+)\s+discussions?$""", RegexOption.IGNORE_CASE) to "$1 条讨论",
        Regex("""^(\d+)\s+files? changed$""", RegexOption.IGNORE_CASE) to "$1 个文件变更",
        Regex("""^(\d+)\s+comments?$""", RegexOption.IGNORE_CASE) to "$1 条评论"
    )

    /**
     * Try to translate locally via dictionary first (0ms instantaneous).
     */
    fun translateLocal(text: String): String? {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return null

        // Exact match
        EXACT_MAP[trimmed]?.let { return it }

        // Case-insensitive match
        for ((key, value) in EXACT_MAP) {
            if (key.equals(trimmed, ignoreCase = true)) {
                return value
            }
        }

        // Regex patterns
        for ((regex, replacement) in REGEX_REPLACEMENTS) {
            if (regex.matches(trimmed)) {
                return regex.replace(trimmed, replacement)
            }
        }

        return null
    }

    /**
     * Categorized dictionary list for UI display
     */
    val CATEGORIZED_LIST: List<DictionaryItem> by lazy {
        val list = mutableListOf<DictionaryItem>()
        val navTerms = setOf(
            "Home", "Explore", "Notifications", "Profile", "Overview", "Repositories", "Repository",
            "Top Repositories", "Projects", "Packages", "Stars", "Starred", "Code", "Issues",
            "Pull requests", "Pull Requests", "Discussions", "Actions", "Security", "Insights",
            "Settings", "Wiki", "Releases", "Branches", "Tags", "Contributors", "Trending", "Activity"
        )
        val actionTerms = setOf(
            "Follow", "Following", "Followers", "Star", "Unstar", "Fork", "Forks", "Watch",
            "Watching", "Unwatch", "Pin", "Pinned", "Edit", "Delete", "Share", "Clone", "Download",
            "Download ZIP", "Open in browser", "Open in GitHub", "Copy link", "Copy URL", "Search",
            "Filter", "Sort", "Sort by", "Clear filter", "Apply", "Cancel", "Save", "Done", "Subscribe"
        )
        val issueTerms = setOf(
            "Open", "Closed", "Merged", "Draft", "New issue", "New pull request", "Author",
            "Assignees", "Reviewers", "Labels", "Milestone", "Conversation", "Checks",
            "Files changed", "Comment", "Close issue", "Reopen issue", "Merge pull request",
            "Approve", "Request changes"
        )

        EXACT_MAP.forEach { (en, zh) ->
            val category = when {
                navTerms.contains(en) -> "导航与标签"
                actionTerms.contains(en) -> "常用操作与按钮"
                issueTerms.contains(en) -> "议题与合并请求"
                else -> "仓库详情与状态"
            }
            list.add(DictionaryItem(category, en, zh))
        }
        list.sortedBy { it.category }
    }
}
