package me.theek.spark.core.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistDetails(
    val artistName: String,
    val songs: List<Song>
) : Parcelable