package me.theek.spark.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_in_playlist",
    indices = [
        Index("song_id"),
        Index("playlist_id")
    ],
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"]
        ),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["song_id"]
        )
    ]
)
data class PlaylistWithSongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlist_id") val playlistId: Long,
    @ColumnInfo(name = "song_id") val songId: Long
)
