package vn.edu.uth.vonglap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// String? cho phép email và phone nhận giá trị null.
data class Student(val name: String, val email: String?, val phone: String?)

private val students = listOf(
    Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
    Student("Trần Thị Bình", null, "0912 888 999"),
    Student("Lê Minh Chi", "chi@sv.edu.vn", null),
    Student("Phạm Quốc Dũng", null, null),
    Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
)

private val appBarColor = Color(0xFF304878)
private val backgroundColor = Color(0xFFF4F4F8)
private val primaryText = Color(0xFF282D33)
private val secondaryText = Color(0xFF7A7E83)
private val missingText = Color(0xFFAD6154)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(appBarColor.toArgb()),
            navigationBarStyle = SystemBarStyle.light(backgroundColor.toArgb(), backgroundColor.toArgb())
        )
        setContent { StudentListScreen() }
    }
}

@Composable
private fun StudentListScreen() {
    // Vòng lặp for thứ nhất: đếm số sinh viên thiếu email.
    var missingEmailCount = 0
    for (student in students) {
        if (student.email == null) missingEmailCount++
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = { AppBar("Danh sách sinh viên") }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            // Vòng lặp for thứ hai: tạo từng thẻ sinh viên.
            for (student in students) {
                Container {
                    Text(student.name, color = primaryText, fontSize = 16.sp,
                        lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
                    SizedBox(height = 4.dp)
                    Row {
                        Text("Email: ", color = secondaryText, fontSize = 14.sp, lineHeight = 20.sp)
                        Text(
                            text = student.email ?: "Chưa cập nhật",
                            color = if (student.email == null) missingText else secondaryText,
                            fontStyle = if (student.email == null) FontStyle.Italic else FontStyle.Normal,
                            fontSize = 14.sp, lineHeight = 20.sp
                        )
                    }
                    Row {
                        Text("SĐT: ", color = secondaryText, fontSize = 14.sp, lineHeight = 20.sp)
                        Text(
                            text = student.phone ?: "Chưa cập nhật",
                            color = if (student.phone == null) missingText else secondaryText,
                            fontStyle = if (student.phone == null) FontStyle.Italic else FontStyle.Normal,
                            fontSize = 14.sp, lineHeight = 20.sp
                        )
                    }
                }
                SizedBox(height = 12.dp)
            }
            Container(color = Color(0xFFE4EBF3), outlined = false) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(buildAnnotatedString {
                        append("Tổng: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${students.size} sinh viên")
                        }
                    }, color = Color(0xFF405575), fontSize = 14.sp, lineHeight = 20.sp)
                    Text(buildAnnotatedString {
                        append("Thiếu email: ")
                        withStyle(SpanStyle(color = missingText, fontWeight = FontWeight.Bold)) {
                            append(missingEmailCount.toString())
                        }
                    }, color = Color(0xFF405575), fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }
    }
}

// Compose không có widget tên AppBar, Container, SizedBox của Flutter.
// Các hàm tương đương dưới đây chỉ dùng Row và Column để bố trí nội dung.
@Composable
private fun AppBar(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(appBarColor)
            .statusBarsPadding().height(64.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Container(
    color: Color = Color.White,
    outlined: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier.fillMaxWidth().background(color, shape)
            .then(if (outlined) Modifier.border(1.dp, Color(0xFFE7E7EC), shape) else Modifier)
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun SizedBox(height: Dp) {
    Column(Modifier.height(height)) {}
}
