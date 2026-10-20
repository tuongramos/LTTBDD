@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@Composable
fun ComponentDemo(component: UiComponent, snackbar: SnackbarHostState) {
    when (component) {
        UiComponent.TEXT -> TextDemo()
        UiComponent.IMAGE -> ImageDemo()
        UiComponent.ICON -> IconDemo()
        UiComponent.TEXT_FIELD -> TextFieldDemo()
        UiComponent.PASSWORD -> PasswordDemo()
        UiComponent.TOGGLE -> ToggleDemo()
        UiComponent.SLIDER -> SliderDemo()
        UiComponent.DATE_PICKER -> DateDemo()
        UiComponent.BUTTON -> ButtonDemo()
        UiComponent.CHECKBOX -> CheckboxDemo()
        UiComponent.RADIO -> RadioDemo()
        UiComponent.PICKER -> PickerDemo()
        UiComponent.CHIP -> ChipDemo()
        UiComponent.NAVIGATION -> NavigationDemo()
        UiComponent.TAB_BAR -> TabDemo()
        UiComponent.MENU -> MenuDemo()
        UiComponent.LIST -> ListDemo()
        UiComponent.CARD -> CardDemo()
        UiComponent.DIVIDER -> DividerDemo()
        UiComponent.COLUMN -> ColumnDemo()
        UiComponent.ROW -> RowDemo()
        UiComponent.BOX -> BoxDemo()
        UiComponent.PROGRESS -> ProgressDemo()
        UiComponent.DIALOG -> DialogDemo()
        UiComponent.SNACKBAR -> SnackbarDemo(snackbar)
        UiComponent.FAB -> FabDemo()
    }
}

@Composable
private fun DemoStack(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp), content = content)
}

@Composable
private fun ResultText(text: String) {
    Text(text, color = Accent, fontSize = 14.sp, lineHeight = 21.sp, textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().testTag("demo_result"))
}

@Composable
private fun TextDemo() {
    Text(buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
        append(" ")
        withStyle(SpanStyle(color = Color(0xFFB17B20), fontSize = 38.sp)) { append("Brown") }
        append("\nfox ")
        withStyle(SpanStyle(letterSpacing = 5.sp)) { append("jumps") }
        append(" ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) { append("over") }
        append("\n")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("the") }
        append(" ")
        withStyle(SpanStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic)) { append("lazy") }
        append(" dog.")
    }, fontSize = 29.sp, lineHeight = 49.sp, color = Color(0xFF16191D), modifier = Modifier.fillMaxWidth())
}

@Composable
private fun ImageDemo() {
    DemoStack {
        Image(painterResource(R.drawable.campus), "Hình minh họa khuôn viên trường",
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().aspectRatio(1.72f).clip(RoundedCornerShape(14.dp)))
        Text("Khuôn viên trường · drawable/campus.xml", color = Muted, fontSize = 12.sp)
        Image(painterResource(R.drawable.landscape), "Núi, hồ và mặt trời",
            contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth().aspectRatio(1.72f).clip(RoundedCornerShape(14.dp)))
        Text("Ảnh trong app · sử dụng được offline", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun IconDemo() {
    var favorite by rememberSaveable { mutableStateOf(false) }
    DemoStack {
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Home, "Trang chủ", tint = Accent, modifier = Modifier.size(34.dp))
            IconToggleButton(checked = favorite, onCheckedChange = { favorite = it }) {
                Icon(Icons.Default.Favorite, "Yêu thích", tint = if (favorite) Color(0xFFE16378) else Muted, modifier = Modifier.size(34.dp))
            }
            Icon(Icons.Default.Info, "Thông tin", tint = Color(0xFF44A385), modifier = Modifier.size(34.dp))
        }
        ResultText(if (favorite) "Đã thêm vào yêu thích" else "Chạm vào trái tim để yêu thích")
    }
}

@Composable
private fun TextFieldDemo() {
    var text by rememberSaveable { mutableStateOf("") }
    DemoStack {
        OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Thông tin nhập") },
            singleLine = true, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().testTag("demo_input"))
        Text(if (text.isEmpty()) "Tự động cập nhật dữ liệu theo TextField" else "Bạn vừa nhập: $text",
            color = Color(0xFFB15153), fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.testTag("live_text"))
    }
}

