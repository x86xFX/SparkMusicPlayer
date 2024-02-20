package me.theek.spark.feature.music_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.data.repository.SongStreamState
import me.theek.spark.core.model.data.Song
import javax.inject.Inject

@HiltViewModel
class MusicListScreenViewModel @Inject constructor(private val songRepository: SongRepository) : ViewModel() {

    val uiState: StateFlow<Any> = songRepository.getSongs()
        .map { songStreamState ->
            when (songStreamState) {
                is SongStreamState.Failure -> {
                    UiState.Failure(message = songStreamState.message)
                }
                is SongStreamState.Progress -> {
                    UiState.Loading
                }
                is SongStreamState.Success -> {
                    UiState.Success(songStreamState.songs)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun getSongCoverArt(songPath: String) : ByteArray? {
        return songRepository.getSongCoverArt(songPath)
    }
}

sealed interface UiState {
    data object Loading : UiState
    data class Failure(val message: String?) : UiState
    data class Success(val songs: List<Song>) : UiState
}