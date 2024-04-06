package me.theek.spark.feature.music_player.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.theek.spark.core.data.repository.PlaylistRepository
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.util.UiState

@HiltViewModel(assistedFactory = PlaylistPlayerViewModel.PlaylistPlayerViewModelFactory::class)
class PlaylistPlayerViewModel @AssistedInject constructor(
    @Assisted playlistId: Long,
    playlistRepository: PlaylistRepository,
) : ViewModel() {
    @AssistedFactory
    interface PlaylistPlayerViewModelFactory {
        fun create(playlistId: Long) : PlaylistPlayerViewModel
    }

    val uiState: StateFlow<UiState<PlaylistWithSongs>> = playlistRepository.getSelectedPlaylistSongs(playlistId)
        .map {
            delay(1000)
            val songs = mutableListOf<Song>()
            var playlistName = ""
            var playlistCreatedAt = ""

            it.forEach { playlistWithSongs ->
                println(playlistWithSongs)
                songs.add(playlistWithSongs.song)
                playlistName = playlistWithSongs.playlistName
                playlistCreatedAt = playlistWithSongs.toPlaylistCreatedDate()
            }

            UiState.Success(
                PlaylistWithSongs(
                    playlistId = playlistId,
                    playlistName = playlistName,
                    songs = songs,
                    playlistCreatedAt = playlistCreatedAt
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
}

data class PlaylistWithSongs(
    val playlistId: Long,
    val playlistName: String,
    val playlistCreatedAt: String,
    val songs: List<Song>
)