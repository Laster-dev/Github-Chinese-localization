package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object ModuleStatusHelper {

    const val GITHUB_PACKAGE_NAME = "com.github.android"

    /**
     * This method will return false in normal unhooked mode.
     * When LSPosed activates this module, GitHubHookEntry hooks this method
     * and forces it to return true!
     */
    @JvmStatic
    fun isModuleActive(): Boolean {
        return false
    }

    /**
     * Check if official GitHub App is installed
     */
    fun isGitHubInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo(GITHUB_PACKAGE_NAME, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Get installed GitHub App version
     */
    fun getGitHubVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(GITHUB_PACKAGE_NAME, 0)
            pInfo.versionName ?: "已安装"
        } catch (_: PackageManager.NameNotFoundException) {
            "未安装"
        }
    }

    /**
     * Open GitHub app if installed, or open GitHub play store page
     */
    fun launchGitHub(context: Context): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(GITHUB_PACKAGE_NAME)
        return if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            true
        } else {
            val marketIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$GITHUB_PACKAGE_NAME")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(marketIntent)
                false
            } catch (_: Exception) {
                false
            }
        }
    }

    /**
     * Open LSPosed Manager
     */
    fun launchLSPosedManager(context: Context): Boolean {
        val lsposedPkgs = listOf("org.lsposed.manager", "com.topjohnwu.magisk")
        for (pkg in lsposedPkgs) {
            val intent = context.packageManager.getLaunchIntentForPackage(pkg)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return true
            }
        }
        return false
    }
}
