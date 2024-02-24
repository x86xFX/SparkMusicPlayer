package me.theek.spark.core.model.data

data class Song(
    val id: Int,
    val songName: String?,
    val artistName: String?,
    val albumId: Int?,
    val duration: Int?,
    val trackNumber: Int?,
    val releaseYear: Int?,
    val path: String
)