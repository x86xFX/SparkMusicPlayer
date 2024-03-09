package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.ArtistRemoteData

interface ArtistRepository {
    fun getAristImages(artistName: String) : Flow<ArtistRemoteData>
}