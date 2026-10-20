package com.example.uicomponents

import org.junit.Assert.*
import org.junit.Test

class ComponentCatalogTest {
    @Test fun catalogCoversEveryAssignmentComponent() {
        val required = setOf("Text", "Image", "TextField", "PasswordField", "Toggle", "Slider",
            "DatePicker", "Button", "Checkbox", "RadioButton", "Picker", "NavigationView",
            "TabBar", "Menu", "List", "Card", "Divider", "Column", "Row")
        assertTrue(UiComponent.entries.map { it.title }.containsAll(required))
        assertEquals(26, UiComponent.entries.size)
        assertTrue(UiComponent.entries.all { it.group in componentGroups && it.code.isNotBlank() })
    }

    @Test fun searchSupportsVietnameseWithoutAccentsAndWhitespace() {
        assertTrue(filteredComponents("  van ban  ").contains(UiComponent.TEXT))
        assertTrue(filteredComponents("BAT HOAC TAT").contains(UiComponent.TOGGLE))
        assertEquals(listOf(UiComponent.PASSWORD), filteredComponents("PasswordField"))
        assertEquals(UiComponent.entries.toList(), filteredComponents("  "))
        assertTrue(filteredComponents("does-not-exist").isEmpty())
    }
}
