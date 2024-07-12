package me.theek.spark.feature.music_player.components.draggable_player

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.theek.spark.core.model.data.Song
import me.theek.spark.core.player.RepeatMode
import me.theek.spark.feature.music_player.util.themedPaletteSwitch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
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
    val density = LocalDensity.current
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val rowHeight = 43.dp + statusBarPadding + navigationBarPadding

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {

        val draggableState = rememberDraggablePlayerState(
            density = density,
            constraintsScope = this,
            rowHeight = rowHeight
        )

        Column(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = 0,
                        y = draggableState
                            .state
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(
                    state = draggableState.state,
                    orientation = Orientation.Vertical
                )
        ) {
            AnimatedVisibility(visible = draggableState.state.currentValue == DragAnchors.MINIMIZED) {
                DraggableSongRow(
                    modifier = Modifier
                        .height(rowHeight)
                        .fillMaxWidth()
                        .background(backgroundContainerColor)
                        .displayCutoutPadding()
                        .navigationBarsPadding()
                        .padding(
                            top = if (draggableState.state.currentValue == DragAnchors.EXPANDED) statusBarPadding else 0.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                    isPlaying = isPlaying,
                    currentSelectedSong = currentSelectedSong,
                    onPausePlayClick = onPausePlayClick,
                    onSkipNextClick = onSkipNextClick,
                    onSkipPreviousClick = onSkipPreviousClick,
                    bodyTextColor = bodyTextColor,
                    titleTextColor = titleTextColor
                )
            }

            when (orientation) {
                ORIENTATION_PORTRAIT -> {
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
                        backgroundBrush = gradientBrush,
                        onProgressChange = onProgressChange,
                        progressString = progressString,
                        onSkipPreviousClick = onSkipPreviousClick,
                        onPausePlayClick = onPausePlayClick,
                        onSkipNextClick = onSkipNextClick,
                        onPlayerMinimizeClick = {
                            scope.launch {
                                draggableState.animateTo(DragAnchors.MINIMIZED)
                            }
                        },
                        onPlayerQueueClick = { onGetCurrentQueue() },
                        onFavouriteClick = onFavouriteClick,
                        onRepeatClick = onRepeatClick,
                    )
                }

                ORIENTATION_LANDSCAPE -> {
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
                        backgroundBrush = gradientBrush,
                        onProgressChange = onProgressChange,
                        progressString = progressString,
                        onSkipPreviousClick = onSkipPreviousClick,
                        onPausePlayClick = onPausePlayClick,
                        onSkipNextClick = onSkipNextClick,
                        onPlayerMinimizeClick = {
                            scope.launch {
                                draggableState.animateTo(DragAnchors.MINIMIZED)
                            }
                        },
                        onPlayerQueueClick = { onGetCurrentQueue() },
                        onFavouriteClick = onFavouriteClick,
                        onRepeatClick = onRepeatClick,
                    )
                }
            }
        }
    }
}