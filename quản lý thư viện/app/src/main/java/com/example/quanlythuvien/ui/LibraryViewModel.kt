package com.example.quanlythuvien.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlythuvien.data.LibraryStore
import com.example.quanlythuvien.domain.LibraryManager
import com.example.quanlythuvien.domain.LibrarySnapshot
import com.example.quanlythuvien.domain.OperationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val store: LibraryStore? = LibraryStore(application)
    var state: LibrarySnapshot? by mutableStateOf(null)
        private set
    var message: String? by mutableStateOf(null)
        private set
    var busy: Boolean? by mutableStateOf(true)
        private set
    var loadFailed: Boolean? by mutableStateOf(false)
        private set

    init { reload() }

    fun reload() {
        busy = true
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) { runCatching { store?.load() } }
            state = loaded.getOrNull()
            loadFailed = loaded.isFailure || state == null
            if (loadFailed == true) message = "Không đọc được dữ liệu. Hãy thử lại; dữ liệu cũ vẫn được giữ."
            busy = false
        }
    }

    fun clearMessage() { message = null }

    fun addBook(title: String?, author: String?, type: String?, onSuccess: () -> Unit) =
        update(onSuccess) { addBook(title, author, type) }

    fun addStudent(name: String?, code: String?, onSuccess: () -> Unit) =
        update(onSuccess) { addStudent(name, code) }

    fun borrow(studentId: String?, bookIds: List<String>?, onSuccess: () -> Unit) =
        update(onSuccess) { borrow(studentId, bookIds) }

    fun returnBook(studentId: String?, bookId: String?) = update { returnBook(studentId, bookId) }

    private fun update(onSuccess: () -> Unit = {}, operation: LibraryManager.() -> OperationResult) {
        if (busy == true || state == null || loadFailed == true) return
        val candidate = LibraryManager(state)
        val result = candidate.operation()
        if (result.success != true) {
            message = result.message
            return
        }
        busy = true
        viewModelScope.launch {
            val snapshot = candidate.snapshot()
            val saved = withContext(Dispatchers.IO) { runCatching { store?.save(snapshot) == true }.getOrDefault(false) }
            if (saved) {
                state = snapshot
                message = result.message
                onSuccess()
            } else {
                message = "Không lưu được dữ liệu. Vui lòng thử lại."
            }
            busy = false
        }
    }
}
