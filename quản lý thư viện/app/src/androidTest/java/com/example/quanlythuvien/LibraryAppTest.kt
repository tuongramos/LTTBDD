package com.example.quanlythuvien

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.quanlythuvien.data.LibraryStore
import com.example.quanlythuvien.domain.LibraryManager
import com.example.quanlythuvien.domain.ReferenceBook
import com.example.quanlythuvien.domain.sampleLibrary
import org.junit.Assert.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class LibraryAppTest {
    @get:Rule
    val compose: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>? =
        createAndroidComposeRule<MainActivity>()

    @After fun resetSampleData() {
        LibraryStore(InstrumentationRegistry.getInstrumentation().targetContext).save(sampleLibrary())
    }

    @Test fun threeTabsBorrowReturnAndRecreate() {
        val rule = requireNotNull(compose)
        rule.waitUntil(15_000) { rule.onAllNodesWithText("Thay đổi").fetchSemanticsNodes().isNotEmpty() }
        screenshot("01-quan-ly")
        rule.onNodeWithText("Sách 01").performClick()
        rule.onNodeWithText("Thêm", useUnmergedTree = true).performScrollTo().performClick()
        rule.waitUntil(10_000) { rule.onAllNodesWithText("Sách đang mượn (1)").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Thay đổi").performScrollTo().performClick()
        rule.onNodeWithText("Tran Thi B (SV002)").performClick()
        rule.onNodeWithText("Đã có người mượn").assertExists()
        rule.onNodeWithText("Thay đổi").performClick()
        rule.onNodeWithText("Nguyen Van A (SV001)").performClick()
        rule.activityRule.scenario.recreate()
        rule.waitUntil(15_000) { rule.onAllNodesWithText("Sách đang mượn (1)").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Trả sách").performScrollTo().performClick()
        rule.waitUntil(10_000) { rule.onAllNodesWithText("Sách đang mượn (1)").fetchSemanticsNodes().isEmpty() }
        rule.onNodeWithText("DS Sách").performClick()
        rule.onNodeWithText("Tên sách").assertExists()
        screenshot("02-ds-sach")
        rule.onNodeWithText("Tên sách").performTextInput("Kotlin thực hành")
        rule.onNodeWithText("Tác giả").performTextInput("Tác giả kiểm thử")
        rule.onNodeWithText("Thêm sách", substring = false).performScrollTo().performClick()
        rule.waitUntil(10_000) { rule.onAllNodesWithText("Danh sách sách (3)").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Sinh viên", substring = false).performClick()
        screenshot("03-sinh-vien")
        rule.onNodeWithText("Họ và tên").performTextInput("Sinh viên kiểm thử")
        rule.onNodeWithText("Mã sinh viên", substring = false).performTextInput("SV004")
        rule.onNodeWithText("Thêm sinh viên", substring = false).performScrollTo().performClick()
        rule.waitUntil(10_000) { rule.onAllNodesWithText("Sinh viên (4)").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Quản lý", substring = false).performClick()
        rule.onNodeWithText("Thay đổi").performScrollTo().performClick()
        rule.onNodeWithText("Sinh viên kiểm thử (SV004)").performClick()
        rule.onNodeWithText("Sinh viên kiểm thử", substring = false).assertExists()
        rule.onNodeWithText("Kotlin thực hành").assertExists()
    }

    @Test fun storageRoundTripPreservesNullableValuesAndPolymorphism() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val store = LibraryStore(context)
        val manager = LibraryManager(sampleLibrary())
        manager.borrow("student-01", listOf("book-02"), 1000L)
        assertTrue(store.save(manager.snapshot()))
        val restored = store.load()
        assertTrue(restored.books.orEmpty()[1] is ReferenceBook)
        assertEquals(1000L + 7 * 86_400_000L, restored.loans.orEmpty().single().dueAt)
        val nullable = restored.copy(books = listOf(ReferenceBook()))
        assertTrue(store.save(nullable))
        assertNull(store.load().books.orEmpty().single().title)
        assertTrue(store.save(sampleLibrary()))
    }

    private fun screenshot(name: String) {
        val rule = requireNotNull(compose)
        rule.waitForIdle()
        val image = rule.onRoot().captureToImage().asAndroidBitmap()
        val directory = File(rule.activity.getExternalFilesDir(null), "screenshots").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
