package me.theek.spark.core.data.util

import android.net.Uri
import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.util.Response

class ArtistCoverInterceptor(private val artistRepository: ArtistRepository) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val imageRequest = chain.request

        return if (imageRequest.data is ArtistDetails) {
            val artistResponse = artistRepository.getAristImages((imageRequest.data as ArtistDetails).artistName)
            when (artistResponse) {
                is Response.Loading -> {
                    ErrorResult(
                        drawable = null,
                        request = imageRequest,
                        throwable = Exception(artistResponse.message)
                    )
                }

                is Response.Failure -> {
                    ErrorResult(
                        drawable = null,
                        request = imageRequest,
                        throwable = Exception(artistResponse.message)
                    )
                }
                is Response.Success -> {
                    val artistUri = Uri.parse(artistResponse.data.imageUrl)
                        .buildUpon()
                        .build()

                    val artistRequest = imageRequest.newBuilder()
                        .data(artistUri)
                        .crossfade(true)
                        .crossfade(500)
                        .build()

                    chain.proceed(artistRequest)
                }
            }
        } else {
            chain.proceed(imageRequest)
        }
    }
}