package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.model.data.SongInPlayList

interface PlaylistRepository {
    fun getAllPlayLists() : Flow<List<PlaylistData>>
    suspend fun createPlaylistWithSong(
        song: Song,
        playListName: String
    )
    suspend fun createPlaylistWithSongs(playListWithSong: List<SongInPlayList>)
    suspend fun addSongToExistingPlaylist(songId: Long, playListId: Long)
}