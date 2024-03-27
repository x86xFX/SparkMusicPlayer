package me.theek.spark.core.data.repository

import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.model.util.Response

interface ArtistRepository {
    suspend fun getAllArtistsSongDetails() : List<ArtistDetails>
    suspend fun getAristDetails(artistName: String) : Response<ArtistRemoteData>
}