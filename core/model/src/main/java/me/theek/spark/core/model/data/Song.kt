package me.theek.spark.core.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val songName: String?,
    val artistName: String?,
    val albumId: Int?,
    val albumName: String?,
    val duration: Int?,
    val trackNumber: Int?,
    val releaseYear: Int?,
    val genres: List<String>,
    val mimeType: String?,
    val lastModified: Long,
    val size: Long,
    val path: String,
    val externalId: String?,
    val isFavourite: Boolean
) : Parcelable