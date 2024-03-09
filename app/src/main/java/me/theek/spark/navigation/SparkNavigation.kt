package me.theek.spark.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.theek.spark.MainActivityUiState
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
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
        
        composable(route = Screen.Home.route) {
            MusicListScreen(onSongServiceStart = onSongServiceStart)
        }
    }
}

private val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

sealed class Screen(val route: String) {
    data object Onboarding : Screen(route = "onboarding_screen")
    data object Home : Screen(route = "home_screen")
}