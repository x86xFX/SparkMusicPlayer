package me.theek.spark.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [
        Index("path", unique = true)
    ]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "song_name") val songName: String?,
    @ColumnInfo(name = "artist_name") val artistName: String?,
    @ColumnInfo(name = "duration") val duration: Int?,
    @ColumnInfo(name = "album_id") val albumId: Int?,
    @ColumnInfo(name = "album_name") val albumName: String?,
    @ColumnInfo(name = "track_number") val trackNumber: Int?,
    @ColumnInfo(name = "released_year") val releasedYear: Int?,
    @ColumnInfo(name = "added_at") val addedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "genres") val genres: List<String>,
    @ColumnInfo(name = "mime_type") val mimeType: String?,
    @ColumnInfo(name = "last_modified") val lastModified: Long,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "external_id") val externalId: String?,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean
)