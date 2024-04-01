package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.FlowEvent
import me.theek.spark.core.model.util.Response

interface SongRepository {
    fun getSongImportStream() : Flow<FlowEvent<List<Song>>>
    fun getSongs() : Flow<Response<List<Song>>>
    suspend fun getArtistSongs(artistName: String) : Response<List<Song>>
    suspend fun getSongCoverArt(songPath: String) : ByteArray?
}