package me.theek.spark.core.model.data

import android.graphics.Bitmap
import android.net.Uri

data class Audio(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val artistName: String,
    val duration: Int,
    val title: String,
    val albumId: Int,
    val trackNumber: Int,
    val albumArt: Bitmap?
)
