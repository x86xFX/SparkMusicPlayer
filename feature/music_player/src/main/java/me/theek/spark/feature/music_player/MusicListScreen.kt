package me.theek.spark.feature.music_player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.components.draggable_state.BottomSheetStates
import me.theek.spark.core.design_system.components.draggable_state.rememberPlayerDraggableState
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.DraggablePlayer
import me.theek.spark.feature.music_player.components.SparkPlayerTopAppBar
import me.theek.spark.feature.music_player.tabs.ArtistsComposable
import me.theek.spark.feature.music_player.tabs.PlaylistComposable
import me.theek.spark.feature.music_player.tabs.SongListComposable
import me.theek.spark.feature.music_player.util.MusicUiTabs
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.viewmodels.MusicListScreenViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel

@Composable
fun MusicListScreen(
    onSongServiceStart: () -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    musicListViewModel: MusicListScreenViewModel = hiltViewModel(),
    playlistViewModel: PlaylistViewModel = hiltViewModel()
) {
    val currentSelectedSong = musicListViewModel.currentSelectedSong
    val musicListState = musicListViewModel.uiState
    val playlistState by playlistViewModel.uiState.collectAsStateWithLifecycle()
    val artistDetailsStream by musicListViewModel.artistDetailsStream.collectAsStateWithLifecycle()

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
                    artistDetailsStream = artistDetailsStream,
                    shouldOpenCreatePlaylistDialog = playlistViewModel.shouldOpenCreatePlaylistDialog,
                    onCreatePlaylistAlertOpen = playlistViewModel::onCreatePlaylistAlertOpen,
                    onCreatePlaylistDismiss = playlistViewModel::onCreatePlaylistDismiss,
                    newPlaylistName = playlistViewModel.newPlaylistName,
                    onNewPlaylistNameChange = playlistViewModel::onNewPlaylistNameChange,
                    onPlaylistSave = playlistViewModel::onPlaylistSave,
                    onNavigateToArtistDetailScreen = onNavigateToArtistDetailScreen,
                    onSongClick = {
                        musicListViewModel.onSongClick(it)
                        onSongServiceStart()
                    },
                    onPlaylistViewClick = {
                        println(it)
                    }
                )
            }
        )

        LaunchedEffect(key1 = currentSelectedSong) {
            if (currentSelectedSong != null) {
                draggableState.animateTo(BottomSheetStates.MINIMISED)
            }
        }

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
                draggableState = draggableState,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicUi(
    musicListState: UiState<List<Song>>,
    playlistsState: UiState<List<Playlist>>,
    artistDetailsStream: Map<String, ArtistDetails>,
    shouldOpenCreatePlaylistDialog: Boolean,
    onCreatePlaylistAlertOpen: () -> Unit,
    onCreatePlaylistDismiss: () -> Unit,
    newPlaylistName: String,
    onNewPlaylistNameChange: (String) -> Unit,
    onPlaylistSave: () -> Unit,
    onSongClick: (Song) -> Unit,
    onPlaylistViewClick: (Playlist) -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
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
                        onSongClick = onSongClick
                    )
                }

                1 -> { //Albums

                }

                2 -> { //Artists
                    ArtistsComposable(
                        modifier = Modifier.fillMaxSize(),
                        artistDetailsStream = artistDetailsStream,
                        onArtistClick = onNavigateToArtistDetailScreen
                    )
                }

                3 -> { // Playlist
                    PlaylistComposable(
                        modifier = Modifier.fillMaxSize(),
                        playlistsState = playlistsState,
                        shouldOpenCreatePlaylistDialog = shouldOpenCreatePlaylistDialog,
                        onCreatePlaylistClick = onCreatePlaylistAlertOpen,
                        onCreatePlaylistDismiss = onCreatePlaylistDismiss,
                        newPlaylistName = newPlaylistName,
                        onNewPlaylistNameChange = onNewPlaylistNameChange,
                        onPlaylistSave = onPlaylistSave,
                        onPlaylistViewClick = onPlaylistViewClick
                    )
                }

                4 -> { // Favourites

                }
            }
        }
    }
}