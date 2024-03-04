package me.theek.spark.core.player

import kotlinx.coroutines.flow.StateFlow
import me.theek.spark.core.model.data.Song

interface AudioService {
    val musicPlayStateStream: StateFlow<MusicPlayerState>
    fun setMediaItemList(mediaItems: List<Song>)
    suspend fun onPlayerEvent(playerEvent: PlayerEvent)
    fun stopPlayer()
}

sealed interface PlayerEvent {
    data object Backward : PlayerEvent
    data object Forward : PlayerEvent
    data object PlayPause : PlayerEvent
    data class SelectedSongChange(val changedSongIndex: Int) : PlayerEvent
    data class SeekTo(val seekTo: Long) : PlayerEvent
}

sealed interface MusicPlayerState {
    data object Initial : MusicPlayerState
    data class Ready(val duration: Long) : MusicPlayerState
    data class Progress(val progress: Long) : MusicPlayerState
    data class Buffering(val progress: Long) : MusicPlayerState
    data class Playing(val isPlaying: Boolean) : MusicPlayerState
    data class CurrentPlaying(val mediaItemIndex: Int) : MusicPlayerState
}