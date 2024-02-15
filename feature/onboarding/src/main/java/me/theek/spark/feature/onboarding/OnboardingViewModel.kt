package me.theek.spark.feature.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.data.repository.SongStreamState
import me.theek.spark.core.data.repository.UserDataRepository
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

    var shouldShowScanner by mutableStateOf(false)
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
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.getSongs().collect { songStreamState ->
                when (songStreamState) {
                    is SongStreamState.Failure -> {
                        _uiState.value = UiState.Failure(songStreamState.message)
                    }

                    is SongStreamState.Progress -> {
                        shouldShowScanner = true
                        _uiState.value = UiState.Progress(
                            hint = songStreamState.retrieveHint,
                            progress = songStreamState.progress
                        )
                    }

                    is SongStreamState.Success -> {
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
        val hint: String,
        val progress: Float
    ) : UiState
}