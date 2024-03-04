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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.components.draggable_state.BottomSheetStates
import me.theek.spark.core.design_system.components.draggable_state.rememberPlayerDraggableState
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.DraggablePlayer
import me.theek.spark.feature.music_player.components.EmptySongComposable
import me.theek.spark.feature.music_player.components.ProgressSongComposable
import me.theek.spark.feature.music_player.components.SongListComposable
import me.theek.spark.feature.music_player.components.SparkPlayerTopAppBar
import me.theek.spark.feature.music_player.util.MusicUiTabs

@Composable
fun MusicListScreen(
    viewModel: MusicListScreenViewModel,
    onSongClick: (Pair<Int, Song>) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentSelectedSong = viewModel.currentSelectedSong

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
                                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                                    .padding(top = innerPadding.calculateTopPadding()),
                                songs = state.songs,
                                imageLoader = viewModel::getSongCoverArt,
                                onSongClick = onSongClick
                            )
                        }
                    }
                }
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
                isPlaying = viewModel.isPlaying,
                isFavourite = true,
                isOnRepeat = true,
                progress = viewModel.progress,
                onProgressChange = viewModel::onProgressChange,
                progressString = { viewModel.processString },
                currentSelectedSong = currentSelectedSong!!,
                songDuration = viewModel.duration.toFloat(),
                currentSelectedSongCoverArt = viewModel.currentSelectedSongCover,
                currentSelectedSongPalette = viewModel.currentSelectedSongPalette,
                onPausePlayClick = viewModel::onPausePlayClick,
                onSkipNextClick = viewModel::onSkipNextClick,
                onSkipPreviousClick = viewModel::onSkipPreviousClick,
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
    songs: List<Song>,
    imageLoader: suspend (String) -> ByteArray?,
    onSongClick: (Pair<Int, Song>) -> Unit,
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
            indicator = {},
            edgePadding = 0.dp,
            selectedTabIndex = selectedIndex,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
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
                        Text(
                            text = tab,
                            fontSize = if (selectedIndex == index) MaterialTheme.typography.titleMedium.fontSize else MaterialTheme.typography.bodyMedium.fontSize,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            fontWeight = if (selectedIndex == index) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
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
    }
}