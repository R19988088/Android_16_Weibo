package com.example.myweibo.ui

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeiboAppBehaviorSourceTest {
    private val source: String by lazy {
        listOf(
            File("src/main/java/com/example/myweibo/ui/WeiboApp.kt"),
            File("app/src/main/java/com/example/myweibo/ui/WeiboApp.kt"),
        ).first(File::exists).readText()
    }

    @Test
    fun composeRootBackReturnsToFeedInsteadOfExitHint() {
        assertTrue(source.contains("onRootBack = { selectedTab = MainTab.Feed }"))
        assertFalse(source.contains("pageUrl = \"https://m.weibo.cn/compose/\",\n                        onRootBack = ::handleRootBackPress"))
    }

    @Test
    fun messagesTabKeepsBottomNavigationVisible() {
        val bottomBarCondition = source.substringBefore("WeiboLiquidBottomBar(")
            .takeLast(500)
        assertFalse(bottomBarCondition.contains("selectedTab != MainTab.Messages"))
        assertTrue(source.contains("bottomContentPadding = if (messageRoot) 96.dp else 0.dp"))
    }

    @Test
    fun singleTallImageAllowsOriginalRatioUntilOneToFive() {
        assertTrue(source.contains("private const val SingleImageMaxHeightToWidth = 5f"))
        assertTrue(source.contains("return naturalAspect.coerceAtLeast(minAspectFromHeightCap).coerceAtMost(3f)"))
    }

    @Test
    fun multiVideoUsesGridAndSingleVideoUsesWidePlayer() {
        assertTrue(source.contains("val mediaGridItems = buildList"))
        assertTrue(source.contains("val showSingleWideVideo = images.isEmpty() && medias.size == 1"))
        assertTrue(source.contains("FeedMediaGridCell("))
    }

    @Test
    fun feedImageTapCancelsWhenMultiplePointersArePresent() {
        assertTrue(source.contains("var multiPointerGesture = false"))
        assertTrue(source.contains("if (event.changes.size > 1) multiPointerGesture = true"))
        assertTrue(source.contains("if (!cancelledByMoveBeforeLongPress && !multiPointerGesture)"))
    }
}
