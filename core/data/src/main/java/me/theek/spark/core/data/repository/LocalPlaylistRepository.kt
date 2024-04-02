package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.theek.spark.core.data.mapper.toPlayListEntity
import me.theek.spark.core.data.mapper.toPlaylistData
import me.theek.spark.core.data.mapper.toSongInPlaylistEntity
import me.theek.spark.core.database.dao.PlaylistDao
import me.theek.spark.core.database.dao.SongInPlaylistDao
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.data.SongInPlayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val songInPlaylistDao: SongInPlaylistDao
) : PlaylistRepository {

    override fun getAllPlayLists(): Flow<List<PlaylistData>> {
        return songInPlaylistDao.getSongsPlaylists()
            .map {
            it.map {  playlistDataEntity ->
                playlistDataEntity.toPlaylistData()
            }
        }
    }

    override suspend fun createPlaylistWithSong(
        song: Song,
        playListName: String
    ) {
        val playlistId = playlistDao.insert(
            playlistEntity = Playlist(
                name = playListName
            ).toPlayListEntity()
        )

        songInPlaylistDao.insert(
            playlistWithSongsEntity = SongInPlayList(
                songId = song.id,
                playlistId = playlistId
            ).toSongInPlaylistEntity()
        )
    }

    override suspend fun createPlaylistWithSongs(playListWithSong: List<SongInPlayList>) {
        songInPlaylistDao.insert(playListWithSong.map { it.toSongInPlaylistEntity() })
    }

    override suspend fun addSongToExistingPlaylist(songId: Long, playListId: Long) {
        songInPlaylistDao.insert(
            playlistWithSongsEntity = SongInPlayList(
                songId = songId,
                playlistId = playListId
            ).toSongInPlaylistEntity()
        )
    }

    override suspend fun deletePlaylists(playListIds: List<Long>) {
        songInPlaylistDao.deletePlaylists(playListIds)
    }
}