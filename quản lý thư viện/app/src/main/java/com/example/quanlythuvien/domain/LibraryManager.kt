package com.example.quanlythuvien.domain

import java.util.UUID

class LibraryManager(snapshot: LibrarySnapshot? = null) {
    // ĐÓNG GÓI: các danh sách nullable, private; chỉ thay đổi qua nghiệp vụ đã kiểm tra.
    private var books: MutableList<Book>? = snapshot?.books?.toMutableList()
    private var students: MutableList<Student>? = snapshot?.students?.toMutableList()
    private var loans: MutableList<Loan>? = snapshot?.loans?.toMutableList()

    fun snapshot(): LibrarySnapshot = LibrarySnapshot(
        books = books.orEmpty().toList(),
        students = students.orEmpty().toList(),
        loans = loans.orEmpty().toList(),
    )

    fun addBook(title: String?, author: String?, type: String?): OperationResult {
        val cleanTitle = title?.trim().orEmpty()
        val cleanAuthor = author?.trim().orEmpty()
        if (cleanTitle.isBlank() || cleanAuthor.isBlank()) return failure("Nhập đủ tên sách và tác giả.")
        if (cleanTitle.length > 120 || cleanAuthor.length > 80) return failure("Tên sách tối đa 120, tác giả tối đa 80 ký tự.")
        if (type != "textbook" && type != "reference") return failure("Vui lòng chọn loại sách.")
        val id = UUID.randomUUID().toString()
        val book: Book = if (type == "reference") ReferenceBook(id, cleanTitle, cleanAuthor)
        else Textbook(id, cleanTitle, cleanAuthor)
        if (books == null) books = mutableListOf()
        books?.add(book)
        return success("Đã thêm $cleanTitle.")
    }

    fun addStudent(name: String?, studentCode: String?): OperationResult {
        val cleanName = name?.trim().orEmpty()
        val cleanCode = studentCode?.trim().orEmpty()
        if (cleanName.isBlank() || cleanCode.isBlank()) return failure("Nhập đủ họ tên và mã sinh viên.")
        if (cleanName.length > 80 || cleanCode.length > 30) return failure("Họ tên tối đa 80, mã sinh viên tối đa 30 ký tự.")
        if (students.orEmpty().any { it.studentCode.equals(cleanCode, ignoreCase = true) }) {
            return failure("Mã sinh viên đã tồn tại.")
        }
        if (students == null) students = mutableListOf()
        students?.add(Student(UUID.randomUUID().toString(), cleanName, cleanCode))
        return success("Đã thêm sinh viên $cleanName.")
    }

    fun borrow(studentId: String?, bookIds: List<String>?, now: Long = System.currentTimeMillis()): OperationResult {
        if (studentId.isNullOrBlank() || students.orEmpty().none { it.id == studentId }) {
            return failure("Vui lòng chọn sinh viên hợp lệ.")
        }
        val ids = bookIds.orEmpty().distinct()
        if (ids.isEmpty()) return failure("Hãy chọn ít nhất một cuốn sách.")
        val chosen = ids.map { id -> books.orEmpty().find { it.id == id } }
        if (chosen.any { it == null }) return failure("Có sách không còn trong thư viện.")
        if (loans.orEmpty().any { it.bookId in ids }) return failure("Có sách đang được mượn. Hãy chọn lại.")
        // ĐA HÌNH: cùng biến Book, loanDays() trả về 14 hoặc 7 theo đối tượng thực tế.
        // Kiểm tra cả nhóm trước khi ghi để tránh mượn thành công một phần.
        val newLoans = chosen.filterNotNull().map { book ->
            Loan(book.id, studentId, now, now + book.loanDays() * 86_400_000L)
        }
        if (loans == null) loans = mutableListOf()
        loans?.addAll(newLoans)
        return success("Đã mượn ${newLoans.size} cuốn sách.")
    }

    fun returnBook(studentId: String?, bookId: String?): OperationResult {
        if (studentId.isNullOrBlank() || bookId.isNullOrBlank()) return failure("Thiếu thông tin trả sách.")
        val removed = loans?.removeAll { it.studentId == studentId && it.bookId == bookId } == true
        return if (removed) success("Đã trả sách.") else failure("Sinh viên chưa mượn cuốn sách này.")
    }

    private fun failure(message: String): OperationResult = OperationResult(false, message)
    private fun success(message: String): OperationResult = OperationResult(true, message)
}

fun sampleLibrary(): LibrarySnapshot = LibrarySnapshot(
    books = listOf(
        Textbook("book-01", "Sách 01", "Nguyễn Văn An"),
        ReferenceBook("book-02", "Sách 02", "Trần Thị Bình"),
    ),
    students = listOf(
        Student("student-01", "Nguyen Van A", "SV001"),
        Student("student-02", "Tran Thi B", "SV002"),
        Student("student-03", "Le Van C", "SV003"),
    ),
    loans = emptyList(),
)
