package me.theek.spark.core.model.data

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
    val albumArt: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Audio

        if (albumArt != null) {
            if (other.albumArt == null) return false
            if (!albumArt.contentEquals(other.albumArt)) return false
        } else if (other.albumArt != null) return false

        return true
    }

    override fun hashCode(): Int {
        return albumArt?.contentHashCode() ?: 0
    }
}