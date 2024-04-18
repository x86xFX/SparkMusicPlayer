package me.theek.spark.core.data.util

import android.net.Uri
import coil.ImageLoader
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import me.theek.spark.core.model.data.Song

class CoverArtFetcher(
    private val data: Song,
    private val options: Options,
    private val imageLoader: ImageLoader
) : Fetcher {

    class ImageFactory : Fetcher.Factory<Song> {
        override fun create(data: Song, options: Options, imageLoader: ImageLoader): Fetcher {
            return CoverArtFetcher(data, options, imageLoader)
        }
    }

    override suspend fun fetch(): FetchResult? {
        return if (data.externalId != null) {
            val coverUri = Uri.parse("content://media/external/audio/media/${data.externalId}/albumart")

            val existingFetcher = imageLoader.components.newFetcher(
                data = coverUri,
                imageLoader = imageLoader,
                startIndex = 1,
                options = options
            )

            existingFetcher?.first?.fetch()
        } else {
            null
        }
    }
}