package me.theek.spark.core.design_system.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberSignalWifiStatusBarNotConnected(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "signal_wifi_statusbar_not_connected",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 34.208f)
                quadToRelative(-0.25f, 0f, -0.479f, -0.104f)
                quadToRelative(-0.229f, -0.104f, -0.438f, -0.312f)
                lineTo(1.25f, 16f)
                quadToRelative(-0.417f, -0.417f, -0.396f, -0.937f)
                quadToRelative(0.021f, -0.521f, 0.438f, -0.896f)
                quadToRelative(3.916f, -3.5f, 8.687f, -5.375f)
                quadTo(14.75f, 6.917f, 20f, 6.917f)
                quadToRelative(5.25f, 0f, 10f, 1.875f)
                reflectiveQuadToRelative(8.708f, 5.375f)
                quadToRelative(0.375f, 0.375f, 0.396f, 0.896f)
                quadToRelative(0.021f, 0.52f, -0.354f, 0.937f)
                lineToRelative(-0.625f, 0.583f)
                quadToRelative(-1.042f, -1.166f, -2.479f, -1.791f)
                quadToRelative(-1.438f, -0.625f, -3.104f, -0.625f)
                quadToRelative(-3.25f, 0f, -5.542f, 2.271f)
                quadToRelative(-2.292f, 2.27f, -2.292f, 5.52f)
                quadToRelative(0f, 1.709f, 0.667f, 3.084f)
                reflectiveQuadToRelative(1.833f, 2.458f)
                lineToRelative(-6.291f, 6.292f)
                quadToRelative(-0.209f, 0.208f, -0.438f, 0.312f)
                quadToRelative(-0.229f, 0.104f, -0.479f, 0.104f)
                close()
                moveToRelative(12.542f, -5.625f)
                quadToRelative(-0.459f, 0f, -0.771f, -0.291f)
                quadToRelative(-0.313f, -0.292f, -0.229f, -0.709f)
                quadToRelative(0.083f, -1.166f, 0.5f, -1.958f)
                quadToRelative(0.416f, -0.792f, 1.458f, -1.667f)
                quadToRelative(1f, -0.833f, 1.333f, -1.396f)
                quadToRelative(0.334f, -0.562f, 0.334f, -1.312f)
                quadToRelative(0f, -1f, -0.667f, -1.625f)
                reflectiveQuadTo(32.75f, 19f)
                quadToRelative(-0.833f, 0f, -1.479f, 0.333f)
                quadToRelative(-0.646f, 0.334f, -1.104f, 0.959f)
                quadToRelative(-0.292f, 0.333f, -0.688f, 0.437f)
                quadToRelative(-0.396f, 0.104f, -0.729f, -0.104f)
                quadToRelative(-0.417f, -0.208f, -0.542f, -0.604f)
                quadToRelative(-0.125f, -0.396f, 0.084f, -0.771f)
                quadToRelative(0.708f, -1.125f, 1.833f, -1.792f)
                quadToRelative(1.125f, -0.666f, 2.625f, -0.666f)
                quadToRelative(1.958f, 0f, 3.271f, 1.25f)
                quadToRelative(1.312f, 1.25f, 1.312f, 3.166f)
                quadToRelative(0f, 1.167f, -0.479f, 2f)
                quadToRelative(-0.479f, 0.834f, -1.562f, 1.792f)
                quadToRelative(-0.875f, 0.75f, -1.23f, 1.292f)
                quadToRelative(-0.354f, 0.541f, -0.437f, 1.291f)
                quadToRelative(-0.042f, 0.459f, -0.354f, 0.729f)
                quadToRelative(-0.313f, 0.271f, -0.729f, 0.271f)
                close()
                moveToRelative(0f, 4.834f)
                quadToRelative(-0.625f, 0f, -1.084f, -0.459f)
                quadTo(31f, 32.5f, 31f, 31.875f)
                reflectiveQuadToRelative(0.458f, -1.083f)
                quadToRelative(0.459f, -0.459f, 1.084f, -0.459f)
                reflectiveQuadToRelative(1.062f, 0.459f)
                quadToRelative(0.438f, 0.458f, 0.438f, 1.083f)
                reflectiveQuadToRelative(-0.438f, 1.083f)
                quadToRelative(-0.437f, 0.459f, -1.062f, 0.459f)
                close()
            }
        }.build()
    }
}