package me.theek.spark.feature.music_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.theek.spark.feature.music_player.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SparkPlayerTopAppBar(
    isInPlaylistSelectionMode: Boolean,
    onPlaylistDelete: () -> Unit,
    onPlaylistSelectionClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        title = {
            Text(text = "Spark Player")
        },
        actions = {
            AnimatedVisibility(visible = isInPlaylistSelectionMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onPlaylistDelete) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete_icon)
                        )
                    }

                    IconButton(onClick = onPlaylistSelectionClearClick) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = stringResource(R.string.close_icon)
                        )
                    }
                }
            }
        }
    )
}