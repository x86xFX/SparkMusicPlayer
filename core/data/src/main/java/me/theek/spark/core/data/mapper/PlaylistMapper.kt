package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.entity.PlaylistEntity
import me.theek.spark.core.model.data.Playlist

fun PlaylistEntity.toPlayList() : Playlist {
    return Playlist(
        id = id,
        name = playListName,
        createdAt = createdAt
    )
}

fun Playlist.toPlayListEntity() : PlaylistEntity {
    return PlaylistEntity(playListName = name)
}