package me.theek.spark.feature.music_player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import kotlinx.coroutines.launch
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.CurrentPlayingSongBar
import me.theek.spark.feature.music_player.components.EmptySongComposable
import me.theek.spark.feature.music_player.components.ProgressSongComposable
import me.theek.spark.feature.music_player.components.SongListComposable
import me.theek.spark.feature.music_player.components.SparkPlayerTopAppBar
import me.theek.spark.feature.music_player.util.MusicUiTabs

@Composable
fun MusicListScreen(viewModel: MusicListScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPlayingSong = viewModel.currentPlayingSong
    val currentPlayingSongCover = viewModel.currentPlayingSongCover

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            SparkPlayerTopAppBar(onSearch = {})
        },
        content = { innerPadding ->
            when (val state = uiState) {
                UiState.Loading -> Unit
                is UiState.Progress -> {
                    ProgressSongComposable(
                        modifier = Modifier.fillMaxSize(),
                        progress = state.progress,
                        message = state.status
                    )
                }
                is UiState.Success -> {
                    if (state.songs.isEmpty()) {
                        EmptySongComposable(modifier = Modifier.fillMaxSize())
                    } else {
                        MusicUi(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = innerPadding.calculateTopPadding()),
                            songs = state.songs,
                            imageLoader = viewModel::getSongCoverArt,
                            onSongClick = viewModel::onSongClick,
                            currentPlayingSong = currentPlayingSong,
                            currentPlayingSongCoverArt = currentPlayingSongCover,
                            currentPlayingSongPalette = viewModel.currentPlayingSongPalette,
                            onPausePlayClick = {},
                            onSkipNextClick = {},
                            onSkipPreviousClick = {}
                        )
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicUi(
    songs: List<Song>,
    imageLoader: suspend (String) -> ByteArray?,
    onSongClick: (Song) -> Unit,
    currentPlayingSong: Song?,
    currentPlayingSongCoverArt: ByteArray?,
    currentPlayingSongPalette: Palette?,
    onSkipPreviousClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { MusicUiTabs.size })
    val selectedIndex by remember { derivedStateOf { pagerState.currentPage } }
    val songsList = remember { songs }

    Column(modifier = modifier) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            divider = { },
            edgePadding = 0.dp,
            selectedTabIndex = selectedIndex
        ) {
            MusicUiTabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    },
                    text = {
                        Text(text = tab)
                    }
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.weight(9.3f),
            state = pagerState
        ) { currentTab ->
            when (currentTab) {
                0 -> {
                    SongListComposable(
                        songs = songsList,
                        songRetriever = imageLoader,
                        onSongClick = onSongClick
                    )
                }
                else -> Unit
            }
        }

        AnimatedVisibility(visible = currentPlayingSong != null) {
            CurrentPlayingSongBar(
                modifier = Modifier.weight(0.7f),
                currentPlayingSong = currentPlayingSong!!,
                currentPlayingSongCoverArt = currentPlayingSongCoverArt,
                currentPlayingSongPalette = currentPlayingSongPalette,
                onPausePlayClick = onPausePlayClick,
                onSkipNextClick = onSkipNextClick,
                onSkipPreviousClick = onSkipPreviousClick
            )
        }
    }
}