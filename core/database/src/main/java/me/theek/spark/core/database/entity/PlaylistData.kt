package me.theek.spark.core.database.entity

import androidx.room.Embedded

data class PlaylistData(
    val playlistSongId: Long,
    @Embedded
    val songData: SongEntity
)