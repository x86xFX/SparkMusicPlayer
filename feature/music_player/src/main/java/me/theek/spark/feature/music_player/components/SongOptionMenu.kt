package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.saket.cascade.CascadeDropdownMenu
import me.theek.spark.core.design_system.icons.rememberPlaylistAdd
import me.theek.spark.core.design_system.icons.rememberQueueMusic
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.util.UiState
import androidx.compose.material3.DropdownMenuItem as MaterialDropDownMenuItem

@Composable
internal fun SongOptionMenu(
    song: Song,
    playlistsState: UiState<List<PlaylistData>>,
    onCreatePlaylistClick: () -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onShareClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var shouldExpandDropDownMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { shouldExpandDropDownMenu = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = stringResource(R.string.song_option_icon),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Box {
            CascadeDropdownMenu(
                expanded = shouldExpandDropDownMenu,
                onDismissRequest = { shouldExpandDropDownMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Add to playlist")
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = rememberPlaylistAdd(),
                            contentDescription = null
                        )
                    },
                    children = {
                        MaterialDropDownMenuItem(
                            text = {
                                Text(
                                    text = "Create playlist",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onCreatePlaylistClick()
                                shouldExpandDropDownMenu = false
                            }
                        )

                        when (playlistsState) {
                            is UiState.Failure, is UiState.Progress, UiState.Loading -> Unit
                            is UiState.Success -> {
                                if (playlistsState.data.isNotEmpty()) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 100.dp, max = 250.dp)
                                    ) {
                                        itemsIndexed(
                                            items = playlistsState.data,
                                            key = { index, _ -> index }
                                        ) { _, playlist ->
                                            MaterialDropDownMenuItem(
                                                text = {
                                                    Text(
                                                        text = playlist.playlistName,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                },
                                                leadingIcon = {
                                                    Icon(
                                                        modifier = Modifier.size(26.dp),
                                                        imageVector = rememberQueueMusic(),
                                                        contentDescription = null
                                                    )
                                                },
                                                onClick = {
                                                    onAddToExistingPlaylistClick(
                                                        Pair(
                                                            first = playlist.playlistId,
                                                            second = song.id
                                                        )
                                                    )
                                                    shouldExpandDropDownMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
                MaterialDropDownMenuItem(
                    text = {
                        Text(
                            text = "Song info",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onSongInfoClick(song)
                        shouldExpandDropDownMenu = false
                    }
                )
                MaterialDropDownMenuItem(
                    text = {
                        Text(
                            text = "Share",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onShareClick(song.path)
                        shouldExpandDropDownMenu = false
                    }
                )
            }
        }
    }
}