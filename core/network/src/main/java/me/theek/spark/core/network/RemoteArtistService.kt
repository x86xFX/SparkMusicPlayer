package me.theek.spark.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import me.theek.spark.core.network.dto.ArtistInfoDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteArtistService @Inject constructor(private val httpClient: HttpClient) {

    /**
     * https://spark-music-player-417505.et.r.appspot.com/v1/spark/music/artist/profile/taylor
     * Client response filter. If type is image, response is artist image without artist spotify details.
     * @param type - Available types: ["profile", "details"]
     */
    suspend fun getSongArtistDetails(type: String, artistName: String): ArtistInfoDto {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "spark-music-player-417505.et.r.appspot.com"
                basicAuth(
                    username = "F35_r01s@!10@kl4ls4qlgm",
                    password = "11s@F35_r0lgsll@44k0qm!"
                )
                path("v1", "spark", "music", "artist", type, artistName)
            }
        }.body<ArtistInfoDto>()
    }
}