package me.theek.spark.feature.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
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

@Composable
internal fun FeatureListScreen(
    onboardingViewModel: OnboardingViewModel,
    onNavigateToHomeScreen: () -> Unit
) {

    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        onboardingViewModel.onPermissionResult(it)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = onboardingViewModel.shouldNavigateToHome) {
        if (onboardingViewModel.shouldNavigateToHome) {
            onNavigateToHomeScreen()
        }
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = Color(0xFF000000))
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
                .wrapContentSize(),
            painter = painterResource(id = R.drawable.ic_placeholder),
            contentDescription = stringResource(R.string.feature_list_background),
            tint = Color(0xFF60FF5C)
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
                color = Color(0xFFFFFFFF),
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
                color = Color(0xFFFFFFFF),
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

        SparkCircleButton(
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(end = 20.dp, bottom = 20.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                }
            },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            backgroundColor = Color(0xFF60FF5C),
            tint = Color(0xFF000000),
            contentDescription = stringResource(R.string.next)
        )
    }

    if (onboardingViewModel.shouldShowPermissionAlert) {
        MediaReadPermissionAlert(
            onDismissRequest = onboardingViewModel::onDismissPermissionAlert,
            onNavigateToSettings = {
                openAppSettings(activity = context)
            }
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