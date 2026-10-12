# Thực hành 03 — Kotlin

Nhập hai số, chọn một trong bốn phép toán +, -, *, /; kết quả cập nhật ngay. Toàn bộ giao diện, trạng thái và tính toán nằm trong `app/src/main/java/vn/edu/uth/thuchanh03/MainActivity.kt`.

## Chạy bài

1. Mở thư mục này bằng Android Studio, đợi Gradle Sync xong.
2. Chọn Gradle JDK `jbr-21`, cấu hình chạy `app` và máy ảo `Medium Phone`.
3. Nhấn Run ▶, chờ ứng dụng mở trong **Running Devices**.
4. Bấm vào hai ô trên điện thoại để nhập số, rồi bấm phép toán. Ví dụ: 4 + 4 hiển thị 8. Có thể gõ số bằng bàn phím máy tính khi ô nhập đang được chọn.

Nhấp đúp `MainActivity.kt` trong cây thư mục để mở mã nguồn. Chọn **Code** (biểu tượng ba dòng ở góc phải trình soạn thảo) nếu Android Studio hiện khung **Split/Preview**. Các nút gợi ý AI trong khung đó là của Android Studio; ứng dụng chỉ có màn hình tính toán.

Ứng dụng giữ dữ liệu khi xoay màn hình, chấp nhận dấu chấm/dấu phẩy thập phân và báo lỗi khi nhập sai hoặc chia cho 0.

Build bằng PowerShell: `.\gradlew.ps1 assembleDebug lintDebug`.
APK: `app/build/outputs/apk/debug/app-debug.apk`.
