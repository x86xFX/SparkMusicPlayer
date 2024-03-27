package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.database.entity.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = SongEntity::class)
    suspend fun insertSong(song: SongEntity)

    @Query("SELECT * FROM songs")
    fun getSongs() : Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE artist_name = :artistName")
    suspend fun getArtistSongs(artistName: String) : List<SongEntity>
}