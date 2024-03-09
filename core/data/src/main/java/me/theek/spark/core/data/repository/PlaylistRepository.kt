package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.Playlist

interface PlaylistRepository {
    fun getPlayLists() : Flow<List<Playlist>>
    suspend fun createPlayList(playlist: Playlist)
    suspend fun removePlaylist(playlist: Playlist)
}