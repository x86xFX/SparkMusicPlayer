package me.theek.spark.core.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueManager @Inject constructor() {

    var currentQueue: List<Song> = emptyList()
        private set
    var currentPlayingSong: Song? = null
        private set
    var isPlaying: Boolean = false
        private set
    private val _currentPlayingSongStream: MutableStateFlow<Song?> = MutableStateFlow(null)
    val currentPlayingSongStream = _currentPlayingSongStream.asStateFlow()

    fun updateCurrentQueue(songs: List<Song>) { currentQueue = songs }

    fun updateCurrentPlayingSong(song: Song?) {
        currentPlayingSong = song
        _currentPlayingSongStream.update { song }
    }

    fun updatePlayingState(isPlaying: Boolean) { this.isPlaying = isPlaying }

    fun getCurrentSongsQueue() : List<Song> = currentQueue
}