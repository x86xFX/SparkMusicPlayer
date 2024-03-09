package me.theek.spark.core.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.theek.spark.core.design_system.R
import me.theek.spark.core.design_system.icons.rememberSkipNext
import me.theek.spark.core.design_system.icons.rememberSkipPrevious

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicPlaybackControls(
    playPauseIcon: @Composable () -> Unit,
    onSkipPreviousClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    skipPreviousIconSize: Dp = 48.dp,
    skipNextIconSize: Dp = 48.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
              PlainTooltip {
                Text(text = "Previous")
              }
            },
            state = rememberTooltipState()
        ) {
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
        }

        Spacer(modifier = Modifier.width(10.dp))

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(text = "Play/Pause")
                }
            },
            state = rememberTooltipState()
        ) {
            playPauseIcon()
        }


        Spacer(modifier = Modifier.width(10.dp))

        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(text = "Next")
                }
            },
            state = rememberTooltipState()
        ) {
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

@Preview(showBackground = true)
@Composable
private fun BasicPlaybackControlsPreview() {
    BasicPlaybackControls(
        onSkipNextClick = {},
        onSkipPreviousClick = {},
        playPauseIcon = {}
    )
}