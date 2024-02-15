package me.theek.spark.core.model.data

data class Song(
    val id: Int,
    val path: String,
    val artistName: String?,
    val duration: Int?,
    val title: String?,
    val albumId: Int?,
    val trackNumber: Int
)