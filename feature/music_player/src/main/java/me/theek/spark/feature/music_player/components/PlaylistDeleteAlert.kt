package me.theek.spark.feature.music_player.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.theek.spark.feature.music_player.R

@Composable
fun PlaylistDeleteAlert(
    shouldShowAlert: Boolean,
    onAlertDismiss: () -> Unit,
    onDeletePlaylist: () -> Unit
) {
    if (shouldShowAlert) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            textContentColor = MaterialTheme.colorScheme.onErrorContainer,
            titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
            iconContentColor = MaterialTheme.colorScheme.onErrorContainer,
            onDismissRequest = onAlertDismiss,
            confirmButton = {
                FilledTonalButton(
                    onClick = onDeletePlaylist,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onErrorContainer)
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = onAlertDismiss) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = stringResource(R.string.warning_icon)
                )
            },
            title = {
                Text(text = "Are you sure?")
            },
            text = {
                Text(text = "This playlists will be deleted immediately. You can't undo this action.")
            }
        )
    }
}