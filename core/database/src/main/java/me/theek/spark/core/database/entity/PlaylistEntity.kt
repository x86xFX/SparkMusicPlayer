package me.theek.spark.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlists"
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlist_name") val playListName: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
