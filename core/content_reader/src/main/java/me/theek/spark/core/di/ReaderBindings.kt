package me.theek.spark.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.content_reader.AudioContentResolver
import me.theek.spark.core.content_reader.ContentResolverHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReaderBindings {

    @Binds
    @Singleton
    abstract fun bindAudioContentResolver(audioContentResolver: AudioContentResolver) : ContentResolverHelper
}