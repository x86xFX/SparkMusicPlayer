package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.theek.spark.core.design_system.icons.rememberShuffle
import me.theek.spark.core.design_system.icons.rememberSort
import me.theek.spark.core.model.data.Audio
import me.theek.spark.feature.music_player.R

@Composable
internal fun SongListUi(
    songs: List<Audio>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(7f)) {
                TextButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = rememberSort(),
                        contentDescription = stringResource(R.string.sort_icon),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Date added",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier.weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledTonalIconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = rememberShuffle(),
                        contentDescription = stringResource(R.string.shuffle_icon),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                FilledTonalIconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(R.string.play_icon),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(
                items = songs,
                key = { it.id }
            ) {
                SongRow(song = it)
            }
        }
    }
}