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
        assertTrue(source.contains("topContentPadding = if (messageRoot) 20.dp else topInset"))
        assertTrue(source.contains("bottomContentPadding = if (messageRoot) 0.dp else 0.dp"))
    }

    @Test
    fun messageAvatarClicksNavigateToProfiles() {
        assertTrue(source.contains("installMessageAvatarProfileNavigation()"))
        assertTrue(source.contains("window.__myweiboMessageAvatarProfileNavigation"))
        assertTrue(source.contains("profileUrlFromMessageAvatar"))
        assertTrue(source.contains("document.addEventListener('click'"))
        assertTrue(source.contains("https://m.weibo.cn/profile/"))
    }

    @Test
    fun paragraphSpacingIsReducedFortyPercent() {
        assertTrue(source.contains("SpanStyle(fontSize = 3.sp)"))
        assertFalse(source.contains("SpanStyle(fontSize = 5.sp)"))
    }

    @Test
    fun webTabsStillProvideBottomGlassBackdrop() {
        assertFalse(source.contains("webTabBackdropExcluded"))
        assertTrue(source.contains("WebTabBottomGlassBackdropSource("))
        assertTrue(source.contains("val webTabVisible = messagesWebVisible || composeWebVisible"))
        assertTrue(source.contains("visible = webTabVisible"))
        assertTrue(source.contains("if (!webTabVisible)"))
        assertTrue(source.contains(".layerBackdrop(bottomBarBackdrop)"))
    }

    @Test
    fun primaryTabsUseHorizontalSwitchMotionWithoutUnmountingPages() {
        assertTrue(source.contains("private fun primaryTabTransitionOffset("))
        assertTrue(source.contains("val primaryTabSwitchOffset by animateDpAsState("))
        assertTrue(source.contains("primaryTabSwitchTarget = primaryTabTransitionOffset(previousPrimaryTab, selectedTab)"))
        assertTrue(source.contains("primaryTabSwitchTarget = 0.dp"))
        assertTrue(source.contains(".offset(x = primaryTabSwitchOffset * feedMotionMultiplier)"))
        assertTrue(source.contains(".offset(x = primaryTabSwitchOffset * mineMotionMultiplier)"))
    }

    @Test
    fun homeTopKeepsProgressiveBlurAndWhiteSearchCapsule() {
        assertTrue(source.contains("HomeTopProgressiveBlur("))
        assertTrue(source.contains("HazeProgressive.verticalGradient("))
        assertTrue(source.contains(".background(Color.White.copy(alpha = 0.94f))"))
        assertTrue(source.contains(".border(0.5.dp, HintCapsuleBorderColor, RoundedCornerShape(percent = 50))"))
    }

    @Test
    fun singleTallImageAllowsOriginalRatioUntilOneToFive() {
        assertTrue(source.contains("private const val SingleImageMaxHeightToWidth = 5f"))
        assertTrue(source.contains("return naturalAspect.coerceAtLeast(minAspectFromHeightCap).coerceAtMost(3f)"))
    }

    @Test
    fun extraTallSingleImageUsesDoubleStandardHeightAndFitContentScale() {
        assertTrue(source.contains("private const val ExtraTallImageRatioThreshold = 0.25f"))
        assertTrue(source.contains("private const val ExtraTallSingleImageAspectRatio = 0.5f"))
        assertTrue(source.contains("isExtraTallSingleImage("))
        assertTrue(source.contains("contentScale = if (extraTallImage) ContentScale.Fit else ContentScale.Crop"))
    }

    @Test
    fun feedTabDoubleTapRefreshesWhenBottomNavigationIsExpanded() {
        assertTrue(source.contains("onFeedDoubleTap = { refreshTimelineFromTop() }"))
    }

    @Test
    fun remoteImagesUseBoundedParallelCandidateReads() {
        assertTrue(source.contains("private val FeedImageLoadSemaphore = Semaphore"))
        assertTrue(source.contains("loadFirstRemoteBitmap("))
        assertTrue(source.contains("async(Dispatchers.IO)"))
    }

    @Test
    fun albumWaterfallDefersBackgroundPrefetchAfterTouchPath() {
        assertTrue(source.contains("private const val AlbumGridPrefetchStartDelayMs = 450L"))
        assertTrue(source.contains("private const val AlbumGridPrefetchConcurrency = 3"))
        assertTrue(source.contains("delay(AlbumGridPrefetchStartDelayMs)"))
        assertTrue(source.contains("snapshotFlow { albumListState.isScrollInProgress }"))
        assertTrue(source.contains("albumRowsByIdentity(albumImages)"))
    }

    @Test
    fun deviceSourceVisibilityUsesMetadataSettingEverywhere() {
        assertTrue(source.contains("private fun visibleDeviceSource("))
        assertTrue(source.contains("visibleDeviceSource(item.source, metadataSettings)"))
        assertFalse(source.contains("listOfNotNull(formatWeiboTime(item.createdAt), item.source)"))
    }

    @Test
    fun appearanceSettingsExposeAccentColorAndThemeUsesIt() {
        assertTrue(source.contains("accentColorArgb: Long?"))
        assertTrue(source.contains("onAccentColorChange: (Long?) -> Unit"))
        assertTrue(source.contains("text = \"自定义强调色\""))
        assertTrue(source.contains("onAccentColorChange(argb)"))
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
