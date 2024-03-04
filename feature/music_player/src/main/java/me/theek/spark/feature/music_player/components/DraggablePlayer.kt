package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import me.theek.spark.core.design_system.components.BasicPlaybackControls
import me.theek.spark.core.design_system.components.draggable_state.PlayerDraggableState
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.CurrentPlayingSongDetailView
import me.theek.spark.feature.music_player.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DraggablePlayer(
    isPlaying: Boolean,
    isFavourite: Boolean,
    isOnRepeat: Boolean,
    progress: Float,
    progressString: () -> String,
    onProgressChange: (Float) -> Unit,
    currentSelectedSong: Song,
    songDuration: Float,
    currentSelectedSongCoverArt: ByteArray?,
    currentSelectedSongPalette: Palette?,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    draggableState: PlayerDraggableState,
    maxWidth: Float,
    maxHeight: Float,
    modifier: Modifier = Modifier
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val themedSwatch = themedPaletteSwitch(isSystemInDarkTheme = isSystemInDarkTheme, palette = currentSelectedSongPalette)
    val backgroundContainerColor = if (themedSwatch != null) Color(themedSwatch.rgb) else MaterialTheme.colorScheme.secondaryContainer
    val bodyTextColor = if (themedSwatch != null) Color(themedSwatch.bodyTextColor) else MaterialTheme.colorScheme.onSecondaryContainer
    val titleTextColor = if (themedSwatch != null) Color(themedSwatch.titleTextColor) else MaterialTheme.colorScheme.onSecondaryContainer

    val gradientBrush = Brush.linearGradient(
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = maxWidth, y = maxHeight),
        colors = listOf(
            if (isSystemInDarkTheme) Color.Black else Color.White,
            if (isSystemInDarkTheme) Color.Black else Color.White,
            backgroundContainerColor
        ),
        tileMode = TileMode.Clamp
    )


    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = max(draggableState.state.offset.roundToInt(), 0)
                )
            }
            .anchoredDraggable(
                state = draggableState.state,
                orientation = Orientation.Vertical
            )
    ) {

        /**
         * Current Playing music row
         */
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = backgroundContainerColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(start = 15.dp, end = 15.dp, top = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.alpha(
                    (draggableState.state.offset.roundToInt() / maxHeight).coerceIn(
                        0.0F,
                        1.0F
                    )
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    error = painterResource(id = R.drawable.round_music_note_24),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1.2f)
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = currentSelectedSongCoverArt,
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
                        text = currentSelectedSong.songName ?: "Unknown",
                        color = bodyTextColor,
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
                        color = titleTextColor,
                        text = currentSelectedSong.artistName ?: "Unknown",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.SemiBold,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))

                BasicPlaybackControls(
                    modifier = Modifier
                        .weight(3.5f)
                        .fillMaxWidth(),
                    isPlaying = isPlaying,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    onSkipPreviousClick = onSkipPreviousClick
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        /**
         * Current playing song detail screen
         */
        Column(
            modifier = Modifier
                .alpha(
                    if ((1.0 / (abs(
                            max(
                                draggableState.state.offset.roundToInt(),
                                0
                            )
                        ) / 100)).toFloat() > 0.1
                    ) {
                        (1.0 / (abs(max(draggableState.state.offset.roundToInt(), 0)) / 100))
                            .toFloat()
                            .coerceAtMost(1.0F)
                    } else {
                        0.0F
                    }
                )
                .fillMaxSize()
                .background(gradientBrush)
        ) {
            CurrentPlayingSongDetailView(
                isPlaying = isPlaying,
                isFavourite = isFavourite,
                isOnRepeat = isOnRepeat,
                progress = { progress },
                songTitle = currentSelectedSong.songName,
                songArtistName = currentSelectedSong.artistName,
                songDuration = songDuration,
                currentSelectedSongCoverArt = currentSelectedSongCoverArt,
                thumbColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.colorScheme.onBackground,
                inactiveTrackColor = titleTextColor,
                onProgressChange = onProgressChange,
                progressString = progressString,
                onSkipPreviousClick = onSkipPreviousClick,
                onPausePlayClick = onPausePlayClick,
                onSkipNextClick = onSkipNextClick,
                onPlayerMinimizeClick = { /*TODO*/ },
                onPlayerQueueClick = { /*TODO*/ },
                onFavouriteClick = { /*TODO*/ },
                onRepeatClick = { /*TODO*/ }
            )
        }
    }
}

@Composable
private fun themedPaletteSwitch(isSystemInDarkTheme: Boolean, palette: Palette?): Palette.Swatch? {
    return if (isSystemInDarkTheme) {
        palette?.darkVibrantSwatch
    } else {
        palette?.vibrantSwatch
    }
}