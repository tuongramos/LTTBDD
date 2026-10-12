package com.example.bangdiemlop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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

// ① Tạo class Student gồm String name và Double? score
data class Student(
    val name: String,
    val score: Double?
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BangDiemLopApp()
        }
    }
}

// Hàm xếp loại (dùng lại hàm Bài 2)
fun xepLoai(score: Double): String {
    return if (score >= 8.0) "Giỏi"
    else if (score >= 6.5) "Khá"
    else if (score >= 5.0) "Trung bình"
    else "Yếu"
}

// Màu cho từng xếp loại
fun mauXepLoai(loai: String): Color {
    return when (loai) {
        "Giỏi" -> Color(0xFF2E7D32)
        "Khá" -> Color(0xFF1565C0)
        "Trung bình" -> Color(0xFFE6A700)
        "Yếu" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangDiemLopApp() {
    // Dữ liệu khai báo sẵn trong code
    val danhSach = listOf(
        Student("Nguyễn Văn An", 9.0),
        Student("Trần Thị Bình", 7.2),
        Student("Lê Minh Chi", null),       // chưa có điểm
        Student("Phạm Quốc Dũng", 4.5),
        Student("Võ Thanh Em", 5.8)
    )

    // ③ Dùng for + if để tính
    var tongDiem = 0.0
    var soCoGiem = 0
    var soGioi = 0
    var soChuaCoDiem = 0
    var diemCaoNhat = 0.0

    for (sv in danhSach) {
        if (sv.score != null) {
            tongDiem += sv.score
            soCoGiem++
            if (sv.score >= 8.0) {
                soGioi++
            }
            if (sv.score > diemCaoNhat) {
                diemCaoNhat = sv.score
            }
        } else {
            soChuaCoDiem++
        }
    }

    val diemTB = if (soCoGiem > 0) tongDiem / soCoGiem else 0.0

    // Màu nền
    val darkBlue = Color(0xFF2C3E6B)
    val lightGray = Color(0xFFF0F0F0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bảng điểm lớp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = darkBlue
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGray)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // === PHẦN THỐNG KÊ ===
            // Container tổng hợp điểm trung bình
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = darkBlue
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Điểm trung bình lớp",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format("%.2f", diemTB),
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Đường kẻ ngang trắng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row: Giỏi | Chưa có điểm | Cao nhất
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Giỏi
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Giỏi",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$soGioi",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Chưa có điểm
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Chưa có điểm",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$soChuaCoDiem",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Cao nhất
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Cao nhất",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("%.1f", diemCaoNhat),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === DANH SÁCH SINH VIÊN ===
            Text(
                text = "DANH SÁCH · ${danhSach.size} SINH VIÊN",
                color = Color.Gray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ② Dùng for hiển thị tên, điểm, xếp loại
            for (sv in danhSach) {
                // Container cho mỗi sinh viên
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Cột trái: tên + xếp loại
                        Column {
                            Text(
                                text = sv.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (sv.score != null) {
                                val loai = xepLoai(sv.score)
                                val mauPhu = mauXepLoai(loai)
                                Text(
                                    text = loai,
                                    color = mauPhu,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    text = "Chưa có điểm",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        // Bên phải: điểm số
                        if (sv.score != null) {
                            Text(
                                text = String.format("%.1f", sv.score),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = "—",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
