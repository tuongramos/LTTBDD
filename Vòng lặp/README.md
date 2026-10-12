# Bài 1 — Vòng lặp for và kiểu nullable

Mở thư mục này bằng Android Studio, chọn Gradle JDK 21, đợi Sync xong rồi chạy cấu hình **app**.

Toàn bộ bài nằm trong `app/src/main/java/vn/edu/uth/vonglap/MainActivity.kt`:
- `Student(name: String, email: String?, phone: String?)` và 5 sinh viên khai báo sẵn.
- Dùng `for` để tạo thẻ sinh viên và `for` để đếm email bị thiếu.
- Dùng `?:` của Kotlin (tương đương `??` trong Dart) để hiện “Chưa cập nhật”.
- Cuối danh sách: tổng 5 sinh viên, thiếu email 2.
- Chỉ dùng Scaffold, AppBar, Column, Row, Container, Text, SizedBox. AppBar, Container và SizedBox được định nghĩa bằng Row/Column vì Compose không có các widget Flutter cùng tên.

Không có Preview, dữ liệu mạng hay thư viện kiểm thử. Nếu Android Studio mở khung Split, chọn **Code** ở góc phải để chỉ xem mã.

Build trong PowerShell: `.\gradlew.ps1 assembleDebug lintDebug`.
APK: `app/build/outputs/apk/debug/app-debug.apk`.
