package me.theek.spark.feature.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theek.spark.core.design_system.components.CircleWithLine
import me.theek.spark.core.design_system.components.SparkCircleButton
import me.theek.spark.core.design_system.icons.roundedAudioFile
import me.theek.spark.core.design_system.ui.theme.InriaSansFontFamily
import me.theek.spark.core.design_system.ui.theme.md_theme_dark_shadow
import me.theek.spark.core.design_system.ui.theme.media_scanner_content
import me.theek.spark.core.design_system.ui.theme.onboarding_screen_icon_tint
import me.theek.spark.core.design_system.ui.theme.spark_circular_button_background_2

@Composable
internal fun MediaScanScreen(
    uiState: UiState,
    onPermissionResult: (Boolean) -> Unit,
    shouldShowPermissionAlert: Boolean,
    onDismissPermissionAlert: () -> Unit
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = onPermissionResult
    )
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = md_theme_dark_shadow),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
                .wrapContentSize(),
            painter = painterResource(id = R.drawable.ic_placeholder),
            contentDescription = stringResource(R.string.feature_list_background),
            tint = onboarding_screen_icon_tint
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                text = "Take control of your music:",
                maxLines = 2,
                color = media_scanner_content,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize.div(1.45f),
                    fontFamily = InriaSansFontFamily,
                    textAlign = TextAlign.Start
                )
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.68f)
                    .padding(top = 40.dp, bottom = 10.dp),
                text = "Chop,\nTrim,\nand edit to create\nyour perfect\nlistening\nexperience.",
                color = media_scanner_content,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize.div(1.45f),
                    fontFamily = InriaSansFontFamily,
                    textAlign = TextAlign.End,
                    lineHeight = 35.sp
                )
            )
        }
        CircleWithLine(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        )

        when (uiState) {
            UiState.Idle -> Unit
            is UiState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Something went wrong")
                }
            }
            is UiState.Progress -> {
                MediaScanner(
                    hint = uiState.message ?: "~~~",
                    progress = uiState.progress
                )
            }
        }


        SparkCircleButton(
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(end = 20.dp, bottom = 20.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            backgroundColor = spark_circular_button_background_2,
            tint = md_theme_dark_shadow,
            contentDescription = stringResource(R.string.next),
            isLoading = uiState is UiState.Progress
        )
    }

    if (shouldShowPermissionAlert) {
        MediaReadPermissionAlert(
            onDismissRequest = onDismissPermissionAlert,
            onNavigateToSettings = {
                openAppSettings(activity = context)
            }
        )
    }
}

@Composable
private fun MediaScanner(
    hint: String,
    progress: Float
) {
    AnimatedVisibility(
        visible = true,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        MediaScanProgressCard(
            modifier = Modifier.fillMaxWidth(fraction = 0.88f),
            currentProgress = progress,
            progressMessage = hint
        )
    }
}

@Composable
private fun MediaReadPermissionAlert(
    onDismissRequest: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onNavigateToSettings) {
                Text(text = "Settings")
            }
        },
        icon = {
            Icon(
                imageVector = roundedAudioFile(),
                contentDescription = stringResource(R.string.audio_file_icon)
            )
        },
        title = {
            Text(
                text = "Permission Required",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = {
            Text(
                text = stringResource(R.string.audio_file_permission_alert_description)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}

private fun openAppSettings(activity: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    )
    activity.startActivity(intent)
}