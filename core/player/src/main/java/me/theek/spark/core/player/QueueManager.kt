package me.theek.spark.core.player

import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueManager @Inject constructor(private val audioService: AudioService) {
    fun addSongsToQueue(songs: List<Song>) {
        audioService.addSongsToQueue(mediaItems = songs)
    }

    fun replaceCurrentQueue(songs: List<Song>) {
        audioService.replaceCurrentQueue(mediaItems = songs)
    }

    fun clearCurrentQueue() {
        audioService.clearCurrentQueue()
    }
}