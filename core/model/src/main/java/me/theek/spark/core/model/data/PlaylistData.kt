package me.theek.spark.core.model.data

data class PlaylistData(
    val playlistId: Long,
    val playlistName: String,
    val songForCoverArt: Song
)