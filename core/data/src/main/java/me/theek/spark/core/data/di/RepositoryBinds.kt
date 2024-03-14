package me.theek.spark.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.data.repository.ArtistRepository
import me.theek.spark.core.data.repository.LocalPlaylistRepository
import me.theek.spark.core.data.repository.LocalSongRepository
import me.theek.spark.core.data.repository.PlaylistRepository
import me.theek.spark.core.data.repository.RemoteArtistRepository
import me.theek.spark.core.data.repository.SongRepository
import me.theek.spark.core.data.repository.UserDataRepository
import me.theek.spark.core.data.repository.UserPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinds {

    @Binds
    @Singleton
    abstract fun bindSongRepository(localSongRepository: LocalSongRepository) : SongRepository

    @Binds
    @Singleton
    abstract fun bindUserDataRepository(userPreferencesRepository: UserPreferencesRepository) : UserDataRepository

    @Binds
    @Singleton
    abstract fun bindPlayListRepository(localPlaylistRepository: LocalPlaylistRepository) : PlaylistRepository

    @Binds
    @Singleton
    abstract fun bindArtistRepository(remoteArtistRepository: RemoteArtistRepository) : ArtistRepository
}