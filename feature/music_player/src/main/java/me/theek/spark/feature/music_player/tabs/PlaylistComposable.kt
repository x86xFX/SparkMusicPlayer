package me.theek.spark.feature.music_player.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.feature.music_player.components.PlayListCard
import me.theek.spark.feature.music_player.util.UiState

@Composable
internal fun PlaylistComposable(
    playlistsState: UiState<List<PlaylistData>>,
    onPlaylistViewClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (playlistsState) {
        is UiState.Progress, UiState.Loading -> Unit
        is UiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Something went wrong")
            }
        }
        is UiState.Success -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (playlistsState.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Create playlist and add songs")
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = modifier.padding(12.dp),
                        columns = GridCells.Adaptive(150.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.Top)
                    ) {
                        items(
                            items = playlistsState.data,
                            key = { it.playlistId }
                        ) {playlist ->
                            PlayListCard(
                                playlist = playlist,
                                onPlaylistViewClick = onPlaylistViewClick
                            )
                        }
                    }
                }
            }
        }
    }
}