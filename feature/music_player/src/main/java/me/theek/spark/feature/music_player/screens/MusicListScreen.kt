package me.theek.spark.feature.music_player.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.components.draggable_state.BottomSheetStates
import me.theek.spark.core.design_system.components.draggable_state.rememberPlayerDraggableState
import me.theek.spark.core.model.data.Album
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.DraggablePlayer
import me.theek.spark.feature.music_player.components.PlaylistDeleteAlert
import me.theek.spark.feature.music_player.components.SparkPlayerTopAppBar
import me.theek.spark.feature.music_player.tabs.AlbumsComposable
import me.theek.spark.feature.music_player.tabs.ArtistsComposable
import me.theek.spark.feature.music_player.tabs.FavouritesComposable
import me.theek.spark.feature.music_player.tabs.PlaylistComposable
import me.theek.spark.feature.music_player.tabs.SongListComposable
import me.theek.spark.feature.music_player.util.MusicUiTabs
import me.theek.spark.feature.music_player.util.UiState
import me.theek.spark.feature.music_player.util.shareIntent
import me.theek.spark.feature.music_player.viewmodels.FavouriteViewModel
import me.theek.spark.feature.music_player.viewmodels.PlayerViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel

@Composable
fun MusicListScreen(
    playerViewModel: PlayerViewModel,
    playlistViewModel: PlaylistViewModel,
    onSongServiceStart: () -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    onPlaylistViewClick: (Long) -> Unit,
    onAlbumClick: (Int?) -> Unit,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    val playlistState by playlistViewModel.uiState.collectAsStateWithLifecycle()
    val artistsState by playerViewModel.artistDetailsStream.collectAsStateWithLifecycle()
    val favouritesState by favouriteViewModel.uiState.collectAsStateWithLifecycle()
    val currentSelectedSong = playerViewModel.currentSelectedSong
    val musicListState = playerViewModel.uiState
    val context = LocalContext.current
    val orientation = LocalConfiguration.current.orientation
    val scope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.displayCutout)
            .fillMaxSize()
    ) {
        val draggableState = rememberPlayerDraggableState(constraintsScope = this)
        val maxHeight = with(LocalDensity.current) { maxHeight.toPx() }
        val maxWidth = with(LocalDensity.current) { maxWidth.toPx() }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .navigationBarsPadding(),
            topBar = {
                SparkPlayerTopAppBar(
                    isInPlaylistSelectionMode = playlistViewModel.isInSelectionMode,
                    onSearch = {},
                    onPlaylistDelete = playlistViewModel::onPlaylistDeleteWarningShow,
                    onPlaylistSelectionClearClick = playlistViewModel::onPlaylistSelectionClearClick
                )
            },
            content = { innerPadding ->
                MusicUi(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(top = innerPadding.calculateTopPadding()),
                    musicListState = musicListState,
                    playlistState = playlistState,
                    artistsDetails = artistsState,
                    favouritesState = favouritesState,
                    albums = playerViewModel.albumList,
                    onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                    onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                    onSongInfoClick = playerViewModel::onSongInfoClick,
                    onShareClick = { songPath -> context.startActivity(shareIntent(songPath)) },
                    onPlaylistViewClick = onPlaylistViewClick,
                    onNavigateToArtistDetailScreen = onNavigateToArtistDetailScreen,
                    scope = scope,
                    onSongClick = {
                        playerViewModel.onSongClick(it)
                        onSongServiceStart()
                    },
                    onShufflePlayClick = playerViewModel::onAllShuffleClick,
                    onAllPlayClick = { playerViewModel.onSongClick(0) },
                    isInSelectionMode = playlistViewModel.isInSelectionMode,
                    onChangingToSelectionMode = playlistViewModel::onChangingToSelectionMode,
                    onPlaylistAddToSelection = playlistViewModel::onPlaylistAddToSelection,
                    onPlaylistRemoveFromSelection = playlistViewModel::onPlaylistRemoveFromSelection,
                    onAlbumClick = onAlbumClick,
                    onExternalSongClick = playerViewModel::onCustomQueueSongClick
                )
            }
        )

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = currentSelectedSong != null
        ) {
            DraggablePlayer(
                orientation = orientation,
                isPlaying = playerViewModel.isPlaying,
                repeatState = playerViewModel.repeatMode,
                isFavourite = playerViewModel.isFavourite,
                progress = playerViewModel.progress,
                onProgressChange = playerViewModel::onProgressChange,
                progressString = { playerViewModel.processString },
                currentSelectedSong = currentSelectedSong!!,
                songDuration = playerViewModel.duration.toFloat(),
                currentSelectedSongCoverArt = playerViewModel.currentSelectedSongCover,
                currentSelectedSongPalette = playerViewModel.currentSelectedSongPalette,
                onPausePlayClick = playerViewModel::onPausePlayClick,
                onSkipNextClick = playerViewModel::onSkipNextClick,
                onSkipPreviousClick = playerViewModel::onSkipPreviousClick,
                onRepeatClick = playerViewModel::onRepeatModeChange,
                onFavouriteClick = playerViewModel::onFavouriteClick,
                onPlayerMinimizeClick = { scope.launch { draggableState.animateTo(BottomSheetStates.MINIMISED) } },
                onPlayerMaximizeClick = { scope.launch { draggableState.animateTo(BottomSheetStates.EXPANDED) } },
                draggableState = draggableState,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }
    }

    PlaylistDeleteAlert(
        shouldShowAlert = playlistViewModel.shouldShowPlaylistDeletionWarning,
        onAlertDismiss = playlistViewModel::onPlaylistDeleteWarningDismiss,
        onDeletePlaylist = playlistViewModel::onDeletePlaylists
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicUi(
    musicListState: UiState<List<Song>>,
    playlistState: UiState<List<PlaylistData>>,
    favouritesState: UiState<List<Song>>,
    artistsDetails: List<ArtistDetails>,
    albums: List<Album>,
    onSongClick: (Int) -> Unit,
    onExternalSongClick: (List<Song>, Int) -> Unit,
    onPlaylistViewClick: (Long) -> Unit,
    onNavigateToArtistDetailScreen: (ArtistDetails) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onShareClick: (String) -> Unit,
    onShufflePlayClick: () -> Unit,
    onAllPlayClick: () -> Unit,
    isInSelectionMode: Boolean,
    onChangingToSelectionMode: (Long) -> Unit,
    onPlaylistAddToSelection: (Long) -> Unit,
    onPlaylistRemoveFromSelection: (Long) -> Unit,
    onAlbumClick: (Int?) -> Unit,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { MusicUiTabs.size })
    val selectedIndex by remember { derivedStateOf { pagerState.currentPage } }

    Column(modifier = modifier) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            divider = {},
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
                        playlistState = playlistState,
                        onSongClick = onSongClick,
                        onCreatePlaylistClick = onCreatePlaylistClick,
                        onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                        onSongInfoClick = onSongInfoClick,
                        onShareClick = onShareClick,
                        onShufflePlayClick = onShufflePlayClick,
                        onAllPlayClick = onAllPlayClick
                    )
                }

                1 -> { //Albums
                    AlbumsComposable(
                        modifier = Modifier.fillMaxSize(),
                        albums = albums,
                        onAlbumClick = onAlbumClick
                    )
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
                        playlistState = playlistState,
                        onPlaylistViewClick = onPlaylistViewClick,
                        isInSelectionMode = isInSelectionMode,
                        onChangingToSelectionMode = onChangingToSelectionMode,
                        onPlaylistAddToSelection = onPlaylistAddToSelection,
                        onPlaylistRemoveFromSelection = onPlaylistRemoveFromSelection,
                    )
                }

                4 -> { // Favourites
                    FavouritesComposable(
                        modifier = Modifier.fillMaxSize(),
                        favouritesState = favouritesState,
                        playlistState = playlistState,
                        onSongInfoClick = onSongInfoClick,
                        onCreatePlaylistClick = onCreatePlaylistClick,
                        onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                        onSongClick = onExternalSongClick
                    )
                }
            }
        }
    }
}