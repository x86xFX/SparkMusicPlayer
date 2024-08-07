package me.theek.spark.core.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.mapper.toArtistDetails
import me.theek.spark.core.data.mapper.toArtistProfileEntity
import me.theek.spark.core.data.mapper.toArtistRemoteData
import me.theek.spark.core.database.dao.ArtistDao
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.model.util.Response
import me.theek.spark.core.network.RemoteArtistService
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteArtistRepository @Inject constructor(
    private val remoteArtistService: RemoteArtistService,
    private val artistDao: ArtistDao
) : ArtistRepository {

    override suspend fun getAllArtistsSongDetails(): List<ArtistDetails> = withContext(Dispatchers.IO) {
        artistDao.getArtistsSongsDetails().map { it.toArtistDetails() }
    }

    override suspend fun getAristDetails(artistName: String): Response<ArtistRemoteData> = withContext(Dispatchers.IO) {
        val existingData = artistDao.getArtistProfileDetails(artistName = artistName)

        if (existingData.isNotEmpty()) {
            Response.Success(existingData[0].toArtistRemoteData())

        } else {
            try {
                val response = remoteArtistService.getSongArtistDetails(type = "details", artistName = artistName)
                artistDao.insert(artistProfileEntity = response.toArtistProfileEntity(artistName))
                Response.Success(response.toArtistRemoteData())

            } catch(e: ClientRequestException) {
                Response.Failure("Client side error occurred")

            } catch (e: ServerResponseException) {
                Response.Failure("Internal server error occurred")

            } catch (e: UnknownHostException) {
                Response.Failure("Network error occurred")

            } catch (e: Exception) {
                Response.Failure("Something went wrong")
            }
        }
    }
}
