# UTH Todo — Navigation + OOP

Dự án Kotlin / Jetpack Compose tái tạo **đúng 4 màn** trong bài tập: Start và 3 màn onboarding UTH Todo V2.

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

## Đối chiếu Figma

- Nguồn: `docs/figma/`, giữ nguyên 4 SVG người dùng cung cấp.
- Start: **400 × 860**. Onboarding: **390 × 844**.
- Các file Section có nhãn tên nằm ngoài khung điện thoại. Ứng dụng lấy vùng `x=3, y≈31, w=390, h=844` để bỏ nhãn và bóng của khung ngoài.
- Màu chính `#2F5BEA`; font **Be Vietnam Pro**, đủ trọng lượng 400/500/600/700/800, có sẵn trong ứng dụng và dùng được khi ngoại tuyến.
- Logo, 3 hình minh họa và bóng đổ được xuất từ SVG ở **3x**.
- Tiêu đề 28/36 px, tracking −0,4 px; mô tả 16/24 px; chữ nút 17 px, đậm 600.
- Lề ngang 28 px, logo 32 × 32 px ở (28,66), hình minh họa 280 × 300 px ở (55,171), nút cao 56 px ở y=748, bo góc 16 px.
- Nội dung được bố trí theo hệ tọa độ gốc rồi co giãn đồng đều theo màn hình để giữ tỷ lệ. Thanh hệ thống ẩn để khớp bản thiết kế; vùng thừa trên thiết bị khác tỷ lệ dùng cùng màu nền.
- Bo góc 44 px trong Figma mô phỏng mép điện thoại; màn Android thực tế dùng mép màn hình của thiết bị.

## Kiểm tra

```powershell
$env:JAVA_HOME = 'C:/Users/Acer/.jdks/jbr-17.0.14'
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
.\gradlew.bat connectedDebugAndroidTest
```

Unit test kiểm tra chuyển trang, quay lại, bỏ qua và chỉ số khôi phục ngoài phạm vi. Instrumentation test kiểm tra thao tác Compose và lưu ảnh 4 màn để đối chiếu.

APK debug sau build: `app/build/outputs/apk/debug/app-debug.apk`.

Kết quả đã thực hiện: **5 unit test và 6 kiểm thử Android đạt**, Android Lint **0 lỗi**. Xem `docs/VERIFICATION.md` và ảnh đối chiếu `docs/comparison.png`.

Có thể tái xuất tài nguyên bằng `node tools/export-figma-assets.cjs` sau khi cài `@resvg/resvg-js@2.6.2` trong `tools/`. Công cụ này chỉ dùng khi phát triển; ứng dụng Android không cần Node.js.

Font phát hành theo SIL Open Font License, xem `docs/FONT-LICENSE.txt` và [kho Google Fonts](https://github.com/google/fonts/tree/main/ofl/bevietnampro).
