package me.theek.spark.core.data.util

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.model.data.Song
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream

class SongCoverArtFetcher(
    private val data: Song,
    private val options: Options,
    private val songRepository: SongRepository
) : Fetcher {

    class ImageFactory(private val songRepository: SongRepository) : Fetcher.Factory<Song> {
        override fun create(data: Song, options: Options, imageLoader: ImageLoader): Fetcher {
            return SongCoverArtFetcher(data, options, songRepository)
        }
    }

    override suspend fun fetch(): FetchResult? {
        val kTagLibResult = songRepository.getSongCoverArt(data.path)

        return if (kTagLibResult != null) {
            SourceResult(
                source = ImageSource(
                    source = ByteArrayInputStream(kTagLibResult).source().buffer(),
                    context = options.context,
                ),
                mimeType = null,
                dataSource = DataSource.MEMORY
            )
        } else null
    }
}