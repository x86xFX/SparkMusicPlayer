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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.icons.rememberShuffle
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.EmptySongComposable
import me.theek.spark.feature.music_player.components.ProgressSongComposable
import me.theek.spark.feature.music_player.components.SongRow
import me.theek.spark.feature.music_player.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SongListComposable(
    musicListState: UiState<List<Song>>,
    playlistState: UiState<List<PlaylistData>>,
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
                val lazyColumnState = rememberLazyListState()
                val scope = rememberCoroutineScope()
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
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
//                        Row(modifier = Modifier.weight(7f)) {
//                            TextButton(onClick = { }) {
//                                Icon(
//                                    modifier = Modifier.size(24.dp),
//                                    imageVector = rememberSort(),
//                                    contentDescription = stringResource(R.string.sort_icon),
//                                    tint = MaterialTheme.colorScheme.onSurface
//                                )
//                                Text(
//                                    text = "Date added",
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
//                                    color = MaterialTheme.colorScheme.onSurface
//                                )
//                            }
//                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.End)
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
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = lazyColumnState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(
                                items = musicListState.data,
                                key = { index, _ -> index }
                            ) { index, song ->
                                SongRow(
                                    index = index,
                                    song = song,
                                    playlistState = playlistState,
                                    onSongClick = onSongClick,
                                    onCreatePlaylistClick = onCreatePlaylistClick,
                                    onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                                    onSongInfoClick = onSongInfoClick,
                                    onShareClick = onShareClick
                                )
                            }
                        }
                        if (lazyColumnState.canScrollBackward) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                                IconButton(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 80.dp)
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                                    onClick = { scope.launch { lazyColumnState.animateScrollToItem(index = 0) } }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(5.dp),
                                        imageVector = Icons.Rounded.KeyboardArrowUp,
                                        contentDescription = stringResource(R.string.to_up),
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}