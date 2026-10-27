package com.example.quanlythuvien

import com.example.quanlythuvien.domain.*
import org.junit.Assert.*
import org.junit.Test

class LibraryManagerTest {
    @Test fun nullableDefaultsAreSafe() {
        val library = LibraryManager()
        assertTrue(library.snapshot().books.orEmpty().isEmpty())
        assertFalse(library.borrow(null, null).success == true)
        assertFalse(library.addBook(null, null, null).success == true)
        assertFalse(library.addStudent(null, null).success == true)
        assertFalse(library.returnBook(null, null).success == true)
        assertTrue(Textbook().information().contains("Chưa rõ tác giả"))
        assertNull(Student().name)
        assertNull(Loan().dueAt)
    }

    @Test fun polymorphismDeterminesActualDueDates() {
        val library = LibraryManager(sampleLibrary())
        val now = 1_700_000_000_000L
        assertTrue(library.borrow("student-01", listOf("book-01", "book-02"), now).success == true)
        val loans = library.snapshot().loans.orEmpty()
        assertEquals(now + 14L * 86_400_000L, loans.first { it.bookId == "book-01" }.dueAt)
        assertEquals(now + 7L * 86_400_000L, loans.first { it.bookId == "book-02" }.dueAt)
    }

    @Test fun borrowedBookCannotBeBorrowedTwice() {
        val library = LibraryManager(sampleLibrary())
        library.borrow("student-01", listOf("book-01"))
        assertFalse(library.borrow("student-02", listOf("book-01")).success == true)
        assertFalse(library.borrow("student-01", listOf("book-01")).success == true)
        assertEquals(1, library.snapshot().loans.orEmpty().size)
    }

    @Test fun batchBorrowIsAtomicWhenOneBookIsUnavailable() {
        val library = LibraryManager(sampleLibrary())
        library.borrow("student-01", listOf("book-01"))
        assertFalse(library.borrow("student-02", listOf("book-02", "book-01")).success == true)
        assertEquals(listOf("book-01"), library.snapshot().loans.orEmpty().map { it.bookId })
    }

    @Test fun invalidStudentOrBookCannotCreateLoans() {
        val library = LibraryManager(sampleLibrary())
        assertFalse(library.borrow("missing", listOf("book-01")).success == true)
        assertFalse(library.borrow("student-01", listOf("book-01", "missing")).success == true)
        assertFalse(library.borrow("student-01", emptyList()).success == true)
        assertTrue(library.snapshot().loans.orEmpty().isEmpty())
    }

    @Test fun repeatedIdsOnlyCreateOneLoan() {
        val library = LibraryManager(sampleLibrary())
        assertTrue(library.borrow("student-01", listOf("book-01", "book-01")).success == true)
        assertEquals(1, library.snapshot().loans.orEmpty().size)
    }

    @Test fun onlyBorrowerCanReturnAndBookBecomesAvailable() {
        val library = LibraryManager(sampleLibrary())
        library.borrow("student-01", listOf("book-01"))
        assertFalse(library.returnBook("student-02", "book-01").success == true)
        assertTrue(library.returnBook("student-01", "book-01").success == true)
        assertFalse(library.returnBook("student-01", "book-01").success == true)
        assertTrue(library.borrow("student-02", listOf("book-01")).success == true)
    }

    @Test fun studentCodeIsUniqueIgnoringSpacesAndCase() {
        val library = LibraryManager(sampleLibrary())
        assertFalse(library.addStudent("Test", " sv001 ").success == true)
        assertFalse(library.addStudent(" ", "SV004").success == true)
        assertTrue(library.addStudent(" Nguyen Van D ", " SV004 ").success == true)
        assertEquals("Nguyen Van D", library.snapshot().students.orEmpty().last().name)
        assertEquals("SV004", library.snapshot().students.orEmpty().last().studentCode)
    }

    @Test fun bookValidationAndSubclassSelection() {
        val library = LibraryManager()
        assertFalse(library.addBook(" ", "A", "textbook").success == true)
        assertFalse(library.addBook("B", " ", "textbook").success == true)
        assertFalse(library.addBook("B", "A", null).success == true)
        assertTrue(library.addBook(" Kotlin ", " Author ", "reference").success == true)
        val book = library.snapshot().books.orEmpty().single()
        assertTrue(book is ReferenceBook)
        assertEquals("Kotlin", book.title)
        assertEquals("Author", book.author)
    }

    @Test fun snapshotAndConstructorCannotExposeInternalCollections() {
        val external = sampleLibrary().books.orEmpty().toMutableList()
        val library = LibraryManager(LibrarySnapshot(books = external))
        external.clear()
        assertEquals(2, library.snapshot().books.orEmpty().size)
        val snapshot = library.snapshot()
        library.addBook("New", "Author", "textbook")
        assertEquals(2, snapshot.books.orEmpty().size)
        assertEquals(3, library.snapshot().books.orEmpty().size)
    }

    @Test fun restoredSnapshotPreservesBorrowingRules() {
        val original = LibraryManager(sampleLibrary())
        original.borrow("student-01", listOf("book-02"), 1000L)
        val restored = LibraryManager(original.snapshot())
        assertFalse(restored.borrow("student-02", listOf("book-02")).success == true)
        assertEquals(1000L + 7L * 86_400_000L, restored.snapshot().loans.orEmpty().single().dueAt)
    }
}
