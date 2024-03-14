package me.theek.spark.core.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.theek.spark.core.data.mapper.toArtistRemoteData
import me.theek.spark.core.model.data.ArtistRemoteData
import me.theek.spark.core.model.util.Response
import me.theek.spark.core.network.SpotifyArtistService
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteArtistRepository @Inject constructor(private val spotifyArtistService: SpotifyArtistService) : ArtistRepository {

    override suspend fun getAristImages(artistName: String): Response<ArtistRemoteData> = withContext(Dispatchers.IO) {
        /**
         * Get unauthorized access token from spotify web api
         */
        try {
            val accessToken = "BQCdKs5rjp1JgbMGsREUorh0tPh_V_RqpuDfk1zO8OdtWZS4wPLp4ogTTBUlFuzi0yGwzFlHCvPkRpY1GFuW2sH584b5tSmPO3ZmfPC98k3LLjhozM0"
                //spotifyArtistService.getSpotifyUnauthorizedKey().accessToken
            /**
             * Use that unauthorized token to get artist details
             */
            val artistResponse = spotifyArtistService.getSongArtistImages(
                accessToken = accessToken,
                artistName = artistName
            )
            Response.Success(data = artistResponse.toArtistRemoteData())
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
