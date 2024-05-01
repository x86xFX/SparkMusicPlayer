package me.theek.spark

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.ui.theme.SparkMusicPlayerTheme
import me.theek.spark.core.design_system.ui.theme.isInDarkMode
import me.theek.spark.core.service.MediaService
import me.theek.spark.navigation.SparkNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = if (isInDarkMode()) {
                SystemBarStyle.dark(scrim = Color.TRANSPARENT)
            } else {
                SystemBarStyle.light(
                    scrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT
                )
            }
        )

        splashScreen.setKeepOnScreenCondition { mainViewModel.uiState.value is MainActivityUiState.Loading }

        lifecycleScope.launch {
            val mainActivityUiState = mainViewModel.uiState
                .filterIsInstance<MainActivityUiState.Success>()
                .first()

            setContent {
                SparkMusicPlayerTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        SparkNavigation(
                            uiState = mainActivityUiState,
                            onSongClick = {
                                ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, MediaService::class.java))
                            }
                        )
                    }
                }
            }
        }
    }

//    override fun onDestroy() {
//        stopService(Intent(applicationContext, MediaService::class.java))
//        super.onDestroy()
//    }
}