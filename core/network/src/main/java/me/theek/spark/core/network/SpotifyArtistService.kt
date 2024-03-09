package me.theek.spark.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import me.theek.spark.core.network.dto.ArtistInfoDto
import me.theek.spark.core.network.dto.SpotifyAuthDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyArtistService @Inject constructor(private val httpClient: HttpClient) {

    /**
     * https://open.spotify.com/get_access_token?reason=transport&productType=web_player
     */
    private suspend fun getSpotifyUnauthorizedKey() : SpotifyAuthDto {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "open.spotify.com"
                path("get_access_token")
                parameters.append(name = "reason", value = "transport")
                parameter(key = "productType", value = "web_player")
            }
        }.body<SpotifyAuthDto>()
    }

    /**
     * https://api.spotify.com/v1/search?type=artist&q=Ariana&decorate_restrictions=false&limit=1
     */
    suspend fun getSongArtistImages(
        accessToken: String,
        artistName: String
    ) : ArtistInfoDto {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.spotify.com"
                bearerAuth(accessToken)
                path("search")
                parameters.append(name = "type", value = "artist")
                parameter(key = "q", value = artistName)
                parameter(key = "decorate_restrictions", value = false)
                parameter(key = "limit", value = 1)
            }
        }.body<ArtistInfoDto>()
    }
}