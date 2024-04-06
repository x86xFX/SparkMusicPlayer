package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.PlaylistWithSongsList
import me.theek.spark.core.model.data.PlaylistWithSongs

fun PlaylistWithSongsList.toPlaylistWithSongs() : PlaylistWithSongs {
    return PlaylistWithSongs(
        playlistId = playlistId,
        playlistName = playlistName,
        playlistCreatedAt = playlistCreatedAt,
        song = song.toSong()
    )
}