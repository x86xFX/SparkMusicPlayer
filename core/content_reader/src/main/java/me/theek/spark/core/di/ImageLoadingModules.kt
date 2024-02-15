package me.theek.spark.core.di

import com.simplecityapps.ktaglib.KTagLib
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoadingModules {

    @Provides
    @Singleton
    fun provideKTagLib(): KTagLib {
        return KTagLib()
    }
}