package me.theek.spark.feature.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.theek.spark.core.datastore.UserPreferencesDatasource
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userPreferencesDatasource: UserPreferencesDatasource) : ViewModel() {

    var shouldShowPermissionAlert by mutableStateOf(false)
        private set

    var shouldNavigateToHome by mutableStateOf(false)
        private set

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted.not()) {
            shouldShowPermissionAlert = true
        } else {
            onHideOnboardingScreen()
        }
    }

    fun onDismissPermissionAlert() { shouldShowPermissionAlert = false }

    private fun onHideOnboardingScreen() {
        viewModelScope.launch {
            userPreferencesDatasource.setShouldHideOnboarding(shouldHideOnboarding = true)
            shouldNavigateToHome = true
        }
    }
}