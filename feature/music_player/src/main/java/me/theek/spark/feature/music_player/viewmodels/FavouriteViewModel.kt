package me.theek.spark.feature.music_player.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.util.UiState
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(songRepository: SongRepository): ViewModel() {

    val uiState: StateFlow<UiState<List<Song>>> = songRepository.getFavouriteSongs()
        .map {
            UiState.Success(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
}