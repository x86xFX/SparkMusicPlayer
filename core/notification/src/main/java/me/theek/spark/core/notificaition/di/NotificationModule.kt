package me.theek.spark.core.notificaition.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.content_reader.MediaStoreReader
import me.theek.spark.core.notificaition.SparkNotificationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideSparkNotificationManager(
        @ApplicationContext context: Context,
        mediaSession: MediaSession,
        mediaStoreReader: MediaStoreReader
    ) : SparkNotificationManager {

        return SparkNotificationManager(
            context = context,
            notificationManager = NotificationManagerCompat.from(context),
            mediaStoreReader = mediaStoreReader,
            sessionCompatToken = mediaSession.sessionCompatToken
        )
    }
}