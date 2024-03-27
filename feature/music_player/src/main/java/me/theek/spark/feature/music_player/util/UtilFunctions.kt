package me.theek.spark.feature.music_player.util

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import kotlin.math.floor

internal fun shareIntent(value: String): Intent {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, value.toUri())
        type = "audio/*"
    }
    return Intent.createChooser(sendIntent, "Share song with")
}

@Composable
internal fun themedPaletteSwitch(isSystemInDarkTheme: Boolean, palette: Palette?): Palette.Swatch? {
    return if (isSystemInDarkTheme) {
        palette?.darkVibrantSwatch
    } else {
        palette?.vibrantSwatch
    }
}

internal fun timeStampToDuration(duration: Float): String {
    val totalSecond = floor(duration / 1E3).toInt()
    val hours = totalSecond / 3600  // Divide by 3600 for hours instead of minutes
    val remainingMinutes = (totalSecond % 3600) / 60 // Get remaining minutes after calculating hours
    val remainingSeconds = totalSecond - (hours * 3600) - (remainingMinutes * 60)

    return if (duration < 0) {
        "--:--"
    } else if (hours > 0) {
        "%d:%02d:%02d".format(hours, remainingMinutes, remainingSeconds)
    } else {
        "%d:%02d".format(remainingMinutes, remainingSeconds)
    }
}

internal fun byteToMB(byte: Long?): Long? {
    return byte?.div(1000)?.div(1000)
}