package me.theek.spark.core.database

import androidx.room.Embedded
import me.theek.spark.core.database.entity.SongEntity

data class PlaylistWithSongs(
    val playlistId: Long,
    val playlistName: String,
    @Embedded val songForCoverArt: SongEntity
)

data class PlaylistWithSongsList(
    val playlistId: Long,
    val playlistName: String,
    val playlistCreatedAt: Long,
    @Embedded val song: SongEntity
)