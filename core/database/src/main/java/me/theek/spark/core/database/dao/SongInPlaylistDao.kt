package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.database.PlaylistWithSongs
import me.theek.spark.core.database.entity.PlaylistWithSongsEntity

@Dao
interface SongInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistWithSongsEntity: PlaylistWithSongsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songInPlaylistEntities: List<PlaylistWithSongsEntity>)

    @Query("""
        SELECT p.id as playlistId, p.playlist_name as playlistName, songs.*
        FROM playlists AS p
        INNER JOIN song_in_playlist AS sp ON p.id = sp.playlist_id
        INNER JOIN songs ON sp.song_id = songs.id
        ORDER BY p.playlist_name ASC
    """)
    fun getSongsPlaylists() : Flow<List<PlaylistWithSongs>>


    @Query("DELETE FROM song_in_playlist WHERE playlist_id IN (:playlistIds)")
    suspend fun deleteSongsWithPlaylist(playlistIds: List<Long>)

    @Query("DELETE FROM playlists WHERE id IN (:playlistIds)")
    suspend fun deletePlaylist(playlistIds: List<Long>)

    @Transaction
    suspend fun deletePlaylists(playlistIds: List<Long>) {
        deleteSongsWithPlaylist(playlistIds)
        deletePlaylist(playlistIds)
    }
}