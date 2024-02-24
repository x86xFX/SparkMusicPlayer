package me.theek.spark.feature.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.data.repository.UserDataRepository
import me.theek.spark.core.model.data.FlowEvent
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val songRepository: SongRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var shouldShowPermissionAlert by mutableStateOf(false)
        private set

    var shouldNavigateToHome by mutableStateOf(false)
        private set

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted.not()) {
            shouldShowPermissionAlert = true
        } else {
            onImportSongs()
        }
    }

    fun onDismissPermissionAlert() { shouldShowPermissionAlert = false }

    private fun onImportSongs() {
        viewModelScope.launch {
            songRepository.getSongs().collect { state ->
                when (state) {
                    is FlowEvent.Progress -> {
                        _uiState.value = UiState.Progress(
                            message = state.message,
                            progress = state.asFloat()
                        )
                    }
                    is FlowEvent.Success -> {
                        onHideOnboardingScreen()
                        onNavigateToHomeScreen()
                    }
                }
            }
        }
    }

    private fun onHideOnboardingScreen() {
        viewModelScope.launch {
            userDataRepository.setShouldHideOnboarding(shouldHideOnboarding = true)
        }
    }

    private fun onNavigateToHomeScreen() {
        shouldNavigateToHome = true
    }
}


sealed interface UiState {
    data object Idle : UiState
    data class Failure(val message: String?) : UiState
    data class Progress(
        val message: String?,
        val progress: Float
    ) : UiState
}