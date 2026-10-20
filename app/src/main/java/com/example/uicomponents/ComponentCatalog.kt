package com.example.uicomponents

/** The catalog is the single source for the list, detail pages and sample code. */
enum class UiComponent(
    val title: String,
    val group: String,
    val summary: String,
    val explanation: String,
    val code: String,
) {
    TEXT("Text", "Display", "Hiển thị và định dạng văn bản",
        "Text hỗ trợ màu sắc, kích thước, độ đậm, chữ nghiêng và gạch chân. Dùng AnnotatedString để kết hợp nhiều kiểu trong cùng một đoạn.",
        "Text(\n    text = \"Hello, Compose!\",\n    fontSize = 28.sp,\n    fontWeight = FontWeight.Bold\n)"),
    IMAGE("Image", "Display", "Hiển thị hình ảnh",
        "Image hiển thị tài nguyên drawable. ContentScale.Crop cắt ảnh để lấp đầy khung; Fit giữ toàn bộ ảnh. Hai hình minh họa được đóng gói trong app nên hoạt động offline.",
        "Image(\n    painter = painterResource(R.drawable.campus),\n    contentDescription = \"Khuôn viên trường\",\n    contentScale = ContentScale.Crop\n)"),
    ICON("Icon", "Display", "Biểu tượng nhỏ mô tả hành động",
        "Icon thường xuất hiện trong nút hoặc thanh điều hướng. Cần có contentDescription khi biểu tượng truyền đạt thông tin hoặc thực hiện hành động.",
        "Icon(\n    imageVector = Icons.Default.Favorite,\n    contentDescription = \"Yêu thích\",\n    tint = Color.Red\n)"),
    TEXT_FIELD("TextField", "Input", "Nhập và cập nhật văn bản",
        "TextField nhận dữ liệu qua value và trả thay đổi qua onValueChange. State thay đổi sẽ cập nhật giao diện ngay lập tức.",
        "var text by rememberSaveable { mutableStateOf(\"\") }\nOutlinedTextField(\n    value = text,\n    onValueChange = { text = it },\n    label = { Text(\"Thông tin nhập\") }\n)\nText(text)"),
    PASSWORD("PasswordField", "Input", "Nhập mật khẩu với ký tự được che",
        "PasswordField được tạo từ TextField kết hợp PasswordVisualTransformation. Nút Hiện/Ẩn giúp kiểm tra nội dung. Ví dụ không lưu mật khẩu khi rời màn hình.",
        "var password by remember { mutableStateOf(\"\") }\nOutlinedTextField(\n    value = password,\n    onValueChange = { password = it },\n    visualTransformation = PasswordVisualTransformation(),\n    keyboardOptions = KeyboardOptions(\n        keyboardType = KeyboardType.Password\n    )\n)"),
    TOGGLE("Toggle", "Input", "Công tắc bật hoặc tắt",
        "Trong Jetpack Compose, Toggle được triển khai bằng Switch. checked biểu diễn trạng thái và onCheckedChange nhận giá trị mới.",
        "var enabled by rememberSaveable { mutableStateOf(true) }\nSwitch(checked = enabled, onCheckedChange = { enabled = it })"),
    SLIDER("Slider", "Input", "Chọn một giá trị trong khoảng",
        "Kéo thanh trượt để thay đổi giá trị. valueRange xác định khoảng hợp lệ; steps chia thanh trượt thành các mốc rời rạc khi cần.",
        "var volume by rememberSaveable { mutableFloatStateOf(50f) }\nSlider(\n    value = volume,\n    onValueChange = { volume = it },\n    valueRange = 0f..100f\n)"),
    DATE_PICKER("DatePicker", "Input", "Chọn ngày từ lịch",
        "DatePicker hiển thị lịch trong DatePickerDialog. Chỉ cập nhật kết quả khi người dùng nhấn Chọn; Hủy giữ nguyên ngày trước đó.",
        "val state = rememberDatePickerState()\nDatePickerDialog(\n    onDismissRequest = { open = false },\n    confirmButton = {\n        TextButton(onClick = {\n            selectedDate = state.selectedDateMillis\n            open = false\n        }) { Text(\"Chọn\") }\n    }\n) { DatePicker(state = state) }"),
    BUTTON("Button", "Selection", "Thực hiện hành động khi nhấn",
        "Button dùng cho hành động chính, OutlinedButton cho hành động phụ và TextButton cho hành động ít nhấn mạnh. Nhấn nút để thử cập nhật bộ đếm.",
        "var count by rememberSaveable { mutableIntStateOf(0) }\nButton(onClick = { count++ }) { Text(\"Nhấn thử\") }\nText(\"Đã nhấn: ${'$'}count\")"),
    CHECKBOX("Checkbox", "Selection", "Chọn một hoặc nhiều tùy chọn",
        "Checkbox cho phép nhiều lựa chọn độc lập. Nhấn cả dòng hoặc ô vuông để thay đổi lựa chọn.",
        "var checked by rememberSaveable { mutableStateOf(false) }\nCheckbox(\n    checked = checked,\n    onCheckedChange = { checked = it }\n)"),
    RADIO("RadioButton", "Selection", "Chọn duy nhất một tùy chọn",
        "Các RadioButton dùng chung một state lựa chọn. Khi chọn mục mới, mục cũ tự bỏ chọn. selectableGroup hỗ trợ trình đọc màn hình.",
        "var selected by rememberSaveable { mutableStateOf(\"Kotlin\") }\nRadioButton(\n    selected = selected == \"Kotlin\",\n    onClick = { selected = \"Kotlin\" }\n)"),
    PICKER("Picker", "Selection", "Chọn một giá trị trong danh sách",
        "Ví dụ dùng DropdownMenu để triển khai Picker. Nhấn nút, chọn một giá trị và xem kết quả cập nhật.",
        "DropdownMenu(expanded = expanded,\n    onDismissRequest = { expanded = false }) {\n    options.forEach { option ->\n        DropdownMenuItem(\n            text = { Text(option) },\n            onClick = { selected = option; expanded = false }\n        )\n    }\n}"),
    CHIP("Chip", "Selection", "Lọc nội dung bằng các nhãn lựa chọn",
        "FilterChip là lựa chọn gọn cho bộ lọc hoặc chủ đề. Ví dụ cho phép chọn nhiều chủ đề và hiển thị các lựa chọn hiện tại.",
        "FilterChip(\n    selected = selected,\n    onClick = { selected = !selected },\n    label = { Text(\"Compose\") }\n)"),
    NAVIGATION("NavigationView", "Navigation", "Tiêu đề và điều hướng quay lại",
        "Tên NavigationView trong đề tương ứng với vùng điều hướng. Trên Compose có thể kết hợp Scaffold, TopAppBar và back stack. App này dùng state lưu màn hình và BackHandler cho luồng Giới thiệu → Danh sách → Chi tiết.",
        "BackHandler(enabled = page != \"welcome\") {\n    page = if (page == \"detail\") \"catalog\" else \"welcome\"\n}\nScaffold(topBar = {\n    TopAppBar(title = { Text(\"Chi tiết\") })\n}) { padding -> /* Nội dung màn hình */ }"),
    TAB_BAR("TabBar", "Navigation", "Chuyển giữa các vùng nội dung",
        "TabRow chứa các Tab dùng chung chỉ số selectedTabIndex. Chọn tab sẽ đổi vùng nội dung bên dưới mà không mở thêm trang chính.",
        "TabRow(selectedTabIndex = selected) {\n    tabs.forEachIndexed { index, title ->\n        Tab(selected = selected == index,\n            onClick = { selected = index },\n            text = { Text(title) })\n    }\n}"),
    MENU("Menu", "Navigation", "Hiển thị nhiều hành động",
        "DropdownMenu mở từ một nút neo. Chọn một hành động để hiển thị kết quả, hoặc chạm ra ngoài để đóng menu.",
        "DropdownMenu(expanded = expanded,\n    onDismissRequest = { expanded = false }) {\n    DropdownMenuItem(\n        text = { Text(\"Sao chép\") },\n        onClick = { expanded = false /* Xử lý hành động */ }\n    )\n}"),
    LIST("List", "Content", "Hiển thị danh sách có thể cuộn",
        "LazyColumn chỉ dựng các mục cần hiển thị, phù hợp với danh sách dài. Cuộn danh sách bên dưới và chọn một bài học.",
        "LazyColumn {\n    items(lessons, key = { it.id }) { lesson ->\n        ListItem(headlineContent = { Text(lesson.title) })\n    }\n}"),
    CARD("Card", "Content", "Nhóm nội dung trong một thẻ",
        "Card nhóm các thông tin liên quan trong một bề mặt có bo góc. Có thể chứa hình, tiêu đề, mô tả và hành động.",
        "ElevatedCard {\n    Column(Modifier.padding(16.dp)) {\n        Text(\"Jetpack Compose\")\n        Text(\"Xây dựng UI với Kotlin\")\n    }\n}"),
    DIVIDER("Divider", "Content", "Phân tách các vùng nội dung",
        "HorizontalDivider và VerticalDivider tạo đường phân cách nhẹ. VerticalDivider cần chiều cao xác định từ vùng chứa.",
        "Text(\"Phần trên\")\nHorizontalDivider(Modifier.padding(vertical = 16.dp))\nText(\"Phần dưới\")"),
    COLUMN("Column", "Layout", "Sắp xếp phần tử theo chiều dọc",
        "Column đặt các phần tử từ trên xuống dưới. verticalArrangement điều khiển khoảng cách; horizontalAlignment điều khiển căn ngang.",
        "Column(\n    verticalArrangement = Arrangement.spacedBy(12.dp),\n    horizontalAlignment = Alignment.CenterHorizontally\n) {\n    Text(\"Phần tử 1\")\n    Text(\"Phần tử 2\")\n    Text(\"Phần tử 3\")\n}"),
    ROW("Row", "Layout", "Sắp xếp phần tử theo chiều ngang",
        "Row đặt các phần tử cạnh nhau. Modifier.weight(1f) giúp các ô chia đều chiều rộng; Arrangement.spacedBy tạo khoảng cách giữa các ô.",
        "Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {\n    repeat(3) {\n        Box(Modifier.weight(1f).height(64.dp)\n            .background(Color.Blue, RoundedCornerShape(12.dp)))\n    }\n}"),
    BOX("Box", "Layout", "Xếp chồng và căn vị trí phần tử",
        "Box cho phép các phần tử chồng lên nhau. Phần tử khai báo sau nằm trên; Modifier.align đặt nó vào góc hoặc giữa vùng chứa.",
        "Box(Modifier.size(180.dp)) {\n    Image(painter, contentDescription = null)\n    Text(\"Nhãn\", Modifier.align(Alignment.BottomStart))\n}"),
    PROGRESS("ProgressIndicator", "Feedback", "Biểu diễn tiến trình công việc",
        "LinearProgressIndicator và CircularProgressIndicator thể hiện tiến độ từ 0 đến 1. Thanh trượt bên dưới mô phỏng mức hoàn thành.",
        "LinearProgressIndicator(progress = { progress })\nCircularProgressIndicator(progress = { progress })"),
    DIALOG("AlertDialog", "Feedback", "Hộp thoại cần người dùng phản hồi",
        "AlertDialog tạm ngắt luồng để yêu cầu quyết định. Ví dụ có nút xác nhận và hủy; kết quả được hiển thị ngay dưới nút mở.",
        "AlertDialog(\n    onDismissRequest = { open = false },\n    title = { Text(\"Hoàn thành bài học?\") },\n    confirmButton = {\n        TextButton(onClick = { open = false }) { Text(\"Xác nhận\") }\n    }\n)"),
    SNACKBAR("Snackbar", "Feedback", "Thông báo ngắn có hành động phụ",
        "Snackbar hiển thị phản hồi tạm thời ở cuối màn hình. Hãy thử đánh dấu hoàn thành rồi nhấn Hoàn tác trong thông báo.",
        "val host = remember { SnackbarHostState() }\nval scope = rememberCoroutineScope()\nButton(onClick = {\n    scope.launch { host.showSnackbar(\"Đã lưu\", \"Hoàn tác\") }\n}) { Text(\"Lưu\") }\n// Đặt SnackbarHost(host) trong Scaffold."),
    FAB("FloatingActionButton", "Selection", "Nút nổi cho hành động chính",
        "FloatingActionButton nhấn mạnh một hành động quan trọng, thường là thêm mới. Nhấn dấu cộng để thêm mục minh họa.",
        "FloatingActionButton(onClick = { count++ }) {\n    Icon(Icons.Default.Add, contentDescription = \"Thêm mục\")\n}");
}

val componentGroups = listOf("Display", "Input", "Selection", "Navigation", "Content", "Layout", "Feedback")

fun filteredComponents(query: String): List<UiComponent> {
    val normalized = normalize(query.trim())
    return UiComponent.entries.filter {
        normalize("${it.title} ${it.group} ${it.summary}").contains(normalized)
    }
}

private fun normalize(value: String): String = java.text.Normalizer
    .normalize(value.lowercase(java.util.Locale.ROOT), java.text.Normalizer.Form.NFD)
    .replace("\\p{M}+".toRegex(), "").replace('đ', 'd')
