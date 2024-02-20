package me.theek.spark.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.data.repository.LocalSongRepository
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
}