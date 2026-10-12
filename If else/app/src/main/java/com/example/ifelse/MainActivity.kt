package com.example.ifelse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XepLoaiHocLucApp()
        }
    }
}


// 1. Khai báo biến điểm có giá trị 8.5

val diem: Double = 8.5


// 2. Hàm nhận vào điểm và trả về xếp loại

fun xepLoai(diem: Double): String {
    if (diem < 0 || diem > 10) {
        return "Không hợp lệ"
    } else if (diem >= 8.0) {
        return "Giỏi"
    } else if (diem >= 6.5) {
        return "Khá"
    } else if (diem >= 5.0) {
        return "Trung bình"
    } else {
        return "Yếu"
    }
}


// 3. Hàm chọn màu chữ xếp loại bằng if

fun mauXepLoai(loai: String): Color {
    if (loai == "Giỏi") {
        return Color(0xFF2E7D32) // xanh lá
    } else if (loai == "Khá") {
        return Color(0xFF1565C0) // xanh dương
    } else if (loai == "Trung bình") {
        return Color(0xFFE65100) // cam
    } else if (loai == "Yếu") {
        return Color(0xFFC62828) // đỏ
    } else {
        return Color(0xFF616161) // xám
    }
}


// Giao diện chính

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XepLoaiHocLucApp() {
    // Xếp loại cho điểm chính
    val loai = xepLoai(diem)
    val mau = mauXepLoai(loai)

    // 4. Kiểm tra thêm với các điểm 9, 7, 5.5, 3, 11
    val danhSachDiem = listOf(9.0, 7.0, 5.5, 3.0, 11.0)

    Scaffold(
        topBar = {
            // AppBar
            TopAppBar(
                title = {
                    Text(
                        text = "Xếp loại học lực",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5C6BC0)
                )
            )
        }
    ) { paddingValues ->
        // Column chính
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // SizedBox phía trên
            Spacer(modifier = Modifier.height(32.dp))

            // Container hiển thị điểm trung bình
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Text "Điểm trung bình"
                    Text(
                        text = "Điểm trung bình",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    // SizedBox
                    Spacer(modifier = Modifier.height(8.dp))

                    // Text hiển thị điểm
                    Text(
                        text = diem.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    // SizedBox
                    Spacer(modifier = Modifier.height(12.dp))

                    // Container chứa xếp loại
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = mau
                    ) {
                        Text(
                            text = loai,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // SizedBox
            Spacer(modifier = Modifier.height(32.dp))

            // Text tiêu đề "KIỂM TRA THÊM"
            Text(
                text = "KIỂM TRA THÊM",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // SizedBox
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Gọi hàm với các điểm 9, 7, 5.5, 3 và 11
            for (d in danhSachDiem) {
                val loaiPhu = xepLoai(d)
                val mauPhu = mauXepLoai(loaiPhu)

                // Container cho mỗi dòng kiểm tra
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    // Row hiển thị điểm và xếp loại
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Text hiển thị điểm
                        Text(
                            text = "Điểm ${if (d == d.toLong().toDouble()) d.toLong().toString() else d.toString()}",
                            fontSize = 15.sp,
                            color = Color(0xFF333333)
                        )

                        // Container chứa nhãn xếp loại
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = mauPhu
                        ) {
                            Text(
                                text = loaiPhu,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // SizedBox phía dưới
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
