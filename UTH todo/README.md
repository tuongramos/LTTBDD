# UTH Todo — Navigation + OOP

Dự án Kotlin / Jetpack Compose.

## Mở và chạy trong Android Studio

1. Chọn **Open** và mở thư mục `UTH todo` chứa file `settings.gradle.kts`.
2. Dùng **JDK 17** cho Gradle. Máy hiện tại có `C:\Users\Acer\.jdks\jbr-17.0.14`. Vào **Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK** nếu cần đổi. Gradle 8.10.2 của dự án cũng hỗ trợ JDK 21.
3. Chờ Gradle Sync, chọn thiết bị Android API 24 trở lên và chạy cấu hình **app**.
4. Xem từng màn trong Compose Preview của `OnboardingScreens.kt`.

SDK hiện tại được cấu hình bằng `local.properties`: `D:\Androidstudio\Sdk`. Nếu mở trên máy khác, Android Studio sẽ tạo lại file này.

## Hành vi

- Start hiển thị 2,4 giây rồi chuyển sang onboarding đầu tiên. Có thể chạm logo để chuyển sớm.
- **Tiếp tục** mở trang kế tiếp.
- **Bỏ qua** đưa tới trang cuối; các dấu chấm hoặc vuốt hình minh họa cho phép đổi trang.
- Nút Back của Android quay lại trang trước.
- **Bắt đầu ngay** hoàn tất phần giới thiệu và hiện thông báo xác nhận ngay trên trang cuối. Phạm vi bài chỉ gồm 4 màn nên không có màn trang chủ mới.
- ViewModel và SavedStateHandle giữ trang hiện tại khi Activity được tạo lại. Mỗi lần khởi động mới vẫn có thể xem đủ bài tập.

## OOP

| Thành phần | Vai trò |
|---|---|
| `AppBrand`, `Illustration`, `OnboardingPage`, `OnboardingLabels`, `OnboardingContent` | Các đối tượng dữ liệu bất biến |
| `OnboardingRepository` | Interface cung cấp dữ liệu |
| `FigmaOnboardingRepository` | Lớp triển khai, chứa nội dung 4 màn |
| `AppScreen` | Sealed interface mô tả trạng thái màn hình |
| `OnboardingNavigator` | Đóng gói quy tắc chuyển trang, giới hạn chỉ số trang |
| `OnboardingUiState`, `OnboardingViewModel` | Đóng gói trạng thái, chỉ thay đổi qua phương thức công khai |
| `DesignFrame`, `TodoDesign` | Kích thước, màu sắc và kiểu chữ dùng chung |

UI nhận đối tượng nội dung và callback. Tài nguyên minh họa là hình trang trí trích từ SVG gốc.
## Kiểm tra

```powershell
$env:JAVA_HOME = 'C:/Users/Acer/.jdks/jbr-17.0.14'
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
.\gradlew.bat connectedDebugAndroidTest
```

APK debug sau build: `app/build/outputs/apk/debug/app-debug.apk`.
