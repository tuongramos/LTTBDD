package com.example.quanlythuvien.domain

// TRỪU TƯỢNG: định nghĩa hợp đồng; không thể tạo trực tiếp một Book.
abstract class Book(
    val id: String? = null,
    val title: String? = null,
    val author: String? = null,
) {
    abstract fun typeName(): String
    abstract fun loanDays(): Int
    abstract fun typeKey(): String

    fun information(): String = "${author ?: "Chưa rõ tác giả"} • ${typeName()} • ${loanDays()} ngày"
}

// KẾ THỪA: hai loại sách dùng chung thông tin và hành vi từ Book.
class Textbook(id: String? = null, title: String? = null, author: String? = null) :
    Book(id, title, author) {
    override fun typeName(): String = "Giáo trình"
    override fun loanDays(): Int = 14
    override fun typeKey(): String = "textbook"
}

class ReferenceBook(id: String? = null, title: String? = null, author: String? = null) :
    Book(id, title, author) {
    override fun typeName(): String = "Tham khảo"
    override fun loanDays(): Int = 7
    override fun typeKey(): String = "reference"
}

data class Student(
    val id: String? = null,
    val name: String? = null,
    val studentCode: String? = null,
)

data class Loan(
    val bookId: String? = null,
    val studentId: String? = null,
    val borrowedAt: Long? = null,
    val dueAt: Long? = null,
)

data class OperationResult(
    val success: Boolean? = null,
    val message: String? = null,
)

data class LibrarySnapshot(
    val books: List<Book>? = null,
    val students: List<Student>? = null,
    val loans: List<Loan>? = null,
)
