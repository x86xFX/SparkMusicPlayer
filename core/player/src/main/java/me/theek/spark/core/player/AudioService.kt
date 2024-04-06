package me.theek.spark.core.player

import androidx.annotation.IntDef
import kotlinx.coroutines.flow.StateFlow
import me.theek.spark.core.model.data.Song

interface AudioService {
    val musicPlayStateStream: StateFlow<MusicPlayerState>
    fun addSongsToQueue(mediaItems: List<Song>)
    fun clearCurrentQueue()
    suspend fun onPlayerEvent(playerEvent: PlayerEvent)
    fun setRepeatMode(@RepeatMode repeatMode: Int)
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
    data class CurrentRepeatMode(@RepeatMode val repeatMode: Int) : MusicPlayerState
}

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
@IntDef(RepeatMode.REPEAT_MODE_OFF, RepeatMode.REPEAT_MODE_ONE, RepeatMode.REPEAT_MODE_ALL)
annotation class RepeatMode {
    companion object {
        const val REPEAT_MODE_OFF = 0
        const val REPEAT_MODE_ONE = 1
        const val REPEAT_MODE_ALL = 2
    }
}