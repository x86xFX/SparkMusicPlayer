package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.Song

interface SongRepository {
    fun getSongs() : Flow<SongStreamState>
}

sealed interface SongStreamState {
    data class Progress(
        val retrieveHint: String,
        val progress: Float
    ) : SongStreamState
    data class Failure(val message: String?) : SongStreamState
    data class Success(val songs: List<Song>) : SongStreamState
}