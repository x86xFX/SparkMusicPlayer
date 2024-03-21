package me.theek.spark.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_profiles")
data class ArtistProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("artist_name") val artistName: String,
    val followers: Int,
    val popularity: Int,
    @ColumnInfo("artist_genres") val artistGenres: List<String>,
    @ColumnInfo("external_artist_profile") val externalArtistProfile: String,
    @ColumnInfo("cover_image") val imageUrl: String
)