package me.theek.spark.core.design_system.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.theek.spark.core.design_system.R

@Composable
fun CircleWithLine(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier.matchParentSize(),
            contentDescription = stringResource(R.string.circle_with_line)
        ) {
            drawLine(
                color = color,
                start = Offset(x = size.width.div(8).times(7), y = size.height.div(16)),
                end = Offset(x = size.width.div(8).times(7), y = size.height.div(7).times(3)),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = color,
                center = Offset(x = size.width.div(8).times(7), y = size.height.div(7).times(3)),
                radius = 20f
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleWithLinePreview() {
    CircleWithLine(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    )
}