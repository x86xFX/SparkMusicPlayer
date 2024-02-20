package me.theek.spark.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.theek.spark.core.design_system.ui.theme.InriaSansFontFamily
import me.theek.spark.core.design_system.ui.theme.media_scanner_background
import me.theek.spark.core.design_system.ui.theme.media_scanner_content
import me.theek.spark.core.design_system.ui.theme.media_scanner_progress_color
import me.theek.spark.core.design_system.ui.theme.media_scanner_progress_track_color

@Composable
internal fun MediaScanProgressCard(
    currentProgress: Float,
    progressMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = media_scanner_background,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .background(Color.Black, shape = CircleShape)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Scanning...",
                color = media_scanner_content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = InriaSansFontFamily,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { currentProgress },
                strokeCap = StrokeCap.Round,
                color = media_scanner_progress_color,
                trackColor = media_scanner_progress_track_color
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = progressMessage,
                color = media_scanner_content,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = InriaSansFontFamily,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
    }
}

@Preview
@Composable
private fun MediaScanProgressCardPreview() {
    MediaScanProgressCard(0.5f, "")
}