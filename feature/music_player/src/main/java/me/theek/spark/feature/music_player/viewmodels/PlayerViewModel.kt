package me.theek.spark.feature.music_player.viewmodels

import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.Album
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.Response
import me.theek.spark.core.player.AudioService
import me.theek.spark.core.player.MusicPlayerState
import me.theek.spark.core.player.PlayerEvent
import me.theek.spark.core.player.QueueManager
import me.theek.spark.core.player.RepeatMode
import me.theek.spark.core.player.RepeatMode.Companion.REPEAT_MODE_ALL
import me.theek.spark.feature.music_player.util.UiState
import javax.inject.Inject
import kotlin.math.floor
import kotlin.random.Random

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val audioService: AudioService,
    private val queueManager: QueueManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var allSongList by mutableStateOf(listOf<Song>())
    private var currentQueuedSongList by mutableStateOf(listOf<Song>())
    var albumList by mutableStateOf(listOf<Album>())
    private val _artistDetailsStream = MutableStateFlow<List<ArtistDetails>>(emptyList())
    val artistDetailsStream = _artistDetailsStream.asStateFlow()
    var uiState by mutableStateOf<UiState<List<Song>>>(UiState.Loading)
        private set

    var currentSelectedSong by mutableStateOf<Song?>(null)
        private set
    var currentSelectedSongCover by mutableStateOf<ByteArray?>(null)
        private set
    var currentSelectedSongPalette by mutableStateOf<Palette?>(null)
        private set
    var isPlaying by savedStateHandle.saveable { mutableStateOf(true) }
        private set
    var isFavourite by savedStateHandle.saveable { mutableStateOf(false) }
        private set
    var progress by savedStateHandle.saveable { mutableFloatStateOf(0f) }
        private set
    var processString by savedStateHandle.saveable { mutableStateOf("00:00") }
        private set
    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
        private set
    var repeatMode by savedStateHandle.saveable { mutableIntStateOf(REPEAT_MODE_ALL) }
        private set
    var songInfo by mutableStateOf(SongInfo())
        private set

    init {
        viewModelScope.launch {
            audioService.musicPlayStateStream.collectLatest { musicPlayerState ->
                when (musicPlayerState) {
                    is MusicPlayerState.OnTrackChange -> {
                        getCurrentPlayingSongCoverArt(currentQueuedSongList[musicPlayerState.mediaItemIndex].path)
                        currentSelectedSong = currentQueuedSongList[musicPlayerState.mediaItemIndex]
                        if (currentSelectedSong != null) {
                            isFavourite = allSongList.first { currentSelectedSong!!.id == it.id }.isFavourite
                        }
                    }
                    is MusicPlayerState.Playing -> { isPlaying = musicPlayerState.isPlaying }
                    is MusicPlayerState.Progress -> { calculateProgress(musicPlayerState.progress) }
                    is MusicPlayerState.Buffering -> { calculateProgress(musicPlayerState.progress) }
                    is MusicPlayerState.Ready -> { duration = musicPlayerState.duration }
                    is MusicPlayerState.CurrentRepeatMode -> { repeatMode = musicPlayerState.repeatMode }
                    MusicPlayerState.Initial -> Unit
                }
            }
        }
    }

    init { loadSongData() }

    fun onLoadSongData() { loadSongData() }

    fun onSongClick(songIndex: Int) {
        viewModelScope.launch {
            queueManager.addSongsToQueue(allSongList)
            currentQueuedSongList = allSongList
            audioService.setRepeatMode(REPEAT_MODE_ALL)
            audioService.onPlayerEvent(PlayerEvent.SelectedSongChange(changedSongIndex = songIndex))
        }
    }

    fun onCustomQueueSongClick(songsInQueue: List<Song>, songIndex: Int) {
        viewModelScope.launch {
            queueManager.addSongsToQueue(songsInQueue)
            currentQueuedSongList = songsInQueue
            audioService.onPlayerEvent(PlayerEvent.SelectedSongChange(changedSongIndex = songIndex))
        }
    }

    fun onAllShuffleClick() {
        viewModelScope.launch {
            queueManager.addSongsToQueue(allSongList)
            currentQueuedSongList = allSongList
            val randomSongIndex = Random.nextInt(until = allSongList.lastIndex)
            audioService.setRepeatMode(REPEAT_MODE_ALL)
            audioService.onPlayerEvent(PlayerEvent.SelectedSongChange(changedSongIndex = randomSongIndex))
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

    fun onRepeatModeChange(@RepeatMode repeatMode: Int) {
        audioService.setRepeatMode(repeatMode)
    }

    fun onSongInfoClick(song: Song) {
        songInfo = SongInfo(
            shouldShowSheet = true,
            song = song
        )
    }

    fun onFavouriteClick(songId: Long, isFavourite: Boolean) {
        viewModelScope.launch {
            songRepository.setSongFavourite(songId, isFavourite)
        }
    }

    fun onSongInfoSheetDismiss() {
        songInfo = SongInfo(shouldShowSheet = false)
    }

    private fun loadSongData() {
        viewModelScope.launch {
            songRepository.getSongs().collectLatest { songResponse ->
                when (songResponse) {
                    is Response.Failure -> {
                        uiState = UiState.Failure(songResponse.message)
                    }
                    is Response.Success -> {
                        allSongList = songResponse.data
                        if (currentSelectedSong != null) {
                            isFavourite = songResponse.data.first { currentSelectedSong!!.id == it.id }.isFavourite
                        }
                        launch { extractSongArtistsAndCounts() }
                        extractAlbums()
                        uiState = UiState.Success(songResponse.data)
                    }
                    is Response.Loading -> {
                        uiState = UiState.Progress(
                            progress = songResponse.progress,
                            status = songResponse.message
                        )
                    }
                }
            }
        }
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

    private suspend fun currentPlayingSongColorPalette(coverArtData: ByteArray?) = withContext(Dispatchers.IO) {
        if (coverArtData != null) {
            val bitmap = BitmapFactory.decodeByteArray(coverArtData, 0, coverArtData.size)
            currentSelectedSongPalette = Palette.from(bitmap).generate()
        }
    }

    private suspend fun extractSongArtistsAndCounts() {
        _artistDetailsStream.value = artistRepository.getAllArtistsSongDetails()
    }

    private fun extractAlbums() {
        albumList = allSongList
            .groupBy { it.albumId }
            .map { (albumId, songsWithSameAlbumId) ->
                Album(
                    albumId = albumId,
                    albumName = songsWithSameAlbumId[0].albumName,
                    songs = songsWithSameAlbumId
                )
            }
    }
}

data class SongInfo(
    val shouldShowSheet: Boolean = false,
    val song: Song? = null
)