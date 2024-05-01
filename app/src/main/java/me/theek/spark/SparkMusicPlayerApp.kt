package me.theek.spark

import android.app.Application
import android.content.Intent
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import me.theek.spark.core.notificaition.ActivityIntentProvider
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class SparkMusicPlayerApp : Application(), ActivityIntentProvider, ImageLoaderFactory {

    @Inject
    lateinit var coilImageLoader: Provider<ImageLoader>

    override fun newImageLoader(): ImageLoader = coilImageLoader.get()

    override fun provideMainActivityIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }
}