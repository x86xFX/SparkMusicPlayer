package me.theek.spark.feature.music_player.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.feature.music_player.components.PlayListCard
import me.theek.spark.feature.music_player.components.PlayListCreateComposable
import me.theek.spark.feature.music_player.util.UiState

@Composable
internal fun PlaylistComposable(
    playlistsState: UiState<List<Playlist>>,
    shouldOpenCreatePlaylistDialog: Boolean,
    onCreatePlaylistClick: () -> Unit,
    onCreatePlaylistDismiss: () -> Unit,
    newPlaylistName: String,
    onNewPlaylistNameChange: (String) -> Unit,
    onPlaylistSave: () -> Unit,
    onPlaylistViewClick: (Playlist) -> Unit,
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
                PlayListCreateComposable(onCreatePlaylistClick = onCreatePlaylistClick)
                LazyVerticalGrid(
                    modifier = modifier.padding(12.dp),
                    columns = GridCells.Adaptive(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.Top)
                ) {
                    Log.d("PlaylistComposable", playlistsState.songs.toString())

                    items(
                        items = playlistsState.songs,
                        key = { it.id }
                    ) {
                        PlayListCard(
                            playlist = it,
                            onPlaylistViewClick = onPlaylistViewClick
                        )
                    }
                }
            }
        }
    }

    if (shouldOpenCreatePlaylistDialog) {
        AlertDialog(
            onDismissRequest = onCreatePlaylistDismiss,
            confirmButton = {
                Button(onClick = onPlaylistSave) {
                    Text(text = "Create")
                }
            },
            title = {
                Text(text = "Create playlist")
            },
            text = {
                OutlinedTextField(
                    value = newPlaylistName,
                    onValueChange = onNewPlaylistNameChange,
                    label = { Text(text = "Playlist name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onPlaylistSave() }
                    )
                )
            }
        )
    }
}