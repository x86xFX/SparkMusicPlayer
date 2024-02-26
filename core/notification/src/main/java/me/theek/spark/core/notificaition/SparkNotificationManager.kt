package me.theek.spark.core.notificaition

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_CHANNEL_ID
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_CHANNEL_NAME
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_ID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SparkNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayer: ExoPlayer
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    fun startNotificationService(
        mediaSession: MediaSession,
        mediaSessionService: MediaSessionService
    ) {
        buildNotification(mediaSession)
        startForegroundNotificationService(mediaSessionService)
    }

    @OptIn(UnstableApi::class)
    private fun buildNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
        .setMediaDescriptionAdapter(
            SparkNotificationAdapter(
                context = context,
                pendingIntent = mediaSession.sessionActivity
            )
        )
        .setSmallIconResourceId(R.drawable.ic_spark_notification)
        .build().also {
            it.setMediaSessionToken(mediaSession.sessionCompatToken)
            it.setUseFastForwardActionInCompactView(true)
            it.setUseRewindActionInCompactView(true)
            it.setPriority(NotificationCompat.PRIORITY_LOW)
            it.setUseNextActionInCompactView(true)
            it.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            it.setColorized(true)
            it.setPlayer(exoPlayer)
        }
    }

    private fun startForegroundNotificationService(mediaSessionService: MediaSessionService) {
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .build()
        } else {
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .build()
        }

        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager.createNotificationChannel(channel)
        }
    }
}