@Composable
private fun PasswordDemo() {
    // Deliberately not saveable: sample passwords should not survive leaving this screen.
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    DemoStack {
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mật khẩu") },
            singleLine = true, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().testTag("password_input"),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = { TextButton(onClick = { visible = !visible }) { Text(if (visible) "Ẩn" else "Hiện") } })
        ResultText(if (password.isEmpty()) "Nhập mật khẩu để thử chế độ che ký tự" else "Đã nhập ${password.length} ký tự")
    }
}

@Composable
private fun ToggleDemo() {
    var enabled by rememberSaveable { mutableStateOf(true) }
    DemoStack {
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFF1F6FA))
            .toggleable(value = enabled, role = Role.Switch, onValueChange = { enabled = it }).padding(18.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Thông báo", fontWeight = FontWeight.SemiBold)
                Text("Nhận nhắc nhở học tập", color = Muted, fontSize = 12.sp)
            }
            Switch(checked = enabled, onCheckedChange = null)
        }
        ResultText(if (enabled) "Thông báo đang bật" else "Thông báo đã tắt")
    }
}

@Composable
private fun SliderDemo() {
    var value by rememberSaveable { mutableFloatStateOf(50f) }
    DemoStack {
        Text("${value.roundToInt()}%", fontSize = 44.sp, fontWeight = FontWeight.Light, color = Accent)
        Slider(value = value, onValueChange = { value = it }, valueRange = 0f..100f, modifier = Modifier.testTag("demo_slider"))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("0%", color = Muted); Text("Âm lượng", color = Muted); Text("100%", color = Muted)
        }
    }
}

@Composable
private fun DateDemo() {
    var open by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf<Long?>(null) }
    DemoStack {
        Text("Lịch học của bạn", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Button(onClick = { open = true }) { Text("Chọn ngày") }
        ResultText(selected?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN")).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date(it))
        } ?: "Chưa chọn ngày")
    }
    if (open) {
        val state = rememberDatePickerState(initialSelectedDateMillis = selected)
        DatePickerDialog(onDismissRequest = { open = false },
            confirmButton = {
                TextButton(enabled = state.selectedDateMillis != null, onClick = {
                    selected = state.selectedDateMillis; open = false
                }) { Text("Chọn") }
            }, dismissButton = { TextButton(onClick = { open = false }) { Text("Hủy") } }) {
            DatePicker(state = state, title = { Text("Chọn ngày học", Modifier.padding(24.dp)) })
        }
    }
}

@Composable
private fun ButtonDemo() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    DemoStack {
        Button(onClick = { count++ }, modifier = Modifier.testTag("counter_button")) { Text("Nhấn thử") }
        OutlinedButton(onClick = { count = 0 }) { Text("Đặt lại") }
        TextButton(onClick = { count += 5 }) { Text("Cộng thêm 5") }
        ResultText("Đã nhấn: $count")
    }
}

@Composable
private fun CheckboxDemo() {
    var kotlin by rememberSaveable { mutableStateOf(true) }
    var compose by rememberSaveable { mutableStateOf(false) }
    var android by rememberSaveable { mutableStateOf(false) }
    DemoStack {
        Column {
            CheckRow("Kotlin", kotlin) { kotlin = it }
            CheckRow("Jetpack Compose", compose) { compose = it }
            CheckRow("Android", android) { android = it }
        }
        ResultText("Đã chọn ${listOf(kotlin, compose, android).count { it }}/3 chủ đề")
    }
}

@Composable
private fun CheckRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 52.dp).toggleable(checked, role = Role.Checkbox, onValueChange = onChange)
        .padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange = null)
        Text(title, Modifier.padding(start = 14.dp))
    }
}

@Composable
private fun RadioDemo() {
    var selected by rememberSaveable { mutableStateOf("Kotlin") }
    DemoStack {
        Column(Modifier.selectableGroup()) {
            listOf("Kotlin", "Java", "Dart").forEach { option ->
                Row(Modifier.fillMaxWidth().heightIn(min = 52.dp)
                    .selectable(selected == option, role = Role.RadioButton, onClick = { selected = option })
                    .padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected == option, onClick = null)
                    Text(option, Modifier.padding(start = 14.dp))
                }
            }
        }
        ResultText("Ngôn ngữ đã chọn: $selected")
    }
}

