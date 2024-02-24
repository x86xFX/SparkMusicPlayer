package me.theek.spark.core.player.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.player.AudioService
import me.theek.spark.core.player.MediaListener
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Bindings {

    @Binds
    @Singleton
    abstract fun bindAudioService(mediaListener: MediaListener) : AudioService
}