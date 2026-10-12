package vn.edu.uth.thuchanh03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.MathContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                CalculatorScreen()
            }
        }
    }
}

@Composable
private fun CalculatorScreen() {
    // Lưu hai số và phép toán, giữ lại khi xoay màn hình.
    var first by rememberSaveable { mutableStateOf("") }
    var second by rememberSaveable { mutableStateOf("") }
    var operation by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val secondFieldFocus = remember { FocusRequester() }
    val result = calculate(first, second, operation)
    val colors = listOf(Color(0xFFCE4944), Color(0xFFDDAE4D), Color(0xFF6039DA), Color(0xFF202020))
    val names = listOf("Cộng", "Trừ", "Nhân", "Chia")

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
            .safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Khoảng cách cố định để màn hình không nhảy khi bàn phím xuất hiện.
        Spacer(Modifier.height(140.dp))
        Text("Thực hành 03", fontSize = 18.sp, color = Color.Black)
        Spacer(Modifier.height(64.dp))
        NumberField(first, { first = it }, "Số thứ nhất", ImeAction.Next) {
            secondFieldFocus.requestFocus()
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("+", "-", "*", "/").forEachIndexed { index, symbol ->
                Button(
                    onClick = {
                        operation = symbol
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.size(52.dp).semantics { contentDescription = names[index] },
                    shape = RoundedCornerShape(8.dp),
                    border = if (operation == symbol) BorderStroke(3.dp, Color.Black) else null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors[index], contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) { Text(symbol, fontSize = 22.sp) }
            }
        }
        Spacer(Modifier.height(24.dp))
        NumberField(second, { second = it }, "Số thứ hai", ImeAction.Done,
            Modifier.focusRequester(secondFieldFocus)) {
            focusManager.clearFocus()
        }
        Spacer(Modifier.height(12.dp))
        Text("Kết quả: $result", Modifier.fillMaxWidth(), color = Color.Black, fontSize = 18.sp)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    onKeyboardAction: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 32) onValueChange(it) },
        modifier = modifier.fillMaxWidth().semantics { contentDescription = hint },
        placeholder = { Text(hint) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { onKeyboardAction() }, onDone = { onKeyboardAction() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFBDBDBD),
            focusedBorderColor = Color(0xFF6039DA),
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black
        )
    )
}

// Tính lại ngay khi một ô nhập hoặc phép toán thay đổi.
private fun calculate(first: String, second: String, operation: String): String {
    if (operation.isEmpty()) return ""
    if (first.isBlank() || second.isBlank()) return "Vui lòng nhập đủ hai số"
    val numberPattern = Regex("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)")
    val firstText = first.trim().replace(',', '.')
    val secondText = second.trim().replace(',', '.')
    if (!numberPattern.matches(firstText) || !numberPattern.matches(secondText)) {
        return "Vui lòng nhập số hợp lệ"
    }
    val a = firstText.toBigDecimalOrNull() ?: return "Số thứ nhất không hợp lệ"
    val b = secondText.toBigDecimalOrNull() ?: return "Số thứ hai không hợp lệ"
    if (operation == "/" && b.signum() == 0) return "Không thể chia cho 0"
    val result = when (operation) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> a.divide(b, MathContext.DECIMAL64)
        else -> return ""
    }
    return result.stripTrailingZeros().toPlainString()
}
