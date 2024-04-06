package me.theek.spark.feature.music_player.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.EmptySongComposable
import me.theek.spark.feature.music_player.components.ProgressSongComposable
import me.theek.spark.feature.music_player.components.SongRow
import me.theek.spark.feature.music_player.util.UiState

@Composable
internal fun SongListComposable(
    musicListState: UiState<List<Song>>,
    playlistsState: UiState<List<PlaylistData>>,
    onSongClick: (Int) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onShareClick: (String) -> Unit,
    onShufflePlayClick: () -> Unit,
    onAllPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (musicListState) {
        UiState.Loading -> Unit
        is UiState.Progress -> {
            ProgressSongComposable(
                modifier = Modifier.fillMaxSize(),
                progress = musicListState.progress,
                message = musicListState.status
            )
        }
        is UiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${musicListState.message}")
            }
        }
        is UiState.Success -> {
            if (musicListState.data.isEmpty()) {
                EmptySongComposable(modifier = Modifier.fillMaxSize())
            } else {
                Column(
                    modifier = modifier
                        .background(
                            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        )
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
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Date added",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.weight(3f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FilledTonalIconButton(
                                onClick = onShufflePlayClick,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = rememberShuffle(),
                                    contentDescription = stringResource(R.string.shuffle_icon),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            FilledTonalIconButton(
                                onClick = onAllPlayClick,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = stringResource(me.theek.spark.core.design_system.R.string.play_pause_icon),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                    }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(
                            items = musicListState.data,
                            key = { index, _ -> index }
                        ) { index, song ->
                            SongRow(
                                index = index,
                                song = song,
                                playlistsState = playlistsState,
                                onSongClick = onSongClick,
                                onCreatePlaylistClick = onCreatePlaylistClick,
                                onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                                onSongInfoClick = onSongInfoClick,
                                onShareClick = onShareClick
                            )
                        }
                    }
                }
            }
        }
    }
}