package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.theek.spark.feature.music_player.R

@Composable
internal fun EmptyPlaylistComposable(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_headphone),
            contentDescription = stringResource(R.string.playlist_icon)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Create playlist and add songs",
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
    }
}