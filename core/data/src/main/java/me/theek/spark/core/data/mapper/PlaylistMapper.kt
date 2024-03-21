package me.theek.spark.core.data.mapper

import me.theek.spark.core.database.PlaylistWithSongs
import me.theek.spark.core.database.entity.PlaylistEntity
import me.theek.spark.core.database.entity.PlaylistWithSongsEntity
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.SongInPlayList

fun Playlist.toPlayListEntity() : PlaylistEntity {
    return PlaylistEntity(playListName = name)
}

fun SongInPlayList.toSongInPlaylistEntity() : PlaylistWithSongsEntity {
    return PlaylistWithSongsEntity(
        songId = songId,
        playlistId = playlistId
    )
}

fun PlaylistWithSongs.toPlaylistData() : PlaylistData {
    return PlaylistData(
        playlistId = playlistId,
        playlistName = playlistName,
        songForCoverArt = songForCoverArt.toSong()
    )
}