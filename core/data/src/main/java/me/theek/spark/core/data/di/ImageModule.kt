package me.theek.spark.core.data.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.data.util.ArtistCoverInterceptor
import me.theek.spark.core.data.util.CoverArtFetcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideCoilImageLoader(
        @ApplicationContext context: Context,
        songRepository: SongRepository,
        artistRepository: ArtistRepository
    ) : ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                add(CoverArtFetcher.ImageFactory(songRepository))
                add(ArtistCoverInterceptor(artistRepository))
            }
            .crossfade(true)
            .crossfade(500)
            .logger(DebugLogger())
            .build()
    }
}