package me.theek.spark.core.data.util

import coil.intercept.Interceptor
import coil.request.ImageResult
import me.theek.spark.core.model.data.ArtistDetails

class ArtistCoverInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val imageRequest = chain.request

        return if (imageRequest.data is ArtistDetails) {
            val artistUrl = "https://spark-music-player-417505.et.r.appspot.com/v1/spark/music/artist/profile/${(imageRequest.data as ArtistDetails).artistName}"

            val artistRequest = imageRequest.newBuilder()
                .data(artistUrl)
                .crossfade(true)
                .crossfade(500)
                .build()

            chain.proceed(artistRequest)

        } else {
            chain.proceed(imageRequest)
        }
    }
}