package me.theek.spark.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfoDto(
    val artists: Artists
) {
    @Serializable
    data class Artists(
        val href: String,
        val items: List<ArtistData>,
        val limit: Int,
        val next: String?,
        val offset: Int,
        val previous: String?,
        val total: Int
    ) {
        @Serializable
        data class ArtistData(
            @SerialName("external_urls") val externalUrls: ExternalUrls,
            val followers: Followers,
            val genres: List<String>,
            val href: String,
            val id: String,
            val images: List<ArtistImage>,
            val name: String,
            val popularity: Int,
            val type: String,
            val uri: String
        ) {
            @Serializable
            data class ExternalUrls(
                val spotify: String
            )
            @Serializable
            data class Followers(
                val href: String?,
                val total: Long
            )
            @Serializable
            data class ArtistImage(
                val height: Int,
                val url: String,
                val width: Int,
            )
        }
    }
}