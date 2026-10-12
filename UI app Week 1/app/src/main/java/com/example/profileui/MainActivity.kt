package com.example.profileui

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnEdit: ImageButton
    private lateinit var tvStudentName: TextView
    private lateinit var tvStudentID: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack)
        btnEdit = findViewById(R.id.btnEdit)
        tvStudentName = findViewById(R.id.tvStudentName)
        tvStudentID = findViewById(R.id.tvStudentID)

        // Hỗ trợ hiển thị viền tràn cạnh (Edge-to-edge / WindowInsets) mượt mà
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        // Xử lý sự kiện nút Quay lại (Back)
        btnBack.setOnClickListener {
            Toast.makeText(this, "Nhấn nút quay lại", Toast.LENGTH_SHORT).show()
        }

        // Xử lý sự kiện nút Chỉnh sửa (Edit)
        btnEdit.setOnClickListener {
            showEditProfileDialog()
        }
    }

    /**
     * Hiển thị Dialog cho phép chỉnh sửa Tên và MSSV trực tiếp trên ứng dụng
     */
    private fun showEditProfileDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Chỉnh sửa thông tin sinh viên")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val edtName = EditText(this).apply {
            hint = "Họ và tên"
            setText(tvStudentName.text)
        }

        val edtId = EditText(this).apply {
            hint = "Mã số sinh viên"
            setText(tvStudentID.text)
        }

        layout.addView(edtName)
        layout.addView(edtId)
        builder.setView(layout)

        builder.setPositiveButton("Lưu") { _, _ ->
            val newName = edtName.text.toString().trim()
            val newId = edtId.text.toString().trim()

            if (newName.isNotEmpty()) {
                tvStudentName.text = newName
            }
            if (newId.isNotEmpty()) {
                tvStudentID.text = newId
            }
            Toast.makeText(this, "Đã cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
