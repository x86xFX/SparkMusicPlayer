package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.entity.SongEntity
import me.theek.spark.core.model.data.Song

fun SongEntity.toSong() : Song {
    return Song(
        id = id,
        path = location,
        artistName = artistName,
        duration = duration,
        songName = songName,
        albumId = albumId,
        trackNumber = trackNumber
    )
}

fun Song.toSongEntity() : SongEntity {
    return SongEntity(
        artistName = artistName,
        duration = duration,
        songName = songName,
        albumId = albumId,
        trackNumber = trackNumber,
        location = path
    )
}