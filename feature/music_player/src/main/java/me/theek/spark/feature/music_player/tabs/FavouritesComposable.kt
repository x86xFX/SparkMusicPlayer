package me.theek.spark.feature.music_player.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.EmptyFavouritesComposable
import me.theek.spark.feature.music_player.components.SongRow
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.util.shareIntent

@Composable
internal fun FavouritesComposable(
    favouritesState: UiState<List<Song>>,
    playlistState: UiState<List<PlaylistData>>,
    onSongInfoClick: (Song) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongClick: (List<Song>, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    when (favouritesState) {
        is UiState.Progress -> Unit
        is UiState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.something_went_wrong))
            }
        }
        UiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(strokeCap = StrokeCap.Round)
            }
        }
        is UiState.Success -> {
            if (favouritesState.data.isEmpty()) {
                EmptyFavouritesComposable(modifier = Modifier.fillMaxSize())
            }
            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                    )
            ) {
                itemsIndexed(
                    items = favouritesState.data,
                    key = { index, _ -> index }
                ) { index, song ->
                    SongRow(
                        index = index,
                        song = song,
                        playlistState = playlistState,
                        onSongClick = {
                            onSongClick(favouritesState.data, it)
                        },
                        onCreatePlaylistClick = onCreatePlaylistClick,
                        onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                        onSongInfoClick = onSongInfoClick,
                        onShareClick = { songPath ->
                            context.startActivity(shareIntent(songPath))
                        }
                    )
                }
            }
        }
    }
}