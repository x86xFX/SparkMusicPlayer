package me.theek.spark

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class SparkMusicPlayerApp : Application(), ImageLoaderFactory {

    @Inject
    lateinit var coilImageLoader: Provider<ImageLoader>

    override fun newImageLoader(): ImageLoader = coilImageLoader.get()

}