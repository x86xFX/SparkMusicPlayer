package me.theek.spark.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.theek.spark.MainActivityUiState
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.feature.music_player.screens.ArtistDetailScreen
import me.theek.spark.feature.music_player.screens.MusicListScreen
import me.theek.spark.feature.music_player.screens.PlaylistViewScreen
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.ShowSongInfoBottomBar
import me.theek.spark.feature.music_player.screens.AlbumDetailScreen
import me.theek.spark.feature.music_player.viewmodels.PlayerViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel
import me.theek.spark.feature.onboarding.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SparkNavigation(
    uiState: MainActivityUiState.Success,
    onSongClick: () -> Unit
) {
    val navController = rememberNavController()
    val playerViewModel = hiltViewModel<PlayerViewModel>()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()

    NavHost(
        navController = navController,
        startDestination = if (uiState.userData.shouldHideOnboarding) Route.MUSIC_DETAILS else Route.ONBOARDING
    ) {

        composable(route = Route.ONBOARDING) {
            WelcomeScreen(
                onNavigateToHomeScreen = {
                    if (navController.canGoBack) {
                        navController.navigate(Route.MUSIC_DETAILS) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                        playerViewModel.onLoadSongData()
                    }
                }
            )
        }
        
        composable(route = Route.MUSIC_DETAILS) {
            MusicListScreen(
                playerViewModel = playerViewModel,
                playlistViewModel = playlistViewModel,
                currentQueue = playerViewModel.currentQueuedSongList,
                onNavigateToArtistDetailScreen = { artistDetails ->
                    if (navController.canGoBack) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "artist_details",
                            value = artistDetails
                        )
                        navController.navigate(Route.ARTIST_DETAILS)
                    }
                },
                onPlaylistViewClick = {
                    if (navController.canGoBack) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "playlist_id",
                            value = it
                        )
                    }
                    navController.navigate(Route.PLAYLIST)
                },
                onAlbumClick = {
                    if (navController.canGoBack) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "album_id",
                            value = it
                        )
                    }
                    navController.navigate(Route.ALBUM_DETAILS)
                },
                onSongClick = onSongClick
            )
        }

        composable(
            route = Route.PLAYLIST,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val playlistId = remember { navController.previousBackStackEntry?.savedStateHandle?.get<Long>(key = "playlist_id") }
            PlaylistViewScreen(
                playlistViewModel = playlistViewModel,
                playlistId = playlistId,
                onNavigateBackClick = { if(navController.canGoBack) { navController.popBackStack() } },
                onSongInfoClick = playerViewModel::onSongInfoClick,
                onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                onSongClick = playerViewModel::onCustomQueueSongClick
            )
        }

        composable(
            route = Route.ARTIST_DETAILS,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val artistDetails = remember { navController.previousBackStackEntry?.savedStateHandle?.get<ArtistDetails>(key = "artist_details") }
            ArtistDetailScreen(
                playlistViewModel = playlistViewModel,
                artistDetails = artistDetails,
                onNavigateBackClick = { if (navController.canGoBack) { navController.popBackStack() } },
                onSongInfoClick = playerViewModel::onSongInfoClick,
                onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                onSongClick = playerViewModel::onCustomQueueSongClick
            )
        }


        composable(
            route = Route.ALBUM_DETAILS,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            val albumId = remember { navController.previousBackStackEntry?.savedStateHandle?.get<Int?>(key = "album_id") }
            AlbumDetailScreen(
                albumId = albumId,
                playlistViewModel = playlistViewModel,
                onNavigateBackClick = { if (navController.canGoBack) { navController.popBackStack() } },
                onSongInfoClick = playerViewModel::onSongInfoClick,
                onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                onSongClick = playerViewModel::onCustomQueueSongClick
            )
        }
    }

    ShowSongInfoBottomBar(
        sheetState = rememberModalBottomSheetState(true),
        songInfo = playerViewModel.songInfo,
        onDismissRequest = playerViewModel::onSongInfoSheetDismiss
    )

    if (playlistViewModel.shouldOpenCreatePlaylistDialog) {
        AlertDialog(
            onDismissRequest = playlistViewModel::onCreatePlaylistDismiss,
            confirmButton = {
                Button(onClick = playlistViewModel::onPlaylistSave) {
                    Text(text = "Create")
                }
            },
            title = {
                Text(text = "Create playlist")
            },
            text = {
                OutlinedTextField(
                    value = playlistViewModel.newPlaylistName,
                    onValueChange = playlistViewModel::onNewPlaylistNameChange,
                    label = { Text(text = stringResource(R.string.playlist_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { playlistViewModel.onPlaylistSave() }
                    )
                )
            }
        )
    }
}

private val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

object Route {
    const val ONBOARDING = "onboarding_screen"
    const val MUSIC_DETAILS = "music_detail_screen"
    const val ARTIST_DETAILS = "artist_detail_screen"
    const val ALBUM_DETAILS = "album_detail_screen"
    const val PLAYLIST = "playlist_detail_screen"
}