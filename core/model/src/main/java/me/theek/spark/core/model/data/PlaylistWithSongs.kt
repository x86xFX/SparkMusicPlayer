package me.theek.spark.core.model.data

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PlaylistWithSongs(
    val playlistId: Long,
    val playlistName: String,
    val playlistCreatedAt: Long,
    val song: Song
) {
    fun toPlaylistCreatedDate() : String {
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault())
        return sdf.format(Date(playlistCreatedAt))
    }
}
