package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.model.data.Song

interface SongRepository {
    fun getSongImportStream() : Flow<FlowEvent<List<Song>>>
    suspend fun getSongs() : Response<List<Song>>
    suspend fun getSongCoverArt(songPath: String) : ByteArray?
}

sealed interface Response<out T> {
    data class Success<T>(val data: List<Song>) : Response<T>
    data class Failure<T>(val message: String?) : Response<T>
    data class Loading<T>(
        val size: Int,
        val progress: Float,
        val message: String?
    ): Response<T>
}