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
}

sealed interface MusicPlayerState {
    data object Initial : MusicPlayerState
    data object Pause : MusicPlayerState
    data object Playing : MusicPlayerState
}