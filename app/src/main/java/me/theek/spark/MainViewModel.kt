package me.theek.spark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.theek.spark.core.datastore.UserPreferencesDatasource
import me.theek.spark.core.model.data.UserData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userPreferencesDatasource: UserPreferencesDatasource) :
    ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userPreferencesDatasource.userData.map { userData ->
            MainActivityUiState.Success(userData)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState.Loading
        )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}