package me.theek.spark.feature.music_player.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.util.UiState
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(private val playlistRepository: PlaylistRepository) : ViewModel() {

    val uiState: StateFlow<UiState<List<PlaylistData>>> = playlistRepository.getAllPlayLists()
        .map { playlists ->
            playlists.distinctBy { it.playlistId } // Filter out duplicates based on playlistId
        }.map {
            UiState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
    private var currentQueuedSong by mutableStateOf<Song?>(null)
    var newPlaylistName by mutableStateOf("")
        private set
    var shouldOpenCreatePlaylistDialog by mutableStateOf(false)
        private set
    var isInSelectionMode by mutableStateOf(false)
        private set
    var shouldShowPlaylistDeletionWarning by mutableStateOf(false)
        private set
    private var selectedPlaylists = mutableStateListOf<Long>()

    fun addToQueuedPlaylistSong(song: Song) {
        currentQueuedSong = song
        shouldOpenCreatePlaylistDialog = true
    }

    fun onCreatePlaylistDismiss() {
        shouldOpenCreatePlaylistDialog = false
    }

    fun onNewPlaylistNameChange(value: String) {
        newPlaylistName = value
    }

    fun onAddToExistingPlaylistClick(data: Pair<Long, Long>) {
        viewModelScope.launch {
            playlistRepository.addSongToExistingPlaylist(
                playListId = data.first,
                songId = data.second
            )
        }
    }


    fun onPlaylistSave() {
        if (newPlaylistName.isNotBlank() && currentQueuedSong != null) {
            viewModelScope.launch {
                playlistRepository
                    .createPlaylistWithSong(
                        song = currentQueuedSong!!,
                        playListName =  newPlaylistName
                    )
                shouldOpenCreatePlaylistDialog = false
                newPlaylistName = ""
                currentQueuedSong = null
            }
        }
    }

    fun onChangingToSelectionMode(playlistId: Long) {
        selectedPlaylists.add(playlistId)
        isInSelectionMode = true
    }

    fun onPlaylistAddToSelection(playlistId: Long) {
        selectedPlaylists.add(playlistId)
    }

    fun onPlaylistRemoveFromSelection(playlistId: Long) {
        selectedPlaylists.remove(playlistId)
    }

    fun onPlaylistSelectionClearClick() {
        isInSelectionMode = false
        selectedPlaylists.clear()
    }

    fun onPlaylistDeleteWarningShow() { shouldShowPlaylistDeletionWarning = true }

    fun onPlaylistDeleteWarningDismiss() { shouldShowPlaylistDeletionWarning = false }

    fun onDeletePlaylists() {
        viewModelScope.launch {
            playlistRepository.deletePlaylists(selectedPlaylists)
            isInSelectionMode = false
            onPlaylistDeleteWarningDismiss()
        }
    }
}