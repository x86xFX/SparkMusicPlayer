package me.theek.spark.core.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.mapper.toPlayList
import me.theek.spark.core.data.mapper.toPlayListEntity
import me.theek.spark.core.database.dao.PlaylistDao
import me.theek.spark.core.model.data.Playlist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPlaylistRepository @Inject constructor(private val playlistDao: PlaylistDao) : PlaylistRepository {

    override fun getPlayLists(): Flow<List<Playlist>> = flow {
        playlistDao.getPlaylist().collect { songs ->
            emit(songs.map { song -> song.toPlayList() })
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createPlayList(playlist: Playlist) = withContext(Dispatchers.IO) {
        playlistDao.insert(playlist.toPlayListEntity())
    }

    override suspend fun removePlaylist(playlist: Playlist) = withContext(Dispatchers.IO) {
        playlistDao.delete(playlist.toPlayListEntity())
    }
}