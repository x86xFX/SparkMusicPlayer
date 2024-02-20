package me.theek.spark.feature.music_player

import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.model.data.Song
import javax.inject.Inject

@HiltViewModel
class MusicListScreenViewModel @Inject constructor(private val songRepository: SongRepository) : ViewModel() {

    val uiState: StateFlow<UiState> = songRepository.getSongs()
        .map { state ->
            when (state) {
                is FlowEvent.Progress -> {
                    UiState.Progress(
                        progress = state.asFloat(),
                        status = state.message
                    )
                }
                is FlowEvent.Success -> {
                    UiState.Success(state.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
    var currentPlayingSong by mutableStateOf<Song?>(null)
        private set
    var currentPlayingSongCover by mutableStateOf<ByteArray?>(null)
        private set
    var currentPlayingSongPalette by mutableStateOf<Palette?>(null)
        private set


    suspend fun getSongCoverArt(songPath: String) : ByteArray? {
        return songRepository.getSongCoverArt(songPath)
    }

    fun onSongClick(song: Song) {
        currentPlayingSong = song
        getCurrentPlayingSongCoverArt(song.path)
    }

    private fun getCurrentPlayingSongCoverArt(songPath: String) {
        viewModelScope.launch {
            currentPlayingSongCover = songRepository.getSongCoverArt(songPath)
            currentPlayingSongColorPalette(coverArtData = currentPlayingSongCover)
        }
    }

    private suspend fun currentPlayingSongColorPalette(coverArtData: ByteArray?) = withContext(Dispatchers.IO) {
        if (coverArtData != null) {
            val bitmap = BitmapFactory.decodeByteArray(coverArtData, 0, coverArtData.size)
            currentPlayingSongPalette = Palette.from(bitmap).generate()
        }
    }
}