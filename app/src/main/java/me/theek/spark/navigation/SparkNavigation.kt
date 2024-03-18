package me.theek.spark.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.theek.spark.MainActivityUiState
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.feature.music_player.ArtistDetailScreen
import me.theek.spark.feature.music_player.MusicListScreen
import me.theek.spark.feature.onboarding.WelcomeScreen

@Composable
fun SparkNavigation(
    uiState: MainActivityUiState.Success,
    onSongServiceStart: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (uiState.userData.shouldHideOnboarding)
            Screen.Home.route else Screen.Onboarding.route
    ) {
        composable(route = Screen.Onboarding.route) {
            WelcomeScreen(
                onNavigateToHomeScreen = {
                    if (navController.canGoBack) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
        
        composable(
            route = Screen.Home.route
        ) {
            MusicListScreen(
                onSongServiceStart = onSongServiceStart,
                onNavigateToArtistDetailScreen = {
                    if (navController.canGoBack) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "artist_with_songs",
                            value = it
                        )
                        navController.navigate(Screen.ArtistDetailScreen.route)
                    }
                }
            )
        }

        composable(
            route = Screen.ArtistDetailScreen.route,
            enterTransition = { slideInHorizontally(animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(animationSpec = tween(500)) }
        ) {
            val artistWithSongs = navController.previousBackStackEntry?.savedStateHandle?.get<ArtistDetails>(key = "artist_with_songs")
            if (artistWithSongs != null) {
                ArtistDetailScreen(
                    artistDetails = artistWithSongs,
                    onNavigateBackClick = {
                        if (navController.canGoBack) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

private val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

sealed class Screen(val route: String) {
    data object Onboarding : Screen(route = "onboarding_screen")
    data object Home : Screen(route = "home_screen")
    data object ArtistDetailScreen : Screen(route = "artist_detail_screen")
}