@Composable
private fun PickerDemo() {
    var open by remember { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf("Năm 1") }
    DemoStack {
        Text("Bạn là sinh viên năm mấy?", fontWeight = FontWeight.SemiBold)
        Box {
            OutlinedButton(onClick = { open = true }) { Text("$selected  ▾") }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                listOf("Năm 1", "Năm 2", "Năm 3", "Năm 4").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { selected = option; open = false })
                }
            }
        }
        ResultText("Đã chọn: $selected")
    }
}

@Composable
private fun ChipDemo() {
    var kotlin by rememberSaveable { mutableStateOf(true) }
    var compose by rememberSaveable { mutableStateOf(false) }
    var design by rememberSaveable { mutableStateOf(false) }
    DemoStack {
        // Wrapping one chip per row keeps the demo usable with large accessibility fonts.
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilterChip(kotlin, onClick = { kotlin = !kotlin }, label = { Text("Kotlin") })
            FilterChip(compose, onClick = { compose = !compose }, label = { Text("Compose") })
            FilterChip(design, onClick = { design = !design }, label = { Text("UI Design") })
        }
        val selected = listOfNotNull("Kotlin".takeIf { kotlin }, "Compose".takeIf { compose }, "UI Design".takeIf { design })
        ResultText(if (selected.isEmpty()) "Chưa chọn chủ đề" else selected.joinToString(" · "))
    }
}

@Composable
private fun NavigationDemo() {
    var detail by rememberSaveable { mutableStateOf(false) }
    DemoStack {
        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF1F6FA)) {
            Column {
                TopAppBar(title = { Text(if (detail) "Bài học" else "Thư viện", fontSize = 18.sp) },
                    navigationIcon = {
                        if (detail) IconButton(onClick = { detail = false }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại ví dụ")
                        }
                    }, windowInsets = WindowInsets(0, 0, 0, 0),
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = TileBlue))
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (detail) "Nội dung bài học Compose" else "Mô phỏng điều hướng trong app", textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { detail = !detail }) { Text(if (detail) "Về thư viện" else "Mở bài học") }
                }
            }
        }
        ResultText(if (detail) "Thư viện → Bài học" else "Đang ở Thư viện")
    }
}

@Composable
private fun TabDemo() {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val titles = listOf("Học tập", "Yêu thích", "Hồ sơ")
    DemoStack {
        TabRow(selectedTabIndex = tab) {
            titles.forEachIndexed { index, title ->
                Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title, fontSize = 12.sp) })
            }
        }
        Icon(when (tab) { 0 -> Icons.Default.Home; 1 -> Icons.Default.Favorite; else -> Icons.Default.Person },
            null, tint = Accent, modifier = Modifier.size(48.dp))
        Text(titles[tab], fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        ResultText(listOf("Khám phá 26 thành phần UI", "Các bài học bạn đã lưu", "Xin chào, sinh viên UTH!")[tab])
    }
}

@Composable
private fun MenuDemo() {
    var open by remember { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf("Chưa chọn hành động") }
    DemoStack {
        Box {
            OutlinedButton(onClick = { open = true }) {
                Text("Mở menu"); Icon(Icons.Default.MoreVert, null, Modifier.padding(start = 8.dp))
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                listOf("Sao chép", "Lưu mục", "Chia sẻ").forEach { action ->
                    DropdownMenuItem(text = { Text(action) }, onClick = {
                        result = "Bạn đã chọn: $action (minh họa)"; open = false
                    })
                }
            }
        }
        ResultText(result)
    }
}

@Composable
private fun ListDemo() {
    var selected by rememberSaveable { mutableStateOf("Chưa chọn bài học") }
    DemoStack {
        LazyColumn(Modifier.fillMaxWidth().height(248.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF1F6FA))) {
            items((1..12).toList(), key = { it }) { index ->
                Surface(onClick = { selected = "Đã chọn bài học $index" }, color = Color.Transparent) {
                    ListItem(headlineContent = { Text("Bài học $index") },
                        supportingContent = { Text("Khám phá Jetpack Compose") },
                        leadingContent = { Text(index.toString().padStart(2, '0'), color = Accent, fontWeight = FontWeight.Bold) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }
                if (index < 12) HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            }
        }
        ResultText(selected)
    }
}

@Composable
private fun CardDemo() {
    var saved by rememberSaveable { mutableStateOf(false) }
    ElevatedCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Image(painterResource(R.drawable.landscape), "Phong cảnh núi và hồ", contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(140.dp))
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Khám phá Compose", fontWeight = FontWeight.Bold, fontSize = 21.sp)
            Text("Xây dựng giao diện Android từ những thành phần nhỏ.", color = Muted, lineHeight = 22.sp)
            TextButton(onClick = { saved = !saved }) {
                Icon(if (saved) Icons.Default.Check else Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text(if (saved) "Đã lưu bài học" else "Lưu bài học")
            }
        }
    }
}

