package me.theek.spark

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.theek.spark.core.design_system.ui.theme.SparkMusicPlayerTheme
import me.theek.spark.core.service.MediaService
import me.theek.spark.navigation.SparkNavigation
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var exoPlayer: ExoPlayer
    private var isServiceRunning: Boolean = false
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value is MainActivityUiState.Loading
        }

        lifecycleScope.launch {
            val mainActivityUiState = mainViewModel.uiState
                .filterIsInstance<MainActivityUiState.Success>()
                .first()

            setContent {
                SparkMusicPlayerTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        SparkNavigation(
                            uiState = mainActivityUiState,
                            onSongServiceStart = { startService() }
                        )
                    }
                }
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
    }

    override fun onDestroy() {
        exoPlayer.release()
        val intent = Intent(this, MediaService::class.java)
        stopService(intent)
        super.onDestroy()
    }

    private fun startService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isServiceRunning) {
                val intent = Intent(this, MediaService::class.java)
                startForegroundService(intent)

            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }
}