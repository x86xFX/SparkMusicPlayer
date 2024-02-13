package me.theek.spark.core.data.mapper

import androidx.core.net.toUri
import me.theek.spark.core.database.entity.SongEntity
import me.theek.spark.core.model.data.Song

fun SongEntity.toSong() : Song {
    return Song(
        id = id,
        path = location,
        artistName = artistName,
        duration = duration,
        title = title,
        albumId = albumId,
        trackNumber = trackNumber,
        albumArt = albumArtLocation?.toUri()
    )
}

fun Song.toSongEntity() : SongEntity {
    return SongEntity(
        artistName = artistName,
        duration = duration,
        title = title,
        albumId = albumId,
        trackNumber = trackNumber,
        albumArtLocation = albumArt.toString(),
        location = path
    )
}