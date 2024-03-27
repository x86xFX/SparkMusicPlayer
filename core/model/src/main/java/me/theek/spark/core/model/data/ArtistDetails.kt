package me.theek.spark.core.model.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistDetails(
    val artistName: String,
    val songCount: Int,
    val totalDuration: Int
) : Parcelable