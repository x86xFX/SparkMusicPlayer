package me.theek.spark.core.model.data

data class ArtistRemoteData(
    val artistName: String,
    val followers: Int,
    val popularity: Int,
    val artistGenres: List<String>,
    val externalArtistProfile: String,
    val imageUrl: String
)