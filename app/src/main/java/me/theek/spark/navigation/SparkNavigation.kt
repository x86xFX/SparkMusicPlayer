package me.theek.spark.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.theek.spark.MainActivityUiState
import me.theek.spark.feature.onboarding.WelcomeScreen

@Composable
fun SparkNavigation(uiState: MainActivityUiState.Success) {
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
            Box(modifier = Modifier.fillMaxSize().background(Color.Red))
        }
    }
}

private val NavHostController.canGoBack: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

sealed class Screen(val route: String) {
    data object Onboarding : Screen(route = "onboarding_screen")
    data object Home : Screen(route = "home_screen")
}