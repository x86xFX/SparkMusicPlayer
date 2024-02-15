package me.theek.spark.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val artistName: String?,
    val duration: Int?,
    val title: String?,
    val albumId: Int?,
    val trackNumber: Int,
    val location: String
)
