package me.theek.spark.core.data.mapper

import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.network.dto.ArtistInfoDto

fun ArtistInfoDto.toArtistRemoteData() : ArtistRemoteData {
    return ArtistRemoteData(
        artistName = artists.items[0].name,
        followers = artists.items[0].followers.total,
        popularity = artists.items[0].popularity,
        artistGenres = artists.items[0].genres,
        externalArtistProfile = artists.items[0].externalUrls.spotify,
        imageUrl = artists.items[0].images[0].url
    )
}