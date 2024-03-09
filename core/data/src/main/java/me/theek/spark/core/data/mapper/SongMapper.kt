package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.entity.SongEntity
import me.theek.spark.core.model.data.Song

fun SongEntity.toSong() : Song {
    return Song(
        id = id,
        path = path,
        artistName = artistName,
        duration = duration,
        songName = songName,
        albumId = albumId,
        releaseYear = releasedYear,
        trackNumber = trackNumber,
        genres = genres,
        mimeType = mimeType,
        lastModified = lastModified,
        size = size,
        externalId = externalId
    )
}

fun Song.toSongEntity() : SongEntity {
    return SongEntity(
        artistName = artistName,
        duration = duration,
        songName = songName,
        albumId = albumId,
        trackNumber = trackNumber,
        releasedYear = releaseYear,
        path = path,
        genres = genres,
        mimeType = mimeType,
        lastModified = lastModified,
        size = size,
        externalId = externalId
    )
}