package me.theek.spark.feature.music_player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.theek.spark.core.design_system.components.BasicPlaybackControls
import me.theek.spark.core.design_system.icons.rememberQueueMusic
import me.theek.spark.core.design_system.icons.rememberRepeat
import me.theek.spark.core.design_system.icons.rememberRepeatOne
import kotlin.math.floor

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CurrentPlayingSongDetailView(
    isPlaying: Boolean,
    isFavourite: Boolean,
    isOnRepeat: Boolean,
    progress: () -> Float,
    songTitle: String?,
    songArtistName: String?,
    songDuration: Float,
    currentSelectedSongCoverArt: ByteArray?,
    thumbColor: Color,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    onProgressChange: (Float) -> Unit,
    progressString: () -> String,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onPlayerMinimizeClick: () -> Unit,
    onPlayerQueueClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onRepeatClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val sliderColors = SliderDefaults.colors(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = onPlayerMinimizeClick
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.playlist_down_action_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                modifier = Modifier
                    .weight(8f)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = songTitle ?: "Unknown",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = songArtistName ?: "Unknown",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            IconButton(
                modifier = Modifier.weight(1f),
                onClick = onPlayerQueueClick
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = rememberQueueMusic(),
                    contentDescription = stringResource(R.string.player_queue_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        AsyncImage(
            error = painterResource(id = R.drawable.round_music_note_24),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(260.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = currentSelectedSongCoverArt,
            contentDescription = stringResource(R.string.current_playing_song_cover_art)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = progress().div(100).times(songDuration),
                    onValueChange = onProgressChange,
                    thumb = {
                        SliderDefaults.Thumb(
                            interactionSource = interactionSource,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(
                                    start = 4.dp,
                                    top = 4.dp
                                ),
                            colors = sliderColors
                        )
                    },
                    valueRange = 0f..songDuration,
                    colors = sliderColors
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = progressString(),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = timeStampToDuration(songDuration),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = onFavouriteClick
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(R.string.favourite_icon),
                        tint = if (isFavourite) Color(0xFFFF0C6D) else MaterialTheme.colorScheme.onSurface
                    )
                }
                BasicPlaybackControls(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    isPlaying = isPlaying,
                    onSkipPreviousClick = onSkipPreviousClick,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    playPauseIconSize = 70.dp,
                    skipNextIconSize = 40.dp,
                    skipPreviousIconSize = 40.dp
                )
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = onRepeatClick
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = if (isOnRepeat) rememberRepeat() else rememberRepeatOne(),
                        contentDescription = stringResource(R.string.favourite_icon),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun timeStampToDuration(duration: Float) : String {
    val totalSecond = floor(duration / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (duration < 0) "--:--" else "%d:%02d".format(minutes, remainingSeconds)
}