package me.theek.spark.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import me.theek.spark.feature.music_player.ArtistDetailScreen
import me.theek.spark.feature.music_player.MusicListScreen
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.ShowSongInfoBottomBar
import me.theek.spark.feature.music_player.viewmodels.PlayerViewModel
import me.theek.spark.feature.music_player.viewmodels.PlaylistViewModel
import me.theek.spark.feature.onboarding.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SparkNavigation(
    uiState: MainActivityUiState.Success,
    onSongServiceStart: () -> Unit
) {
    val navController = rememberNavController()
    val playerViewModel = hiltViewModel<PlayerViewModel>()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()

    NavHost(
        navController = navController,
        startDestination = if (uiState.userData.shouldHideOnboarding) Route.HOME else Route.ONBOARDING
    ) {
        composable(route = Route.ONBOARDING) {
            WelcomeScreen(
                onNavigateToHomeScreen = {
                    if (navController.canGoBack) {
                        navController.navigate(Route.HOME) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
        
        composable(route = Route.HOME) {
            MusicListScreen(
                playerViewModel = playerViewModel,
                playlistViewModel = playlistViewModel,
                onSongServiceStart = onSongServiceStart,
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
                    println("Clicked playlist id: $it")
                }
            )
        }

        composable(
            route = Route.ARTIST_DETAILS,
            enterTransition = { slideInHorizontally(animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(animationSpec = tween(500)) }
        ) {
            val artistDetails = navController.previousBackStackEntry?.savedStateHandle?.get<ArtistDetails>(key = "artist_details")
            if (artistDetails != null) {
                ArtistDetailScreen(
                    artistDetails = artistDetails,
                    onNavigateBackClick = { if (navController.canGoBack) { navController.popBackStack() } },
                    onSongInfoClick = playerViewModel::onSongInfoClick,
                    onCreatePlaylistClick = playlistViewModel::addToQueuedPlaylistSong,
                    onAddToExistingPlaylistClick = playlistViewModel::onAddToExistingPlaylistClick,
                    onSongClick = playerViewModel::onArtistQueueSongClick
                )
            }
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
    const val HOME = "home_screen"
    const val ARTIST_DETAILS = "artist_detail_screen"
}