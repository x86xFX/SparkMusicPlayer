package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.model.data.Song

interface SongRepository {
    fun getSongs() : Flow<FlowEvent<List<Song>>>
    suspend fun getSongCoverArt(songPath: String) : ByteArray?
}