@Composable
private fun DividerDemo() {
    DemoStack {
        Text("Thông tin cá nhân", fontWeight = FontWeight.SemiBold)
        HorizontalDivider()
        Text("Cài đặt tài khoản", color = Muted)
        HorizontalDivider(thickness = 2.dp, color = TileBlue)
        Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Trái", color = Accent)
            VerticalDivider()
            Text("Phải", color = Accent)
        }
    }
}

private val blockColors = listOf(Color(0xFFA6C1F1), Color(0xFF5389E9), Color(0xFFA6C1F1))

@Composable
private fun ColumnDemo() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Column(Modifier.width(144.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF1F4F8)).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(3) { index ->
                Box(Modifier.fillMaxWidth().height(62.dp).clip(RoundedCornerShape(12.dp)).background(blockColors[index]), contentAlignment = Alignment.Center) {
                    Text("${index + 1}", color = Ink, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun RowDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFF1F4F8)).padding(9.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { index ->
                    Box(Modifier.weight(1f).height(58.dp).clip(RoundedCornerShape(11.dp)).background(blockColors[index]))
                }
            }
        }
    }
}

@Composable
private fun BoxDemo() {
    Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(18.dp))) {
        Image(painterResource(R.drawable.landscape), "Nền phong cảnh", modifier = Modifier.matchParentSize(), contentScale = ContentScale.Crop)
        Surface(color = Color.White.copy(alpha = .9f), shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.TopEnd).padding(14.dp)) {
            Text("TOP END", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Accent, modifier = Modifier.padding(8.dp))
        }
        Text("Một khung, nhiều lớp", color = Ink, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().background(Color.White.copy(alpha = .85f)).padding(18.dp))
    }
}

@Composable
private fun ProgressDemo() {
    var progress by rememberSaveable { mutableFloatStateOf(.65f) }
    DemoStack {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(96.dp), strokeWidth = 7.dp)
            Text("${(progress * 100).roundToInt()}%", color = Accent, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        Slider(value = progress, onValueChange = { progress = it })
        ResultText(if (progress >= 1f) "Đã hoàn thành!" else "Kéo thanh trượt để mô phỏng tiến độ")
    }
}

@Composable
private fun DialogDemo() {
    var open by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf("Chưa có phản hồi") }
    DemoStack {
        Button(onClick = { open = true }) { Text("Mở hộp thoại") }
        ResultText(result)
    }
    if (open) AlertDialog(onDismissRequest = { open = false; result = "Đã đóng hộp thoại" },
        icon = { Icon(Icons.Default.Check, null) },
        title = { Text("Hoàn thành bài học?") },
        text = { Text("Bạn có muốn đánh dấu bài học này là đã hoàn thành không?") },
        confirmButton = { TextButton(onClick = { result = "Đã hoàn thành bài học"; open = false }) { Text("Xác nhận") } },
        dismissButton = { TextButton(onClick = { result = "Đã hủy thao tác"; open = false }) { Text("Hủy") } })
}

@Composable
private fun SnackbarDemo(host: SnackbarHostState) {
    var saved by rememberSaveable { mutableStateOf(false) }
    var pending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    DemoStack {
        Button(enabled = !pending, onClick = {
            saved = true
            pending = true
            scope.launch {
                try {
                    val result = host.showSnackbar("Đã đánh dấu hoàn thành", "Hoàn tác", withDismissAction = true, duration = SnackbarDuration.Long)
                    if (result == SnackbarResult.ActionPerformed) saved = false
                } finally { pending = false }
            }
        }) { Text("Đánh dấu hoàn thành") }
        ResultText(if (saved) "Trạng thái: Đã hoàn thành" else "Trạng thái: Chưa hoàn thành")
    }
}

@Composable
private fun FabDemo() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    DemoStack {
        FloatingActionButton(onClick = { count++ }, containerColor = ActionBlue, contentColor = Color.White) {
            Icon(Icons.Default.Add, "Thêm mục")
        }
        ResultText("Đã thêm $count mục")
        TextButton(onClick = { count = 0 }) { Text("Đặt lại") }
    }
}
