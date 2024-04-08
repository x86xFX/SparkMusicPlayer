package me.theek.spark.core.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.theek.spark.core.content_reader.MediaStoreReader
import me.theek.spark.core.data.mapper.toSong
import me.theek.spark.core.data.mapper.toSongEntity
import me.theek.spark.core.database.dao.SongDao
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.util.FlowEvent
import me.theek.spark.core.model.util.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSongRepository @Inject constructor(
    private val mediaStoreReader: MediaStoreReader,
    private val songDao: SongDao
) : SongRepository {

    override fun getSongImportStream(): Flow<FlowEvent<List<Song>>> = flow<FlowEvent<List<Song>>> {

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

                    is FlowEvent.Failure -> {
                        emit(FlowEvent.Failure(state.message))
                    }
                }
            }
        } else {
            emit(FlowEvent.Success(existingSongs.map { it.toSong() }))
        }
    }.flowOn(Dispatchers.IO)

    override fun getSongs(): Flow<Response<List<Song>>> = channelFlow<Response<List<Song>>> {

        songDao.getSongs().collectLatest { existingSongs ->
            if (existingSongs.isEmpty()) {
                when (val mediaData = mediaStoreReader.getAudioData().first()) {
                    is FlowEvent.Progress -> {
                        send(
                            Response.Loading(
                                size = mediaData.size,
                                progress = mediaData.asFloat(),
                                message = mediaData.message
                            )
                        )
                    }
                    is FlowEvent.Success -> {
                        send(Response.Success(mediaData.data))
                    }

                    is FlowEvent.Failure -> {
                        send(Response.Failure(mediaData.message))
                    }
                }
            } else {
                send(Response.Success(existingSongs.map { it.toSong() }))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getArtistSongs(artistName: String): Response<List<Song>> {
        val result = songDao.getArtistSongs(artistName).map { it.toSong() }

        return if (result.isEmpty()) {
            Response.Failure("Artist doesn't have any songs")
        } else {
            Response.Success(result)
        }
    }

    override fun getAlbumSongs(albumId: Int): Flow<List<Song>> {
        return songDao.getAlbumSongs(albumId).map {
            it.map { songEntity ->
                songEntity.toSong()
            }
        }
    }

    override fun getFavouriteSongs(): Flow<List<Song>> {
        return songDao.getFavouriteSongs().map { it.map { songEntity -> songEntity.toSong() } }
    }

    override suspend fun setSongFavourite(songId: Long, isFavourite: Boolean) {
        songDao.setSongFavourite(songId, isFavourite)
    }

    override suspend fun getSongCoverArt(songPath: String): ByteArray? {
        return mediaStoreReader.getSongCover(songPath)
    }
}