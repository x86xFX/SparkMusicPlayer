package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import me.theek.spark.core.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: PlaylistEntity) : Long
}