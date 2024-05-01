package me.theek.spark.core.service

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.theek.spark.core.notificaition.Constants.NOTIFICATION_ID
import me.theek.spark.core.notificaition.SparkNotificationManager
import me.theek.spark.core.player.QueueManager
import javax.inject.Inject

@AndroidEntryPoint
class MediaService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var notificationManager: SparkNotificationManager

    @Inject
    lateinit var queueManager: QueueManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            CoroutineScope(Dispatchers.IO).launch {
                queueManager.currentPlayingSongStream.collectLatest {
                    it?.let { song ->
                        startForeground(NOTIFICATION_ID, notificationManager.startNotificationService(song))
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    override fun onDestroy() {
        mediaSession.apply {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
        super.onDestroy()
    }
}