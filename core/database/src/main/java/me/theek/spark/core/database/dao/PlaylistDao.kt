package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getPlaylist() : Flow<List<PlaylistEntity>>

    @Delete
    suspend fun delete(playlistEntity: PlaylistEntity)
}