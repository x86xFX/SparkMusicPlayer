package me.theek.spark.feature.music_player.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.components.BasicPlaybackControls
import me.theek.spark.core.design_system.components.draggable_state.BottomSheetStates
import me.theek.spark.core.design_system.components.draggable_state.PlayerDraggableState
import me.theek.spark.core.design_system.components.draggable_state.rememberPlayerDraggableState
import me.theek.spark.core.design_system.icons.rememberPause
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.player.RepeatMode
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.util.themedPaletteSwitch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DraggablePlayer(
    scope: CoroutineScope,
    orientation: Int,
    isPlaying: Boolean,
    repeatState: @RepeatMode Int,
    isFavourite: Boolean,
    progress: Float,
    progressString: () -> String,
    onProgressChange: (Float) -> Unit,
    currentSelectedSong: Song,
    songDuration: Float,
    currentSelectedSongPalette: Palette?,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onRepeatClick: (@RepeatMode Int) -> Unit,
    onFavouriteClick: (Long, Boolean) -> Unit,
    onGetCurrentQueue: () -> List<Song>,
    modifier: Modifier = Modifier
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val themedSwatch = themedPaletteSwitch(
        isSystemInDarkTheme = isSystemInDarkTheme,
        palette = currentSelectedSongPalette
    )
    val backgroundContainerColor = if (themedSwatch != null) Color(themedSwatch.rgb) else MaterialTheme.colorScheme.secondaryContainer
    val bodyTextColor = if (themedSwatch != null) Color(themedSwatch.bodyTextColor) else MaterialTheme.colorScheme.onSecondaryContainer
    val titleTextColor = if (themedSwatch != null) Color(themedSwatch.titleTextColor) else MaterialTheme.colorScheme.onSecondaryContainer
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            if (isSystemInDarkTheme) Color.Black else Color.White,
            if (isSystemInDarkTheme) Color.Black else Color.White,
            backgroundContainerColor
        ),
        tileMode = TileMode.Clamp
    )
    var shouldShowCurrentQueue by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val navPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val draggableState = rememberPlayerDraggableState(constraintsScope = this)
        val maxHeight = with(LocalDensity.current) { maxHeight.toPx() }

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                DraggablePlayerPortrait(
                    modifier = modifier,
                    maxHeight = maxHeight,
                    maxDp = this.maxHeight,
                    navPadding = navPadding,
                    isPlaying = isPlaying,
                    repeatState = repeatState,
                    isFavourite = isFavourite,
                    progress = progress,
                    progressString = progressString,
                    onProgressChange = onProgressChange,
                    currentSelectedSong = currentSelectedSong,
                    songDuration = songDuration,
                    onSkipPreviousClick = onSkipPreviousClick,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    onRepeatClick = onRepeatClick,
                    onPlayerMinimizeClick = {
                        scope.launch {
                            draggableState.animateTo(
                                BottomSheetStates.MINIMISED
                            )
                        }
                    },
                    onPlayerMaximizeClick = {
                        scope.launch {
                            draggableState.animateTo(
                                BottomSheetStates.EXPANDED
                            )
                        }
                    },
                    onPlayerQueueClick = { shouldShowCurrentQueue = true },
                    onFavouriteClick = onFavouriteClick,
                    draggableState = draggableState,
                    backgroundContainerColor = backgroundContainerColor,
                    bodyTextColor = bodyTextColor,
                    titleTextColor = titleTextColor,
                    gradientBrush = gradientBrush
                )
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                DraggablePlayerLandscape(
                    modifier = modifier,
                    maxHeight = maxHeight,
                    maxDp = this.maxHeight,
                    isPlaying = isPlaying,
                    repeatState = repeatState,
                    isFavourite = isFavourite,
                    progress = progress,
                    progressString = progressString,
                    onProgressChange = onProgressChange,
                    currentSelectedSong = currentSelectedSong,
                    songDuration = songDuration,
                    onSkipPreviousClick = onSkipPreviousClick,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    onRepeatClick = onRepeatClick,
                    onPlayerMinimizeClick = {
                        scope.launch {
                            draggableState.animateTo(
                                BottomSheetStates.MINIMISED
                            )
                        }
                    },
                    onPlayerMaximizeClick = {
                        scope.launch {
                            draggableState.animateTo(
                                BottomSheetStates.EXPANDED
                            )
                        }
                    },
                    onPlayerQueueClick = {
                        shouldShowCurrentQueue = true
                    },
                    onFavouriteClick = onFavouriteClick,
                    draggableState = draggableState,
                    backgroundContainerColor = backgroundContainerColor,
                    bodyTextColor = bodyTextColor,
                    titleTextColor = titleTextColor,
                    gradientBrush = gradientBrush
                )
            }
        }
    }
    if (shouldShowCurrentQueue) {

        ModalBottomSheet(
            onDismissRequest = { shouldShowCurrentQueue = false }
        ) {
            LazyColumn {
                itemsIndexed(
                    items = onGetCurrentQueue(),
                    key = { index, _ -> index }
                ) { _, song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(song)
                                .memoryCacheKey(song.path)
                                .diskCacheKey(song.path)
                                .build(),
                            contentDescription = stringResource(R.string.album_art),
                            error = painterResource(id = R.drawable.round_music_note_24),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Column(
                            modifier = Modifier
                                .weight(7f)
                                .padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = song.songName ?: "Unknown",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = song.artistName ?: "Unknown",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DraggablePlayerPortrait(
    maxHeight: Float,
    maxDp: Dp,
    navPadding: Dp,
    isPlaying: Boolean,
    repeatState: @RepeatMode Int,
    isFavourite: Boolean,
    progress: Float,
    progressString: () -> String,
    onProgressChange: (Float) -> Unit,
    currentSelectedSong: Song,
    songDuration: Float,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onRepeatClick: (@RepeatMode Int) -> Unit,
    onPlayerMinimizeClick: () -> Unit,
    onPlayerMaximizeClick: () -> Unit,
    onPlayerQueueClick: () -> Unit,
    onFavouriteClick: (Long, Boolean) -> Unit,
    draggableState: PlayerDraggableState,
    backgroundContainerColor: Color,
    bodyTextColor: Color,
    titleTextColor: Color,
    gradientBrush: Brush,
    modifier: Modifier = Modifier
) {
    val calc = remember(maxHeight) {
        maxHeight.roundToInt().minus(maxDp.value.roundToInt()).div(2).coerceAtMost(375)
    }
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = max(
                        if (draggableState.state.offset.roundToInt() >= 3000) {
                            draggableState.state.offset.roundToInt() - calc
                        } else {
                            draggableState.state.offset.roundToInt() - (calc - navPadding
                                .roundToPx()
                                .times(2))
                        },
                        0
                    )
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
                .clip(RoundedCornerShape(22.dp))
                .background(color = backgroundContainerColor)
                .clickable { onPlayerMaximizeClick() }
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
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
                Row(
                    modifier = Modifier.weight(4f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AsyncImage(
                            error = painterResource(id = R.drawable.round_music_note_24),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentSelectedSong)
                                .memoryCacheKey(currentSelectedSong.path)
                                .diskCacheKey(currentSelectedSong.path)
                                .build(),
                            contentDescription = stringResource(R.string.current_playing_song_cover_art)
                        )
                    }
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
                }

                Spacer(modifier = Modifier.width(2.dp))

                BasicPlaybackControls(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    onSkipNextClick = onSkipNextClick,
                    onSkipPreviousClick = onSkipPreviousClick,
                    skipPreviousIconSize = 28.dp,
                    skipNextIconSize = 28.dp,
                    tint = bodyTextColor,
                    horizontalArrangement = Arrangement.End,
                    playPauseIcon = {
                        IconButton(
                            modifier = Modifier.size(28.dp),
                            onClick = onPausePlayClick
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = if (isPlaying) rememberPause() else Icons.Rounded.PlayArrow,
                                contentDescription = stringResource(me.theek.spark.core.design_system.R.string.play_pause_icon),
                                tint = bodyTextColor
                            )
                        }
                    }
                )
            }
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
            CurrentPlayingSongDetailPortraitView(
                song = currentSelectedSong,
                isPlaying = isPlaying,
                repeatState = repeatState,
                isFavourite = isFavourite,
                progress = { progress },
                songDuration = songDuration,
                thumbColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.colorScheme.onBackground,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                onProgressChange = onProgressChange,
                progressString = progressString,
                onSkipPreviousClick = onSkipPreviousClick,
                onPausePlayClick = onPausePlayClick,
                onSkipNextClick = onSkipNextClick,
                onPlayerMinimizeClick = onPlayerMinimizeClick,
                onPlayerQueueClick = onPlayerQueueClick,
                onFavouriteClick = onFavouriteClick,
                onRepeatClick = onRepeatClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DraggablePlayerLandscape(
    maxHeight: Float,
    maxDp: Dp,
    isPlaying: Boolean,
    repeatState: @RepeatMode Int,
    isFavourite: Boolean,
    progress: Float,
    progressString: () -> String,
    onProgressChange: (Float) -> Unit,
    currentSelectedSong: Song,
    songDuration: Float,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onRepeatClick: (@RepeatMode Int) -> Unit,
    onPlayerMinimizeClick: () -> Unit,
    onPlayerMaximizeClick: () -> Unit,
    onPlayerQueueClick: () -> Unit,
    onFavouriteClick: (Long, Boolean) -> Unit,
    draggableState: PlayerDraggableState,
    backgroundContainerColor: Color,
    bodyTextColor: Color,
    titleTextColor: Color,
    gradientBrush: Brush,
    modifier: Modifier = Modifier
) {
    val calc = remember(maxHeight) { maxHeight.roundToInt().minus(maxDp.value.roundToInt()).div(2) }
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = max(draggableState.state.offset.roundToInt() - calc, 0)
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
                .navigationBarsPadding()
                .displayCutoutPadding()
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(color = backgroundContainerColor)
                .clickable { onPlayerMaximizeClick() }
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
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
                Row(
                    modifier = Modifier.weight(8.5f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        AsyncImage(
                            error = painterResource(id = R.drawable.round_music_note_24),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentSelectedSong)
                                .memoryCacheKey(currentSelectedSong.path)
                                .diskCacheKey(currentSelectedSong.path)
                                .build(),
                            contentDescription = stringResource(R.string.current_playing_song_cover_art)
                        )
                    }
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
                }

                Spacer(modifier = Modifier.width(2.dp))

                BasicPlaybackControls(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxWidth(),
                    onSkipNextClick = onSkipNextClick,
                    onSkipPreviousClick = onSkipPreviousClick,
                    skipPreviousIconSize = 28.dp,
                    skipNextIconSize = 28.dp,
                    tint = bodyTextColor,
                    horizontalArrangement = Arrangement.End,
                    playPauseIcon = {
                        IconButton(
                            modifier = Modifier.size(28.dp),
                            onClick = onPausePlayClick
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = if (isPlaying) rememberPause() else Icons.Rounded.PlayArrow,
                                contentDescription = stringResource(me.theek.spark.core.design_system.R.string.play_pause_icon),
                                tint = bodyTextColor
                            )
                        }
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = draggableState.state.requireOffset().roundToInt()
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .alpha(
                        if ((1.0 / (abs(draggableState.state.offset) / 70)).toFloat() > 0.1) {
                            (1.0 / (abs(draggableState.state.offset) / 70))
                                .toFloat()
                                .coerceAtMost(1.0F)
                        } else {
                            0.0F
                        }
                    )
                    .fillMaxSize()
                    .background(gradientBrush)
            ) {
                CurrentPlayingSongDetailLandscapeView(
                    song = currentSelectedSong,
                    isPlaying = isPlaying,
                    repeatState = repeatState,
                    isFavourite = isFavourite,
                    progress = { progress },
                    songDuration = songDuration,
                    thumbColor = MaterialTheme.colorScheme.onBackground,
                    activeTrackColor = MaterialTheme.colorScheme.onBackground,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    onProgressChange = onProgressChange,
                    progressString = progressString,
                    onSkipPreviousClick = onSkipPreviousClick,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    onPlayerMinimizeClick = onPlayerMinimizeClick,
                    onPlayerQueueClick = onPlayerQueueClick,
                    onFavouriteClick = onFavouriteClick,
                    onRepeatClick = onRepeatClick,
                )
            }
        }
    }
}