package me.theek.spark.core.model.data

data class Album(
    val albumId: Int?,
    val albumName: String?,
    val songs: List<Song>
)
