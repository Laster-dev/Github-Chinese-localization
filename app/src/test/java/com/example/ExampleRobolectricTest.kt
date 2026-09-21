package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GitHubDictionary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("GitHub 汉化助手", appName)
    }

    @Test
    fun `dictionary exact match test`() {
        assertEquals("代码仓库", GitHubDictionary.translateLocal("Repositories"))
        assertEquals("概览", GitHubDictionary.translateLocal("Overview"))
        assertEquals("自述文件 (README)", GitHubDictionary.translateLocal("README"))
        assertEquals("标星", GitHubDictionary.translateLocal("Star"))
    }

    @Test
    fun `dictionary regex pattern match test`() {
        val translatedFork = GitHubDictionary.translateLocal("Forked from torvalds/linux")
        assertNotNull(translatedFork)
        assertEquals("派生自 torvalds/linux", translatedFork)

        val translatedStars = GitHubDictionary.translateLocal("128 stars")
        assertNotNull(translatedStars)
        assertEquals("128 标星", translatedStars)
    }
}
