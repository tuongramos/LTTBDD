package vn.edu.uth.todo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import vn.edu.uth.todo.model.AppScreen
import vn.edu.uth.todo.ui.OnboardingScreen
import vn.edu.uth.todo.ui.OnboardingViewModel
import vn.edu.uth.todo.ui.StartScreen
import vn.edu.uth.todo.ui.TodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()
        setContent {
            TodoTheme {
                TodoApp(onFinished = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    private fun hideSystemBars() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

@Composable
fun TodoApp(
    onFinished: (String) -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val state = viewModel.uiState
    LaunchedEffect(state.screen) {
        if (state.screen == AppScreen.Start) {
            delay(2_400)
            viewModel.next()
        }
    }
    BackHandler(enabled = state.screen is AppScreen.Onboarding) { viewModel.previous() }
    when (val screen = state.screen) {
        AppScreen.Start -> StartScreen(viewModel.content.brand, viewModel::next)
        is AppScreen.Onboarding -> OnboardingScreen(
            content = viewModel.content,
            pageIndex = screen.pageIndex,
            onNext = viewModel::next,
            onSkip = {
                if (screen.pageIndex == viewModel.content.pages.lastIndex) {
                    viewModel.complete()
                    onFinished(viewModel.content.labels.completed)
                } else {
                    viewModel.skip()
                }
            },
            onPageSelected = viewModel::openPage,
            onComplete = {
                viewModel.complete()
                onFinished(viewModel.content.labels.completed)
            }
        )
    }
}
