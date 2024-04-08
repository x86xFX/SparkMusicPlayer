package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
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
import me.theek.spark.core.design_system.components.GenreTag
import me.theek.spark.core.design_system.icons.rememberMotionPhotosAuto
import me.theek.spark.core.design_system.icons.rememberPauseCircle
import me.theek.spark.core.design_system.icons.rememberPlayCircle
import me.theek.spark.core.design_system.icons.rememberQueueMusic
import me.theek.spark.core.design_system.icons.rememberRepeat
import me.theek.spark.core.design_system.icons.rememberRepeatOne
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.player.RepeatMode
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.util.timeStampToDuration

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun CurrentPlayingSongDetailView(
    song: Song,
    isPlaying: Boolean,
    repeatState: @RepeatMode Int,
    progress: () -> Float,
    isFavourite: Boolean,
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
    onFavouriteClick: (Long, Boolean) -> Unit,
    onRepeatClick: (@RepeatMode Int) -> Unit
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
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = "Minimize")
                    }
                },
                state = rememberTooltipState()
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
                    text = song.songName ?: "Unknown",
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
                    text = song.artistName ?: "Unknown",
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

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = "Queue")
                    }
                },
                state = rememberTooltipState()
            ) {
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
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(260.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(id = R.drawable.round_music_note_24),
                contentScale = ContentScale.Crop,
                model = currentSelectedSongCoverArt,
                contentDescription = stringResource(R.string.current_playing_song_cover_art)
            )

            if (song.genres.isNotEmpty()) {
                GenreTag(
                    modifier = Modifier.padding(top = 15.dp),
                    name = song.genres.joinToString()
                )
            }
        }


        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(fraction = 0.8f),
            verticalArrangement = Arrangement.Top,
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

            Row(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = "Favourite")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onFavouriteClick(song.id, !isFavourite) }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.favourite_icon),
                            tint = if (isFavourite) Color(0xFFFF0C6D) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                BasicPlaybackControls(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    onSkipPreviousClick = onSkipPreviousClick,
                    onSkipNextClick = onSkipNextClick,
                    skipNextIconSize = 40.dp,
                    skipPreviousIconSize = 40.dp,
                    playPauseIcon = {
                        IconButton(
                            modifier = Modifier.size(60.dp),
                            onClick = onPausePlayClick
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = if (isPlaying) rememberPauseCircle() else rememberPlayCircle(),
                                contentDescription = stringResource(me.theek.spark.core.design_system.R.string.play_pause_icon),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = "Repeat")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    when (repeatState) {
                        RepeatMode.REPEAT_MODE_ALL -> {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable { onRepeatClick(RepeatMode.REPEAT_MODE_ONE) },
                                imageVector =  rememberRepeat(),
                                contentDescription = stringResource(R.string.repeat_all_icon),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        RepeatMode.REPEAT_MODE_ONE -> {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable { onRepeatClick(RepeatMode.REPEAT_MODE_OFF) },
                                imageVector =  rememberRepeatOne(),
                                contentDescription = stringResource(R.string.repeat_one_icon),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        else -> {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable { onRepeatClick(RepeatMode.REPEAT_MODE_ALL) },
                                imageVector =  rememberMotionPhotosAuto(),
                                contentDescription = stringResource(R.string.repeat_off_icon),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}