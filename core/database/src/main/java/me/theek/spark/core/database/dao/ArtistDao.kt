package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.theek.spark.core.database.entity.ArtistProfileEntity

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artistProfileEntity: ArtistProfileEntity)

    @Query("SELECT * FROM artist_profiles WHERE artist_name = :artistName")
    suspend fun getArtistProfileDetails(artistName: String) : List<ArtistProfileEntity>
}