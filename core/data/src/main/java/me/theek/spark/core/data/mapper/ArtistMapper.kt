package me.theek.spark.core.data.mapper

import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.network.dto.ArtistInfoDto

fun ArtistInfoDto.toArtistRemoteData() : ArtistRemoteData {
    return ArtistRemoteData(
        artistName = artistName,
        followers = followers,
        popularity = popularity,
        artistGenres = artistGenres,
        externalArtistProfile = externalArtistProfile,
        imageUrl = imageUrl
    )
}