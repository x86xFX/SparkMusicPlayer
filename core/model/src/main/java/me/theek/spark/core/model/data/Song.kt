package me.theek.spark.core.model.data

data class Song(
    val id: Long,
    val songName: String?,
    val artistName: String?,
    val albumId: Int?,
    val duration: Int?,
    val trackNumber: Int?,
    val releaseYear: Int?,
    val genres: List<String>,
    val mimeType: String?,
    val lastModified: Long,
    val size: Long,
    val path: String,
    val externalId: String?
)