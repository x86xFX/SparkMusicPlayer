package me.theek.spark.feature.music_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
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
import me.theek.spark.core.design_system.components.GenreTag
import me.theek.spark.core.design_system.icons.rememberSignalWifiStatusBarNotConnected
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.SongRow
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.util.shareIntent
import me.theek.spark.feature.music_player.viewmodels.ArtistDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistDetails: ArtistDetails,
    onNavigateBackClick: () -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongClick: (List<Song>, Int) -> Unit
) {
    val viewModel = hiltViewModel<ArtistDetailViewModel, ArtistDetailViewModel.ArtistDetailViewModelFactory> { factory ->
        factory.create(artistDetails.artistName)
    }

    val artistRemoteState by viewModel.artistRemoteDetails.collectAsStateWithLifecycle()
    val artistSongState by viewModel.artistSongState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black),
            model = ImageRequest.Builder(LocalContext.current)
                .data(artistDetails)
                .memoryCacheKey(artistDetails.artistName)
                .diskCacheKey(artistDetails.artistName)
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

            when (val state = artistRemoteState) {
                is UiState.Failure, UiState.Loading, is UiState.Progress -> Unit
                is UiState.Success -> {
                    if (state.data.externalArtistProfile.isNotBlank()) {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text(text = stringResource(R.string.artist_s_spotify_profile))
                                }
                            },
                            state = rememberTooltipState()
                        ) {
                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = { uriHandler.openUri(state.data.externalArtistProfile) }
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.spotify_icon),
                                    contentDescription = stringResource(R.string.artist_spotify_profile),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(fraction = 0.65f)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStartPercent = 10,
                        topEndPercent = 10
                    )
                )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp
                        ),
                    text = artistDetails.artistName,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Clip
                )

                when (val state = artistRemoteState) {
                    is UiState.Progress -> Unit
                    is UiState.Failure -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = viewModel::getArtistRemoteDetails
                            ) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    imageVector = rememberSignalWifiStatusBarNotConnected(),
                                    contentDescription = stringResource(R.string.no_internet_icon),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    modifier = Modifier.padding(start = 8.dp),
                                    text = "Retry"
                                )
                            }

                        }
                    }
                    UiState.Loading -> {
                        println("Loading...")
                    }
                    is UiState.Success -> {
                        if (state.data.artistGenres.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                itemsIndexed(
                                    items = state.data.artistGenres,
                                    key = { index, _ -> index }
                                ) { _, genre ->
                                    GenreTag(name = genre)
                                }
                            }
                        }
                    }
                }
            }

            when (val artistData = artistSongState) {
                is UiState.Progress -> Unit
                is UiState.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = artistData.message ?: "Something went wrong.")
                    }
                }
                UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeCap = StrokeCap.Round)
                    }
                }
                is UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .navigationBarsPadding()
                            .fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = artistData.data,
                            key = { index, _ -> index }
                        ) { index, song ->
                            SongRow(
                                index = index,
                                song = song,
                                playlistsState = UiState.Loading, // Make sure to fix this
                                onSongClick = {
                                    onSongClick(artistData.data, it)
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