package me.theek.spark.core.notificaition

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.runBlocking

@UnstableApi
class SparkNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?
) : PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.displayTitle ?: "Unknown"
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? = pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence? {
        return player.mediaMetadata.artist
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? = runBlocking {
        if (player.mediaMetadata.artworkUri != null) getBitMapImage(player.mediaMetadata.artworkUri!!) else null
    }

    private suspend fun getBitMapImage(imageUri: Uri) : Bitmap {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUri)
            .build()

        val result = (imageLoader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}