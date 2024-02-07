package me.theek.spark.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import me.theek.spark.core.design_system.components.SparkCircleButton
import me.theek.spark.core.design_system.components.SparkImageLoader
import me.theek.spark.core.design_system.ui.theme.InriaSansFontFamily

@Composable
fun WelcomeScreen(
    onNavigateToHomeScreen: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    var featureListScreenVisibility by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF000000))
    ) {
        SparkImageLoader(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize(),
            imageUrl = "https://i.imgur.com/fxHVmkg.png",
            contentDescription = stringResource(R.string.welcome_screen_background_image)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(
                        vertical = 40.dp,
                        horizontal = 30.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                SparkImageLoader(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(188.dp),
                    imageUrl = "https://i.imgur.com/IlzN84c.png",
                    contentDescription = stringResource(R.string.welcome_screen_picture)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val rainbowColors =
                remember { listOf(Color(0xFFFD5CD0), Color(0xFF4590EC), Color(0xFF9F32FF)) }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append("Music for ")
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append("every mood,")
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append("\nevery moment,")
                    }

                    withStyle(
                        SpanStyle(brush = Brush.linearGradient(colors = rainbowColors))
                    ) {
                        append("\nevery you.")
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
            onClick = { featureListScreenVisibility = true },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.next)
        )

        AnimatedVisibility(
            visible = featureListScreenVisibility,
            enter = slideIn(tween(500, easing = LinearOutSlowInEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            },
            exit = slideOutHorizontally()
        ) {
            FeatureListScreen(
                onboardingViewModel = onboardingViewModel,
                onNavigateToHomeScreen = onNavigateToHomeScreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onNavigateToHomeScreen = {})
}