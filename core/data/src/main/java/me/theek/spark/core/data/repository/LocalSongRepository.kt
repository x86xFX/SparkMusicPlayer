package me.theek.spark.core.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.theek.spark.core.model.data.FlowEvent
import me.theek.spark.core.content_reader.MediaStoreReader
import me.theek.spark.core.data.mapper.toSong
import me.theek.spark.core.data.mapper.toSongEntity
import me.theek.spark.core.database.SongDao
import me.theek.spark.core.model.data.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSongRepository @Inject constructor(
    private val mediaStoreReader: MediaStoreReader,
    private val songDao: SongDao
) : SongRepository {

    override fun getSongs(): Flow<FlowEvent<List<Song>>> = flow<FlowEvent<List<Song>>> {

        Log.d("LocalSongRepository", "Flow executed on ${Thread.currentThread().name}")

        val existingSongs = songDao.getSongs().first()

        if (existingSongs.isEmpty()) {
            mediaStoreReader.getAudioData().collect { state ->
                when (state) {
                    is FlowEvent.Progress -> {
                        emit(
                            FlowEvent.Progress(
                                size = state.size,
                                progress = state.progress,
                                message = state.message
                            )
                        )
                    }
                    is FlowEvent.Success -> {
                        state.data.onEach { song ->
                            songDao.insertSong(song.toSongEntity())
                        }
                        emit(FlowEvent.Success(state.data))
                    }
                }
            }
        } else {
            emit(FlowEvent.Success(existingSongs.map { it.toSong() }))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getSongCoverArt(songPath: String): ByteArray? {
        return mediaStoreReader.getSongCover(songPath)
    }
}