package me.theek.spark.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.theek.spark.core.database.entity.ArtistDetailsEntity
import me.theek.spark.core.database.entity.ArtistProfileEntity

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artistProfileEntity: ArtistProfileEntity)

    @Query("SELECT * FROM artist_profiles WHERE artist_name = :artistName")
    suspend fun getArtistProfileDetails(artistName: String) : List<ArtistProfileEntity>

    @Query("""
        SELECT artist_name as artistName, 
        COUNT(artist_name) as songCount,
        SUM(duration) as totalDuration
        FROM songs GROUP BY artistName ORDER BY songCount DESC
    """)
    suspend fun getArtistsSongsDetails() : List<ArtistDetailsEntity>
}