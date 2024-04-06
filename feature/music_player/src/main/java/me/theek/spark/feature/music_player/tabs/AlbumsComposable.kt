package me.theek.spark.feature.music_player.tabs

import androidx.compose.runtime.Composable
import me.theek.spark.core.model.data.Album

@Composable
internal fun AlbumsComposable(albums: List<Album>) {
    albums.forEach {
        println("Album: ${it.albumName}")
    }
}