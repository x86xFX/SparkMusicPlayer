package me.theek.spark.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.theek.spark.core.design_system.components.SparkCircleButton
import me.theek.spark.core.design_system.ui.theme.InriaSansFontFamily
import me.theek.spark.core.design_system.ui.theme.onboarding_screen_rainbow_color_1
import me.theek.spark.core.design_system.ui.theme.onboarding_screen_rainbow_color_2
import me.theek.spark.core.design_system.ui.theme.onboarding_screen_rainbow_color_3

@Composable
fun WelcomeScreen(
    onNavigateToHomeScreen: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()
    var shouldShowScannerScreen by rememberSaveable { mutableStateOf(false) }

    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(Color(0xFF080133), Color(0xFF000000)),
                center = size.center,
                radius = biggerDimension / 1.2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        if (onboardingViewModel.shouldNavigateToHome) onNavigateToHomeScreen()
    }

    LaunchedEffect(key1 = onboardingViewModel.shouldNavigateToHome) {
        if (onboardingViewModel.shouldNavigateToHome) onNavigateToHomeScreen()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = largeRadialGradient)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .fillMaxWidth()
        ) {

            val rainbowColors = remember {
                listOf(
                    onboarding_screen_rainbow_color_1,
                    onboarding_screen_rainbow_color_2,
                    onboarding_screen_rainbow_color_3
                )
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append(stringResource(R.string.music_for))
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append(stringResource(R.string.every_mood))
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append(stringResource(R.string.every_moment))
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append(stringResource(R.string.every_you))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize,
                    fontFamily = InriaSansFontFamily,
                ),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
        }

        SparkCircleButton(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 20.dp)
                .align(Alignment.BottomEnd),
            onClick = { shouldShowScannerScreen = true },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.next)
        )

        AnimatedVisibility(
            visible = shouldShowScannerScreen,
            enter = slideIn(tween(500, easing = LinearOutSlowInEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            }
        ) {
            MediaScanScreen(
                uiState = uiState,
                onPermissionResult = onboardingViewModel::onPermissionResult,
                shouldShowPermissionAlert = onboardingViewModel.shouldShowPermissionAlert,
                onDismissPermissionAlert = onboardingViewModel::onDismissPermissionAlert
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onNavigateToHomeScreen = {})
}