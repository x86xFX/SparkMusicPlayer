package me.theek.spark.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("song_name") val songName: String?,
    @ColumnInfo("artist_name")val artistName: String?,
    val duration: Int?,
    val albumId: Int?,
    @ColumnInfo("track_number")val trackNumber: Int?,
    @ColumnInfo("released_year")val releasedYear: Int?,
    val location: String,
    @ColumnInfo("added_date") val addedDate: Long = System.currentTimeMillis()
)
