package me.theek.spark.core.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.theek.spark.core.design_system.R
import me.theek.spark.core.design_system.icons.rememberPause
import me.theek.spark.core.design_system.icons.rememberSkipNext
import me.theek.spark.core.design_system.icons.rememberSkipPrevious

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicPlaybackControls(
    isPlaying: Boolean,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    skipPreviousIconSize: Dp = 30.dp,
    skipNextIconSize: Dp = 30.dp,
    playPauseIconSize: Dp = 30.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            IconButton(
                modifier = Modifier.size(skipPreviousIconSize),
                onClick = onSkipPreviousClick
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = rememberSkipPrevious(),
                    contentDescription = stringResource(R.string.skip_previous_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier.size(playPauseIconSize),
                onClick = onPausePlayClick
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = if (isPlaying) rememberPause() else Icons.Rounded.PlayArrow,
                    contentDescription = stringResource(R.string.play_pause_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                modifier = Modifier.size(skipNextIconSize),
                onClick = onSkipNextClick
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = rememberSkipNext(),
                    contentDescription = stringResource(R.string.skip_next_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}