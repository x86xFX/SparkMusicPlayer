package me.theek.spark.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfoDto(
    @SerialName("artist_name") val artistName: String,
    @SerialName("followers") val followers: Int,
    @SerialName("popularity") val popularity: Int,
    @SerialName("artist_genres") val artistGenres: List<String>,
    @SerialName("artist_spotify_account") val externalArtistProfile: String,
    @SerialName("profile_image") val imageUrl: String
)