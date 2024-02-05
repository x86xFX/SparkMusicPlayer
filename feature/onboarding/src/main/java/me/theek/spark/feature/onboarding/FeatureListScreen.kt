package me.theek.spark.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theek.spark.core.design_system.components.CircleWithLine
import me.theek.spark.core.design_system.components.SparkCircleButton
import me.theek.spark.core.design_system.ui.theme.InriaSansFontFamily

@Composable
fun FeatureListScreen() {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(color = Color(0xFF000000))
    ) {
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
                .padding(end = 20.dp, bottom = 20.dp)
                .align(Alignment.BottomEnd),
            onClick = { },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.next)
        )
    }
}

@Preview
@Composable
private fun FeatureListScreenPreview() {
    FeatureListScreen()
}