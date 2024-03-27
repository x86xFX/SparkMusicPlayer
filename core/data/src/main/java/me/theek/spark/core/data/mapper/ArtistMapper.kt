package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.entity.ArtistDetailsEntity
import me.theek.spark.core.database.entity.ArtistProfileEntity
import me.theek.spark.core.model.data.ArtistDetails
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

fun ArtistProfileEntity.toArtistRemoteData() : ArtistRemoteData {
    return ArtistRemoteData(
        artistName = artistName,
        followers = followers,
        popularity = popularity,
        artistGenres = artistGenres,
        externalArtistProfile = externalArtistProfile,
        imageUrl = imageUrl
    )
}

fun ArtistInfoDto.toArtistProfileEntity(queryArtistName: String) : ArtistProfileEntity {
    return ArtistProfileEntity(
        artistName = queryArtistName,
        followers = followers,
        popularity = popularity,
        artistGenres = artistGenres,
        externalArtistProfile = externalArtistProfile,
        imageUrl = imageUrl
    )
}

fun ArtistDetailsEntity.toArtistDetails() : ArtistDetails {
    return ArtistDetails(
        artistName = artistName,
        songCount = songCount,
        totalDuration = totalDuration
    )
}