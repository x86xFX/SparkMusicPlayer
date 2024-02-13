package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import me.theek.spark.core.content_reader.ContentResolverHelper
import me.theek.spark.core.content_reader.FlowEvent
import me.theek.spark.core.data.mapper.toSong
import me.theek.spark.core.data.mapper.toSongEntity
import me.theek.spark.core.database.SongDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSongRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper,
    private val songDao: SongDao
) : SongRepository {

    override fun getSongs(): Flow<SongStreamState> = flow {
        /**
         * Initially check local database for existing songs.
         */
        val existingSongs = songDao.getSongs().first()

        /**
         * If local database is empty, request content resolvers to querying local songs metadata.
         */
        if (existingSongs.isEmpty()) {
            contentResolverHelper.getAudioData().collect { songStreamResult ->
                when (songStreamResult) {
                    /**
                     * Something went wrong with content resolvers
                     */
                    is FlowEvent.Failure -> {
                        emit(SongStreamState.Failure(songStreamResult.message))
                    }
                    /**
                     * Content resolvers emit song meta data in each iteration.
                     */
                    is FlowEvent.Progress -> {
                        emit(
                            SongStreamState.Progress(
                                retrieveHint = songStreamResult.data.message,
                                progress = songStreamResult.data.progress.asFloat()
                            )
                        )
                    }
                    /**
                     * Trigger when retrieving all album details, songs list. Use room to cache result.
                     */
                    is FlowEvent.Success -> {
                        songStreamResult.result.onEach { song ->
                            songDao.insertSong(song = song.toSongEntity())
                        }
                        emit(SongStreamState.Success(songStreamResult.result))
                    }
                }
            }
        } else {
            emit(
                SongStreamState.Success(
                    songs = existingSongs.map { it.toSong() }
                )
            )
        }
    }
}