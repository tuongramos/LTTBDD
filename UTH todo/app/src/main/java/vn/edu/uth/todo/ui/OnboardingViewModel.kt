package vn.edu.uth.todo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import vn.edu.uth.todo.data.FigmaOnboardingRepository
import vn.edu.uth.todo.data.OnboardingRepository
import vn.edu.uth.todo.model.AppScreen
import vn.edu.uth.todo.model.OnboardingNavigator
import vn.edu.uth.todo.model.OnboardingUiState

class OnboardingViewModel(private val savedState: SavedStateHandle) : ViewModel() {
    private val repository: OnboardingRepository = FigmaOnboardingRepository()
    val content = repository.getContent()
    private val navigator = OnboardingNavigator(content.pages.size)

    var uiState by mutableStateOf(
        OnboardingUiState(
            screen = savedState.get<Int>(PAGE)?.let(navigator::open) ?: AppScreen.Start,
            completed = savedState[COMPLETED] ?: false
        )
    )
        private set

    fun next() = navigate(navigator.next(uiState.screen))
    fun previous() = navigate(navigator.previous(uiState.screen))
    fun openPage(index: Int) = navigate(navigator.open(index))
    fun skip() = navigate(navigator.skip())

    fun complete() {
        if (navigator.isLast(uiState.screen)) {
            uiState = uiState.copy(completed = true)
            savedState[COMPLETED] = true
        }
    }

    private fun navigate(screen: AppScreen) {
        uiState = uiState.copy(screen = screen)
        savedState[PAGE] = (screen as? AppScreen.Onboarding)?.pageIndex
    }

    private companion object {
        const val PAGE = "onboarding_page"
        const val COMPLETED = "onboarding_completed"
    }
}
