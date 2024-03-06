package me.theek.spark.feature.music_player

import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.player.AudioService
import me.theek.spark.core.player.MusicPlayerState
import me.theek.spark.core.player.PlayerEvent
import javax.inject.Inject
import kotlin.math.floor

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class MusicListScreenViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val audioService: AudioService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var songList by mutableStateOf<List<Song>>(emptyList())
    var currentSelectedSong by mutableStateOf<Song?>(null)
        private set
    var currentSelectedSongCover by mutableStateOf<ByteArray?>(null)
        private set
    var currentSelectedSongPalette by mutableStateOf<Palette?>(null)
        private set
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
        private set
    var progress by savedStateHandle.saveable { mutableFloatStateOf(0f) }
        private set
    var processString by savedStateHandle.saveable { mutableStateOf("00:00") }
        private set
    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
        private set
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
                    songList = state.data
                    setMediaItems()
                    UiState.Success(state.data)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    init {
        viewModelScope.launch {
            audioService.musicPlayStateStream.collectLatest { musicPlayerState ->
                when (musicPlayerState) {
                    is MusicPlayerState.CurrentPlaying -> {
                        if (currentSelectedSong == null || currentSelectedSong != songList[musicPlayerState.mediaItemIndex]) {
                            getCurrentPlayingSongCoverArt(songList[musicPlayerState.mediaItemIndex].path)
                        }
                        currentSelectedSong = songList[musicPlayerState.mediaItemIndex]
                        isPlaying = true
                    }
                    is MusicPlayerState.Playing -> { isPlaying = musicPlayerState.isPlaying }
                    is MusicPlayerState.Progress -> { calculateProgress(musicPlayerState.progress) }
                    is MusicPlayerState.Buffering -> { calculateProgress(musicPlayerState.progress) }
                    MusicPlayerState.Initial -> Unit
                    is MusicPlayerState.Ready -> { duration = musicPlayerState.duration }
                }
            }
        }
    }

    fun onSongClick(songWithIndex: Pair<Int, Song>) {
        viewModelScope.launch {
            audioService.onPlayerEvent(PlayerEvent.SelectedSongChange(changedSongIndex = songWithIndex.first))
        }
    }

    fun onPausePlayClick() {
        viewModelScope.launch {
            audioService.onPlayerEvent(PlayerEvent.PlayPause)
            isPlaying = false
        }
    }

    fun onSkipNextClick() {
        viewModelScope.launch { audioService.onPlayerEvent(PlayerEvent.Forward) }
    }

    fun onSkipPreviousClick() {
        viewModelScope.launch { audioService.onPlayerEvent(PlayerEvent.Backward) }
    }

    fun onProgressChange(position: Float) {
        viewModelScope.launch {
            audioService.onPlayerEvent(PlayerEvent.SeekTo(position.toLong()))
        }
    }

    suspend fun getSongCoverArt(songPath: String): ByteArray? {
        return songRepository.getSongCoverArt(songPath)
    }

    private fun setMediaItems() {
        audioService.setMediaItemList(mediaItems = songList)
    }

    private fun calculateProgress(currentProgress: Long) {
        progress = if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f) else 0f
        processString = formatDuration(currentProgress)
    }

    private fun formatDuration(duration: Long) : String {
        val totalSecond = floor(duration / 1E3).toInt()
        val minutes = totalSecond / 60
        val remainingSeconds = totalSecond - (minutes * 60)
        return if (duration < 0) "--:--" else "%d:%02d".format(minutes, remainingSeconds)
    }

    private fun getCurrentPlayingSongCoverArt(songPath: String) {
        viewModelScope.launch {
            currentSelectedSongCover = songRepository.getSongCoverArt(songPath)
            currentPlayingSongColorPalette(coverArtData = currentSelectedSongCover)
        }
    }

    private suspend fun currentPlayingSongColorPalette(coverArtData: ByteArray?) =
        withContext(Dispatchers.IO) {
            if (coverArtData != null) {
                val bitmap = BitmapFactory.decodeByteArray(coverArtData, 0, coverArtData.size)
                currentSelectedSongPalette = Palette.from(bitmap).generate()
            }
        }

    override fun onCleared() {
        audioService.stopPlayer()
        super.onCleared()
    }
}