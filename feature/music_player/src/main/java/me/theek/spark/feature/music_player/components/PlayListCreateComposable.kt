package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.theek.spark.core.design_system.icons.rememberPlaylistAdd
import me.theek.spark.feature.music_player.R

@Composable
internal fun PlayListCreateComposable(
    modifier: Modifier = Modifier,
    onCreatePlaylistClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onCreatePlaylistClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = rememberPlaylistAdd(),
                contentDescription = stringResource(R.string.create_playlist_icon)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Create song playlist")
        }
    }
}

@Preview
@Composable
private fun PlayListCreateComposablePreview() {
    PlayListCreateComposable(
        onCreatePlaylistClick = {}
    )
}