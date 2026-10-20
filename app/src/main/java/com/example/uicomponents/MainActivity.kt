package com.example.uicomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Accent = Color(0xFF087FAE)
val ActionBlue = Color(0xFF159EE8)
val TileBlue = Color(0xFFC6E5FA)
val Ink = Color(0xFF182C3C)
val Muted = Color(0xFF586B79)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ComponentsTheme { ComponentsApp() } }
    }
}

@Composable
fun ComponentsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Accent, onPrimary = Color.White,
            primaryContainer = TileBlue, onPrimaryContainer = Ink,
            secondary = Color(0xFF387F71),
            background = Color.White, surface = Color.White,
            onBackground = Ink, onSurface = Ink,
            surfaceVariant = Color(0xFFF1F6FA), onSurfaceVariant = Muted,
        ),
        content = content,
    )
}

/** Exactly three destinations. Saveable state restores the destination after recreation. */
@Composable
fun ComponentsApp() {
    var page by rememberSaveable { mutableStateOf("welcome") }
    var selectedId by rememberSaveable { mutableStateOf(UiComponent.TEXT.name) }
    val states = rememberSaveableStateHolder()
    val back: () -> Unit = { page = if (page == "detail") "catalog" else "welcome" }
    BackHandler(enabled = page != "welcome", onBack = back)

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().safeDrawingPadding().imePadding(), contentAlignment = Alignment.TopCenter) {
            Box(Modifier.widthIn(max = 620.dp).fillMaxSize()) {
                when (page) {
                    "welcome" -> WelcomeScreen { page = "catalog" }
                    "catalog" -> states.SaveableStateProvider("catalog") {
                        CatalogScreen(onBack = back, onSelect = {
                            selectedId = it.name
                            page = "detail"
                        })
                    }
                    "detail" -> states.SaveableStateProvider("detail-$selectedId") {
                        DetailScreen(UiComponent.valueOf(selectedId), onBack = back)
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeScreen(onReady: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 24.dp)) {
        LazyColumn(
            Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Image(painterResource(R.drawable.compose_logo), "Logo Jetpack Compose", Modifier.size(154.dp))
                Spacer(Modifier.height(52.dp))
                Text("Jetpack Compose", fontWeight = FontWeight.SemiBold, fontSize = 19.sp)
                Spacer(Modifier.height(18.dp))
                Text(
                    "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                    color = Muted, textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 21.sp,
                    modifier = Modifier.widthIn(max = 330.dp),
                )
            }
        }
        Button(
            onClick = onReady,
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).testTag("ready"),
            colors = ButtonDefaults.buttonColors(containerColor = ActionBlue),
            shape = RoundedCornerShape(28.dp),
        ) { Text("I’m ready", fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }
        Spacer(Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PageHeader(title: String, onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(title, color = Accent, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại", tint = Accent)
            }
        },
        windowInsets = WindowInsets(0, 0, 0, 0),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
    )
}

@Composable
private fun CatalogScreen(onBack: () -> Unit, onSelect: (UiComponent) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    val results = remember(query) { filteredComponents(query) }
    val listState = rememberLazyListState()
    Column(Modifier.fillMaxSize()) {
        PageHeader("UI Components List", onBack)
        OutlinedTextField(
            value = query, onValueChange = { query = it },
            placeholder = { Text("Tìm thành phần UI…", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = { query = "" }) {
                    Icon(Icons.Default.Close, "Xóa tìm kiếm")
                }
            },
            singleLine = true, shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).testTag("search"),
        )
        Text(
            "${results.size} thành phần · Chọn để khám phá",
            color = Muted, fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp).testTag("result_count"),
        )
        LazyColumn(
            state = listState, modifier = Modifier.weight(1f).testTag("catalog_list"),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (results.isEmpty()) item {
                Column(Modifier.fillMaxWidth().padding(vertical = 64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Search, null, tint = Muted, modifier = Modifier.size(36.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("Không tìm thấy thành phần", fontWeight = FontWeight.SemiBold)
                    Text("Thử tên khác như Text, Button hoặc Row.", color = Muted, fontSize = 13.sp)
                    TextButton(onClick = { query = "" }) { Text("Xóa tìm kiếm") }
                }
            }
            componentGroups.forEach { group ->
                val members = results.filter { it.group == group }
                if (members.isNotEmpty()) {
                    item(key = "group-$group") {
                        Text(group, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                            modifier = Modifier.padding(top = 10.dp, bottom = 2.dp))
                    }
                    items(members, key = { it.name }) { component ->
                        Card(
                            onClick = { onSelect(component) },
                            colors = CardDefaults.cardColors(containerColor = TileBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("component_${component.name}"),
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(component.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    Spacer(Modifier.height(3.dp))
                                    Text(component.summary, fontSize = 13.sp, color = Color(0xFF3D5669), lineHeight = 18.sp)
                                }
                                Text("›", color = Accent, fontSize = 26.sp, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailScreen(component: UiComponent, onBack: () -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    var showCode by rememberSaveable { mutableStateOf(false) }
    val title = when (component) {
        UiComponent.TEXT -> "Text Detail"
        UiComponent.IMAGE -> "Images"
        UiComponent.ROW -> "Row Layout"
        UiComponent.COLUMN -> "Column Layout"
        else -> component.title
    }
    Scaffold(
        topBar = { PageHeader(title, onBack) },
        snackbarHost = { SnackbarHost(snackbar) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).testTag("detail_list"),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(component.group.uppercase(), color = Accent, letterSpacing = 1.5.sp,
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("VÍ DỤ TRỰC QUAN", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
                }
            }
            item {
                Box(Modifier.fillMaxWidth().heightIn(min = 260.dp), contentAlignment = Alignment.Center) {
                    ComponentDemo(component, snackbar)
                }
            }
            item {
                HorizontalDivider(color = Color(0xFFE7EEF3))
                Spacer(Modifier.height(20.dp))
                Text(component.summary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                Text(component.explanation, color = Muted, fontSize = 14.sp, lineHeight = 23.sp)
            }
            item {
                OutlinedButton(onClick = { showCode = !showCode }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (showCode) "Ẩn mã Compose" else "Xem mã Compose")
                }
                if (showCode) {
                    Spacer(Modifier.height(12.dp))
                    Surface(color = Color(0xFFF2F6FA), shape = RoundedCornerShape(12.dp)) {
                        SelectionContainer {
                            Text(component.code, fontFamily = FontFamily.Monospace, fontSize = 12.sp,
                                lineHeight = 19.sp, modifier = Modifier.fillMaxWidth().padding(16.dp))
                        }
                    }
                    Text("Đoạn mã minh họa; một số biến và import được lược bỏ.", color = Muted,
                        fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun WelcomePreview() { ComponentsTheme { WelcomeScreen {} } }

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CatalogPreview() { ComponentsTheme { CatalogScreen({}, {}) } }

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TextPreview() { ComponentsTheme { DetailScreen(UiComponent.TEXT) {} } }
