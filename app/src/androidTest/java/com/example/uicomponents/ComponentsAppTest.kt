package com.example.uicomponents

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Rule
import org.junit.Test

class ComponentsAppTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun openCatalog() {
        compose.onNodeWithTag("ready").performClick()
        compose.onNodeWithText("UI Components List").assertIsDisplayed()
    }

    private fun openComponent(component: UiComponent) {
        compose.onNodeWithTag("search").performTextReplacement(component.title)
        compose.onNodeWithTag("component_${component.name}").performScrollTo().performClick()
        compose.onNodeWithTag("detail_list").assertIsDisplayed()
    }

    @Test fun everyComponentOpensAndBackPreservesSearch() {
        openCatalog()
        UiComponent.entries.forEach { component ->
            openComponent(component)
            compose.onNodeWithTag("back").performClick()
            compose.onNodeWithTag("search").assertTextContains(component.title)
        }
        compose.onNodeWithTag("back").performClick()
        compose.onNodeWithTag("ready").assertIsDisplayed()
    }

    @Test fun textInputUpdatesImmediately() {
        openCatalog()
        openComponent(UiComponent.TEXT_FIELD)
        compose.onNodeWithTag("demo_input").performTextInput("Xin chào UTH")
        compose.onNodeWithTag("live_text").assertTextEquals("Bạn vừa nhập: Xin chào UTH")
    }

    @Test fun counterAndDestinationSurviveActivityRecreation() {
        openCatalog()
        openComponent(UiComponent.BUTTON)
        compose.onNodeWithTag("counter_button").performClick().performClick()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("demo_result").assertTextEquals("Đã nhấn: 2")
        compose.onNodeWithTag("back").performClick()
        compose.onNodeWithTag("search").assertTextContains("Button")
    }

    @Test fun emptySearchCanBeCleared() {
        openCatalog()
        compose.onNodeWithTag("search").performTextInput("zzzzzz")
        compose.onNodeWithText("Không tìm thấy thành phần").assertIsDisplayed()
        compose.onNodeWithContentDescription("Xóa tìm kiếm").performClick()
        compose.onNodeWithTag("result_count").assertTextContains("26 thành phần", substring = true)
    }

    @Test fun dialogConfirmAndSnackbarUndoWork() {
        openCatalog()
        openComponent(UiComponent.DIALOG)
        compose.onNodeWithText("Mở hộp thoại").performClick()
        compose.onNodeWithText("Xác nhận").performClick()
        compose.onNodeWithTag("demo_result").assertTextEquals("Đã hoàn thành bài học")
        compose.onNodeWithTag("back").performClick()
        openComponent(UiComponent.SNACKBAR)
        compose.onNodeWithText("Đánh dấu hoàn thành").performClick()
        compose.onNodeWithText("Hoàn tác").performClick()
        compose.onNodeWithTag("demo_result").assertTextEquals("Trạng thái: Chưa hoàn thành")
    }

    @Test fun captureMainScreensForVisualReview() {
        capture("01-welcome")
        openCatalog()
        capture("02-catalog")
        compose.onNodeWithTag("component_TEXT").performClick()
        capture("03-text-detail")
        compose.onNodeWithTag("back").performClick()
        openComponent(UiComponent.ROW)
        capture("04-row-detail")
        compose.onNodeWithTag("back").performClick()
        openComponent(UiComponent.IMAGE)
        capture("05-image-detail")
    }

    private fun capture(name: String) {
        compose.waitForIdle()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = File(context.getExternalFilesDir(null), "screenshots").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use {
            compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
