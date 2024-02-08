package me.theek.spark.feature.music_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.theek.spark.core.content_reader.ContentResolverHelper
import me.theek.spark.core.model.data.Audio
import javax.inject.Inject

@HiltViewModel
class MusicListScreenViewModel @Inject constructor(contentResolverHelper: ContentResolverHelper) :
    ViewModel() {

    val uiState: StateFlow<UiState> = contentResolverHelper.getAudioData().map { audios ->
        UiState.Success(audios)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val audios: List<Audio>) : UiState
}