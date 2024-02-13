package me.theek.spark.feature.music_player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.components.SongListUi
import me.theek.spark.feature.music_player.util.MusicUiTabs

@Composable
fun MusicListScreen(viewModel: MusicListScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            SparkPlayerTopAppBar(onSearch = {})
        },
        content = { innerPadding ->
            when (uiState) {
                UiState.Loading -> Unit
                is UiState.Success -> {
                    MusicUi(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding()),
                        songs = (uiState as UiState.Success).songs
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SparkPlayerTopAppBar(onSearch: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Spark Player")
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicUi(
    songs: List<Song>,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { MusicUiTabs.size })
    val selectedIndex by remember { derivedStateOf { pagerState.currentPage } }

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
            modifier = Modifier.fillMaxWidth(),
            state = pagerState
        ) { currentTab ->
            when (currentTab) {
               0 -> {
                    SongListUi(songs = songs)
               }

                1 -> Unit

                2 -> Unit
            }
        }
    }
}