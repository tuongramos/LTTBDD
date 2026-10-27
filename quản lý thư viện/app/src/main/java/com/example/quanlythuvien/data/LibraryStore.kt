package com.example.quanlythuvien.data

import android.content.Context
import android.content.SharedPreferences
import com.example.quanlythuvien.domain.Book
import com.example.quanlythuvien.domain.LibrarySnapshot
import com.example.quanlythuvien.domain.Loan
import com.example.quanlythuvien.domain.ReferenceBook
import com.example.quanlythuvien.domain.Student
import com.example.quanlythuvien.domain.Textbook
import com.example.quanlythuvien.domain.sampleLibrary
import org.json.JSONArray
import org.json.JSONObject

class LibraryStore(context: Context) {
    private val preferences: SharedPreferences? =
        context.applicationContext.getSharedPreferences("library_data", Context.MODE_PRIVATE)

    fun load(): LibrarySnapshot {
        val saved = preferences?.getString("snapshot", null) ?: return sampleLibrary()
        val root = JSONObject(saved)
        val books = root.getJSONArray("books").objects().map { item ->
            val id = item.stringOrNull("id")
            val title = item.stringOrNull("title")
            val author = item.stringOrNull("author")
            val book: Book = if (item.stringOrNull("type") == "reference") ReferenceBook(id, title, author)
            else Textbook(id, title, author)
            book
        }
        val students = root.getJSONArray("students").objects().map {
            Student(it.stringOrNull("id"), it.stringOrNull("name"), it.stringOrNull("studentCode"))
        }
        val loans = root.getJSONArray("loans").objects().map {
            Loan(it.stringOrNull("bookId"), it.stringOrNull("studentId"), it.longOrNull("borrowedAt"), it.longOrNull("dueAt"))
        }
        return LibrarySnapshot(books, students, loans)
    }

    fun save(snapshot: LibrarySnapshot): Boolean {
        val books = JSONArray().apply {
            snapshot.books.orEmpty().forEach {
                put(JSONObject().put("id", it.id).put("title", it.title).put("author", it.author).put("type", it.typeKey()))
            }
        }
        val students = JSONArray().apply {
            snapshot.students.orEmpty().forEach {
                put(JSONObject().put("id", it.id).put("name", it.name).put("studentCode", it.studentCode))
            }
        }
        val loans = JSONArray().apply {
            snapshot.loans.orEmpty().forEach {
                put(JSONObject().put("bookId", it.bookId).put("studentId", it.studentId)
                    .put("borrowedAt", it.borrowedAt).put("dueAt", it.dueAt))
            }
        }
        val root = JSONObject().put("books", books).put("students", students).put("loans", loans)
        return preferences?.edit()?.putString("snapshot", root.toString())?.commit() == true
    }
}

private fun JSONArray.objects(): List<JSONObject> = (0 until length()).map { getJSONObject(it) }
private fun JSONObject.stringOrNull(key: String): String? = if (isNull(key)) null else getString(key)
private fun JSONObject.longOrNull(key: String): Long? = if (isNull(key)) null else getLong(key)
