package me.theek.spark.feature.music_player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.components.draggable_state.BottomSheetStates
import me.theek.spark.core.design_system.components.draggable_state.rememberPlayerDraggableState
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.DraggablePlayer
import me.theek.spark.feature.music_player.components.SparkPlayerTopAppBar
import me.theek.spark.feature.music_player.tabs.ArtistsComposable
import me.theek.spark.feature.music_player.tabs.PlaylistComposable
import me.theek.spark.feature.music_player.tabs.SongListComposable
import me.theek.spark.feature.music_player.util.MusicUiTabs
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.util.byteToMB
import me.theek.spark.feature.music_player.util.shareIntent
import me.theek.spark.feature.music_player.util.timeStampToDuration
import me.theek.spark.feature.music_player.viewmodels.MusicListScreenViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel
import me.theek.spark.feature.music_player.viewmodels.SongInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicListScreen(
    onSongServiceStart: () -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    onPlaylistViewClick: (Long) -> Unit,
    musicListViewModel: MusicListScreenViewModel = hiltViewModel(),
    playlistViewModel: PlaylistViewModel = hiltViewModel()
) {
    val currentSelectedSong = musicListViewModel.currentSelectedSong
    val musicListState = musicListViewModel.uiState
    val playlistState by playlistViewModel.uiState.collectAsStateWithLifecycle()
    val artistsDetails by musicListViewModel.artistDetailsStream.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val songInfo = musicListViewModel.songInfo
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val draggableState = rememberPlayerDraggableState(constraintsScope = this)
        val maxHeight = with(LocalDensity.current) { maxHeight.toPx() }
        val maxWidth = with(LocalDensity.current) { maxWidth.toPx() }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .navigationBarsPadding(),
            topBar = {
                SparkPlayerTopAppBar(onSearch = {})
            },
            content = { innerPadding ->
                MusicUi(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(top = innerPadding.calculateTopPadding()),
                    musicListState = musicListState,
                    playlistsState = playlistState,
                    artistsDetails = artistsDetails,
                    shouldOpenCreatePlaylistDialog = playlistViewModel.shouldOpenCreatePlaylistDialog,
                    onCreatePlaylistDismiss = playlistViewModel::onCreatePlaylistDismiss,
                    newPlaylistName = playlistViewModel.newPlaylistName,
                    onNewPlaylistNameChange = playlistViewModel::onNewPlaylistNameChange,
                    onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                    onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                    onSongInfoClick = musicListViewModel::onSongInfoClick,
                    onShareClick = { songPath ->
                       context.startActivity(shareIntent(songPath))
                    },
                    onPlaylistViewClick = onPlaylistViewClick,
                    onPlaylistSave = playlistViewModel::onPlaylistSave,
                    onNavigateToArtistDetailScreen = onNavigateToArtistDetailScreen,
                    scope = scope,
                    onSongClick = {
                        musicListViewModel.onSongClick(it)
                        onSongServiceStart()
                    }
                )
            }
        )

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = currentSelectedSong != null
        ) {
            DraggablePlayer(
                isPlaying = musicListViewModel.isPlaying,
                isFavourite = true,
                repeatState = musicListViewModel.repeatMode,
                progress = musicListViewModel.progress,
                onProgressChange = musicListViewModel::onProgressChange,
                progressString = { musicListViewModel.processString },
                currentSelectedSong = currentSelectedSong!!,
                songDuration = musicListViewModel.duration.toFloat(),
                currentSelectedSongCoverArt = musicListViewModel.currentSelectedSongCover,
                currentSelectedSongPalette = musicListViewModel.currentSelectedSongPalette,
                onPausePlayClick = musicListViewModel::onPausePlayClick,
                onSkipNextClick = musicListViewModel::onSkipNextClick,
                onSkipPreviousClick = musicListViewModel::onSkipPreviousClick,
                onRepeatClick = musicListViewModel::onRepeatModeChange,
                onPlayerMinimizeClick = {
                    scope.launch { draggableState.animateTo(BottomSheetStates.MINIMISED) }
                },
                draggableState = draggableState,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }
    }

    ShowSongInfoBottomBar(
        sheetState = rememberModalBottomSheetState(true),
        songInfo = songInfo,
        onDismissRequest = musicListViewModel::onSongInfoSheetDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowSongInfoBottomBar(
    sheetState: SheetState,
    songInfo: SongInfo,
    onDismissRequest: () -> Unit
) {
    if (songInfo.shouldShowSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(songInfo.song)
                            .memoryCacheKey(songInfo.song?.path)
                            .build(),
                        contentDescription = stringResource(R.string.album_art),
                        error = painterResource(id = R.drawable.round_music_note_24),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth(fraction = 0.9f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Title:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.songName ?: "Unknown",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Artist:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.artistName ?: "Unknown",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Duration:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = timeStampToDuration(songInfo.song?.duration?.toFloat() ?: 0f),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "MIME Type:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.mimeType ?: "Unknown",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Release year:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.releaseYear?.toString() ?: "Unknown",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Location:"
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.path ?: "Unknown",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = "Size:"
                            )

                            val bytesToMB = byteToMB(byte = songInfo.song?.size)

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = if (bytesToMB != null) "${bytesToMB}MB" else "~",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicUi(
    musicListState: UiState<List<Song>>,
    playlistsState: UiState<List<PlaylistData>>,
    artistsDetails: List<ArtistDetails>,
    shouldOpenCreatePlaylistDialog: Boolean,
    onCreatePlaylistDismiss: () -> Unit,
    newPlaylistName: String,
    onNewPlaylistNameChange: (String) -> Unit,
    onPlaylistSave: () -> Unit,
    onSongClick: (Int) -> Unit,
    onPlaylistViewClick: (Long) -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onShareClick: (String) -> Unit,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { MusicUiTabs.size })
    val selectedIndex by remember { derivedStateOf { pagerState.currentPage } }

    Column(modifier = modifier) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            divider = { },
            indicator = {},
            edgePadding = 0.dp,
            selectedTabIndex = selectedIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            MusicUiTabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(page = index) }
                    },
                    text = {
                        Text(
                            text = tab,
                            fontSize = if (selectedIndex == index) MaterialTheme.typography.titleMedium.fontSize else MaterialTheme.typography.bodyMedium.fontSize,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            fontWeight = if (selectedIndex == index) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selectedIndex == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.weight(9.3f),
            state = pagerState
        ) { currentTab ->
            when (currentTab) {
                0 -> { //Tracks
                    SongListComposable(
                        modifier = Modifier.fillMaxSize(),
                        musicListState = musicListState,
                        playlistsState = playlistsState,
                        onSongClick = onSongClick,
                        onCreatePlaylistClick = onCreatePlaylistClick,
                        onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                        onSongInfoClick = onSongInfoClick,
                        onShareClick = onShareClick
                    )
                }

                1 -> { //Albums

                }

                2 -> { //Artists
                    ArtistsComposable(
                        modifier = Modifier.fillMaxSize(),
                        artistsDetails = artistsDetails,
                        onArtistClick = onNavigateToArtistDetailScreen
                    )
                }

                3 -> { // Playlist
                    PlaylistComposable(
                        modifier = Modifier.fillMaxSize(),
                        playlistsState = playlistsState,
                        onPlaylistViewClick = onPlaylistViewClick
                    )
                }

                4 -> { // Favourites

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
                    label = { Text(text = stringResource(R.string.playlist_name)) },
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