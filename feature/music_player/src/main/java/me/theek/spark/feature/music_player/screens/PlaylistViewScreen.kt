package me.theek.spark.feature.music_player.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.SongRow
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.util.shareIntent
import me.theek.spark.feature.music_player.viewmodels.PlaylistPlayerViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistViewScreen(
    playlistViewModel: PlaylistViewModel,
    playlistId: Long?,
    onNavigateBackClick: () -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongClick: (List<Song>, Int) -> Unit
) {
    if (playlistId == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Something went wrong")
            Button(onClick = onNavigateBackClick) {
                Text(text = "Go back")
            }
        }
    } else {
        val viewModel = hiltViewModel<PlaylistPlayerViewModel, PlaylistPlayerViewModel.PlaylistPlayerViewModelFactory> { factory ->
            factory.create(playlistId)
        }
        val context = LocalContext.current
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val playlistState by playlistViewModel.uiState.collectAsStateWithLifecycle()

        when (val state = uiState) {
            is UiState.Progress, is UiState.Failure -> Unit
            UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(strokeCap = StrokeCap.Round)
                }
            }

            is UiState.Success -> {
                Box(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.displayCutout)
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color.Black),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.data.songs[0])
                            .memoryCacheKey(state.data.songs[0].path)
                            .diskCacheKey(state.data.songs[0].path)
                            .build(),
                        placeholder = painterResource(id = R.drawable.artist_placeholder),
                        error = painterResource(id = R.drawable.artist_placeholder),
                        contentDescription = stringResource(R.string.playlist_image),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(horizontal = 20.dp, vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            onClick = onNavigateBackClick
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = stringResource(R.string.navigate_to_artist_list_page),
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxHeight(fraction = 0.65f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStartPercent = 10, topEndPercent = 10))
                            .background(color = MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .basicMarquee()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp
                                    ),
                                text = state.data.playlistName,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontWeight = FontWeight.SemiBold,
                                overflow = TextOverflow.Clip
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp
                                    ),
                                text = "Created ${state.data.playlistCreatedAt}",
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                overflow = TextOverflow.Clip
                            )

                            LazyColumn(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .navigationBarsPadding()
                                    .fillMaxWidth()
                            ) {
                                itemsIndexed(
                                    items = state.data.songs,
                                    key = { index, _ -> index }
                                ) { index, song ->
                                    SongRow(
                                        index = index,
                                        song = song,
                                        playlistState = playlistState,
                                        onSongClick = {
                                            onSongClick(state.data.songs ,it)
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
            }
        }
    }
}