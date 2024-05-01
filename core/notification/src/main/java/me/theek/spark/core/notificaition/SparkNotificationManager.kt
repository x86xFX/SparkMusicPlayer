package me.theek.spark.core.notificaition

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.theek.spark.core.content_reader.MediaStoreReader
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_CHANNEL_ID
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_CHANNEL_NAME
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_ID

class SparkNotificationManager(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val mediaStoreReader: MediaStoreReader,
    private val sessionCompatToken: MediaSessionCompat.Token
) {

    fun startNotificationService(currentSong: Song) : Notification = notificationService(currentSong)

    @SuppressLint("MissingPermission")
    private fun notificationService(currentSong: Song) :Notification {

        createNotificationChannel()

        val notificationIntent = PendingIntent.getActivity(
            context,
            1,
            (context.applicationContext as ActivityIntentProvider).provideMainActivityIntent(),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(currentSong.songName ?: "Unknown")
            .setContentText(currentSong.artistName ?: "Unknown")
            .setShowWhen(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_spark_notification)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(sessionCompatToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setContentIntent(notificationIntent)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColorized(true)
            .setSilent(true)

        val coverImage = mediaStoreReader.getSongCover(currentSong.externalId)

        notificationBuilder.setLargeIcon(coverImage)

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) ?: run {
                val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableLights(false)
                notificationChannel.enableVibration(false)
                notificationChannel.setShowBadge(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }
}