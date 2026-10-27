package com.example.quanlythuvien.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlythuvien.domain.LibrarySnapshot
import com.example.quanlythuvien.domain.Loan
import com.example.quanlythuvien.domain.sampleLibrary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2D55A5),
            onPrimary = Color.White,
            background = Color.White,
            surface = Color.White,
            onSurface = Color(0xFF171717),
            surfaceVariant = Color(0xFFF0F1F4),
        ),
        content = content,
    )
}

@Composable
fun LibraryApp(model: LibraryViewModel) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val snackbar = remember { SnackbarHostState() }
    val message = model.message
    LaunchedEffect(message) {
        if (message != null) {
            snackbar.showSnackbar(message, withDismissAction = true)
            model.clearMessage()
        }
    }
    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color(0xFFC4C4C4))
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    listOf("Quản lý", "DS Sách", "Sinh viên").forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = {
                                Icon(
                                    when (index) {
                                        0 -> Icons.Default.Home
                                        1 -> Icons.AutoMirrored.Filled.List
                                        else -> Icons.Default.Person
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                )
                            },
                            label = { Text(label, fontSize = 14.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF5D8FBB),
                                selectedTextColor = Color(0xFF507FA8),
                                indicatorColor = Color.White,
                                unselectedIconColor = Color(0xFFB8B8B8),
                                unselectedTextColor = Color(0xFF858585),
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).imePadding(), contentAlignment = Alignment.TopCenter) {
            val state = model.state
            when {
                model.loadFailed == true -> Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Không đọc được dữ liệu thư viện.")
                    Button(onClick = model::reload, enabled = model.busy != true) { Text("Thử lại") }
                }
                state == null -> CircularProgressIndicator(Modifier.padding(40.dp))
                else -> {
                    // Giữ biểu mẫu và vị trí cuộn của từng tab khi chuyển trang.
                    val holder = androidx.compose.runtime.saveable.rememberSaveableStateHolder()
                    holder.SaveableStateProvider(tab) {
                        when (tab) {
                            0 -> ManagementScreen(state, model.busy == true, model::borrow, model::returnBook)
                            1 -> BooksScreen(state, model.busy == true, model::addBook)
                            2 -> StudentsScreen(state, model.busy == true, model::addStudent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PageTitle() {
    Text(
        "Hệ thống\nQuản lý Thư viện",
        fontSize = 23.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 28.dp),
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
}

@Composable
private fun ManagementScreen(
    state: LibrarySnapshot,
    busy: Boolean,
    onBorrow: (String?, List<String>?, () -> Unit) -> Unit,
    onReturn: (String?, String?) -> Unit,
) {
    var selectedStudentId by rememberSaveable { mutableStateOf(state.students.orEmpty().firstOrNull()?.id) }
    var selectedBooks by rememberSaveable { mutableStateOf(arrayListOf<String>()) }
    var choosingStudent by rememberSaveable { mutableStateOf(false) }
    val student = state.students.orEmpty().find { it.id == selectedStudentId }
        ?: state.students.orEmpty().firstOrNull()
    val loans = state.loans.orEmpty()
    val owned = loans.filter { it.studentId == student?.id && student?.id != null }
    LaunchedEffect(state.loans) {
        selectedBooks = ArrayList(selectedBooks.filter { id -> loans.none { it.bookId == id } })
    }
    Column(
        Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        PageTitle()
        Spacer(Modifier.height(38.dp))
        SectionTitle("Sinh viên")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = student?.name ?: "Chưa có sinh viên",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.SemiBold),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f),
            )
            Box {
                Button(
                    onClick = { choosingStudent = true },
                    enabled = state.students.orEmpty().isNotEmpty() && !busy,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
                ) { Text("Thay đổi", fontWeight = FontWeight.Bold) }
                DropdownMenu(expanded = choosingStudent, onDismissRequest = { choosingStudent = false }) {
                    state.students.orEmpty().forEach { item ->
                        DropdownMenuItem(
                            text = { Text("${item.name ?: "Chưa có tên"} (${item.studentCode ?: "—"})") },
                            onClick = {
                                selectedStudentId = item.id
                                selectedBooks = arrayListOf()
                                choosingStudent = false
                            },
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        SectionTitle("Danh sách sách")
        Column(
            Modifier.fillMaxWidth().heightIn(min = 210.dp, max = 340.dp)
                .clip(RoundedCornerShape(12.dp)).background(Color(0xFFD9D9D9))
                .verticalScroll(rememberScrollState()).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (state.books.orEmpty().isEmpty()) Text("Chưa có sách. Thêm sách ở trang DS Sách.", Modifier.padding(12.dp))
            state.books.orEmpty().forEach { book ->
                val loan = loans.find { it.bookId == book.id }
                val enabled = loan == null && book.id != null && student != null && !busy
                val checked = book.id in selectedBooks || loan?.studentId == student?.id && loan != null
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    shadowElevation = 3.dp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().toggleable(
                        value = checked,
                        enabled = enabled,
                        role = Role.Checkbox,
                        onValueChange = { value ->
                            book.id?.let { id ->
                                selectedBooks = ArrayList(if (value) selectedBooks + id else selectedBooks - id)
                            }
                        },
                    ),
                ) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = null,
                            enabled = enabled,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFA52C44),
                                disabledCheckedColor = Color(0xFFA52C44),
                            ),
                            modifier = Modifier.padding(8.dp),
                        )
                        Column(Modifier.weight(1f).padding(vertical = 6.dp)) {
                            Text(book.title ?: "Chưa có tên sách", fontSize = 17.sp, color = Color(0xFF505050))
                            if (loan != null) Text(
                                if (loan.studentId == student?.id) "Đang mượn • Hạn ${formatDate(loan.dueAt)}"
                                else "Đã có người mượn",
                                fontSize = 12.sp, color = Color(0xFF7B5560),
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Button(
            onClick = { onBorrow(student?.id, selectedBooks.toList()) { selectedBooks = arrayListOf() } },
            enabled = !busy && student != null,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally).width(180.dp).heightIn(min = 48.dp),
        ) { Text(if (busy) "Đang lưu…" else "Thêm", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        Text(
            "Chọn sách rồi nhấn Thêm để mượn.",
            color = Color(0xFF757575), fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
        )
        if (owned.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            SectionTitle("Sách đang mượn (${owned.size})")
            owned.forEach { loan ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(state.books.orEmpty().find { it.id == loan.bookId }?.title ?: "Chưa có tên sách", fontWeight = FontWeight.Medium)
                        Text("Hạn trả: ${formatDate(loan.dueAt)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = { onReturn(student?.id, loan.bookId) }, enabled = !busy) { Text("Trả sách") }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun BooksScreen(
    state: LibrarySnapshot,
    busy: Boolean,
    onAdd: (String?, String?, String?, () -> Unit) -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("textbook") }
    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PageTitle()
        SectionTitle("Danh sách sách (${state.books.orEmpty().size})")
        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF1F2F5)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Thêm sách mới", fontWeight = FontWeight.Bold)
                OutlinedTextField(title, { title = it.take(120) }, label = { Text("Tên sách") }, singleLine = true, enabled = !busy, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(author, { author = it.take(80) }, label = { Text("Tác giả") }, singleLine = true, enabled = !busy, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(type == "textbook", { type = "textbook" }, label = { Text("Giáo trình") }, enabled = !busy)
                    FilterChip(type == "reference", { type = "reference" }, label = { Text("Tham khảo") }, enabled = !busy)
                }
                Text("Thời hạn mượn: ${if (type == "textbook") 14 else 7} ngày", fontSize = 13.sp)
                Button(
                    onClick = { keyboard?.hide(); onAdd(title, author, type) { title = ""; author = "" } },
                    enabled = !busy, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(),
                ) { Text("Thêm sách") }
            }
        }
        if (state.books.orEmpty().isEmpty()) Text("Thư viện chưa có sách.")
        state.books.orEmpty().forEach { book ->
            InfoCard {
                Text(book.title ?: "Chưa có tên sách", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                // Đa hình cũng được sử dụng trực tiếp khi hiển thị thông tin sách.
                Text(book.information(), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val loan = state.loans.orEmpty().find { it.bookId == book.id }
                if (loan == null) Text("Có thể mượn", color = Color(0xFF28754A), fontSize = 13.sp)
                else {
                    val borrower = state.students.orEmpty().find { it.id == loan.studentId }
                    Text("Đang mượn: ${borrower?.name ?: "Chưa rõ sinh viên"}", color = Color(0xFFA52C44), fontSize = 13.sp)
                    Text("Hạn trả: ${formatDate(loan.dueAt)}", fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun StudentsScreen(
    state: LibrarySnapshot,
    busy: Boolean,
    onAdd: (String?, String?, () -> Unit) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    Column(
        Modifier.widthIn(max = 600.dp).fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PageTitle()
        SectionTitle("Sinh viên (${state.students.orEmpty().size})")
        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF1F2F5)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Thêm sinh viên mới", fontWeight = FontWeight.Bold)
                OutlinedTextField(name, { name = it.take(80) }, label = { Text("Họ và tên") }, singleLine = true, enabled = !busy, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(code, { code = it.take(30) }, label = { Text("Mã sinh viên") }, singleLine = true, enabled = !busy, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = { keyboard?.hide(); onAdd(name, code) { name = ""; code = "" } },
                    enabled = !busy, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(),
                ) { Text("Thêm sinh viên") }
            }
        }
        if (state.students.orEmpty().isEmpty()) Text("Chưa có sinh viên.")
        state.students.orEmpty().forEach { student ->
            InfoCard {
                Text(student.name ?: "Chưa có tên", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("MSSV: ${student.studentCode ?: "—"}", fontSize = 13.sp)
                val borrowed = state.loans.orEmpty().filter { it.studentId == student.id && student.id != null }
                Text("Đang mượn: ${borrowed.size} cuốn", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                borrowed.forEach { loan: Loan ->
                    val book = state.books.orEmpty().find { it.id == loan.bookId }
                    Text("• ${book?.title ?: "Chưa có tên sách"} — hạn ${formatDate(loan.dueAt)}", fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
    ) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp), content = content) }
}

private fun formatDate(timestamp: Long?): String =
    timestamp?.let { SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN")).format(Date(it)) } ?: "Chưa xác định"

@Preview(showBackground = true, widthDp = 393, heightDp = 800)
@Composable
private fun ManagementPreview() {
    LibraryTheme { ManagementScreen(sampleLibrary(), false, { _, _, _ -> }, { _, _ -> }) }
}
