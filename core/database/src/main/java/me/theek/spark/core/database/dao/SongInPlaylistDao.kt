package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.database.entity.PlaylistData
import me.theek.spark.core.database.entity.SongInPlaylistEntity

@Dao
interface SongInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: SongInPlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: List<SongInPlaylistEntity>)

    @Query("""
        SELECT songs.*, song_in_playlist.id as playlistSongId FROM songs
        LEFT JOIN song_in_playlist ON songs.id = song_in_playlist.song_id
            WHERE song_in_playlist.playlist_id = :playListId
    """)
    fun getSongsPlaylist(playListId: Long) : Flow<List<PlaylistData>>

    @Query("DELETE FROM song_in_playlist WHERE playlist_id = :playListId and id IN (:playlistSongIds)")
    suspend fun delete(
        playListId: Int,
        playlistSongIds: Array<Long>
    )

    @Query("DELETE FROM song_in_playlist WHERE playlist_id = :playListId and song_id IN (:songIds)")
    suspend fun deleteSongs(playListId: Int, songIds: Array<Long>)
}