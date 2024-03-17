package me.theek.spark.feature.music_player.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.theek.spark.core.data.repository.PlaylistRepository
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.feature.music_player.util.UiState
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(private val playlistRepository: PlaylistRepository) : ViewModel() {

    val uiState: StateFlow<UiState<List<Playlist>>> = playlistRepository.getPlayLists().map { UiState.Success(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
    var newPlaylistName by mutableStateOf("")
        private set
    var shouldOpenCreatePlaylistDialog by mutableStateOf(false)
        private set

    fun onCreatePlaylistAlertOpen() { shouldOpenCreatePlaylistDialog = true }
    fun onCreatePlaylistDismiss() { shouldOpenCreatePlaylistDialog = false }
    fun onNewPlaylistNameChange(value: String) {
        newPlaylistName = value
    }

    fun onPlaylistSave() {
        if (newPlaylistName.isNotBlank()) {
            viewModelScope.launch {
                playlistRepository.createPlayList(
                    Playlist(
                        id = 0,
                        name = newPlaylistName,
                        createdAt = 0
                    )
                )
                shouldOpenCreatePlaylistDialog = false
                newPlaylistName = ""
            }
        }
    }
}