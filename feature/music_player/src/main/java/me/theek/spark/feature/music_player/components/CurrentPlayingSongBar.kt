package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import me.theek.spark.core.design_system.icons.rememberSkipNext
import me.theek.spark.core.design_system.icons.rememberSkipPrevious
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CurrentPlayingSongBar(
    currentPlayingSong: Song,
    currentPlayingSongCoverArt: ByteArray?,
    currentPlayingSongPalette: Palette?,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themedSwatch = themedPaletteSwitch(palette = currentPlayingSongPalette)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(
                color = if (themedSwatch != null) Color(themedSwatch.rgb) else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            error = painterResource(id = R.drawable.round_music_note_24),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1.2f)
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = currentPlayingSongCoverArt,
            contentDescription = stringResource(R.string.current_playing_song_cover_art)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(5.3f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
                text = currentPlayingSong.songName ?: "Unknown",
                color = if (themedSwatch != null) Color(themedSwatch.bodyTextColor) else MaterialTheme.colorScheme.onSecondaryContainer,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = if (themedSwatch != null) Color(themedSwatch.titleTextColor) else MaterialTheme.colorScheme.onSecondaryContainer,
                text = currentPlayingSong.artistName ?: "Unknown",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
        Spacer(modifier = Modifier.width(2.dp))

        PlayerControls(
            modifier = Modifier
                .weight(3.5f)
                .fillMaxWidth(),
            onPausePlayClick = onPausePlayClick,
            onSkipNextClick = onSkipNextClick,
            onSkipPreviousClick = onSkipPreviousClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerControls(
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            IconButton(onClick = onSkipPreviousClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = rememberSkipPrevious(),
                    contentDescription = stringResource(R.string.skip_previous_icon)
                )
            }
            IconButton(onClick = onPausePlayClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = stringResource(id = R.string.play_icon)
                )
            }
            IconButton(onClick = onSkipNextClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = rememberSkipNext(),
                    contentDescription = stringResource(R.string.skip_next_icon)
                )
            }
        }
    }
}

@Composable
private fun themedPaletteSwitch(palette: Palette?): Palette.Swatch? {
    return if (isSystemInDarkTheme()) {
        palette?.darkVibrantSwatch
    } else {
        palette?.lightVibrantSwatch
    }
}