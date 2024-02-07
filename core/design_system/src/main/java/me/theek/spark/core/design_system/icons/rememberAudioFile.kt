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
fun roundedAudioFile(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "audio_file",
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
                moveTo(17.833f, 31.917f)
                quadToRelative(1.709f, 0f, 2.875f, -1.146f)
                quadToRelative(1.167f, -1.146f, 1.167f, -2.854f)
                verticalLineTo(20f)
                horizontalLineToRelative(3.5f)
                quadToRelative(0.542f, 0f, 0.937f, -0.396f)
                quadToRelative(0.396f, -0.396f, 0.396f, -0.937f)
                quadToRelative(0f, -0.584f, -0.396f, -0.979f)
                quadToRelative(-0.395f, -0.396f, -0.937f, -0.396f)
                horizontalLineToRelative(-3.833f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.396f)
                quadToRelative(-0.375f, 0.395f, -0.375f, 0.937f)
                verticalLineToRelative(6.208f)
                quadToRelative(-0.458f, -0.416f, -1.104f, -0.625f)
                quadTo(18.5f, 24f, 17.833f, 24f)
                quadToRelative(-1.666f, 0f, -2.771f, 1.146f)
                quadToRelative(-1.104f, 1.146f, -1.104f, 2.771f)
                quadToRelative(0f, 1.666f, 1.125f, 2.833f)
                quadToRelative(1.125f, 1.167f, 2.75f, 1.167f)
                close()
                moveToRelative(-8.291f, 4.458f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.771f)
                quadToRelative(-0.791f, -0.771f, -0.791f, -1.854f)
                verticalLineTo(6.25f)
                quadToRelative(0f, -1.083f, 0.791f, -1.854f)
                quadToRelative(0.792f, -0.771f, 1.834f, -0.771f)
                horizontalLineToRelative(13.291f)
                quadToRelative(0.5f, 0f, 0.979f, 0.187f)
                quadToRelative(0.48f, 0.188f, 0.896f, 0.563f)
                lineToRelative(7.584f, 7.583f)
                quadToRelative(0.416f, 0.417f, 0.604f, 0.896f)
                quadToRelative(0.187f, 0.479f, 0.187f, 0.979f)
                verticalLineTo(33.75f)
                quadToRelative(0f, 1.083f, -0.791f, 1.854f)
                quadToRelative(-0.792f, 0.771f, -1.834f, 0.771f)
                close()
                moveTo(22.625f, 6.25f)
                horizontalLineTo(9.542f)
                verticalLineToRelative(27.5f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(14f)
                horizontalLineToRelative(-6.541f)
                quadToRelative(-0.584f, 0f, -0.938f, -0.375f)
                reflectiveQuadToRelative(-0.354f, -0.917f)
                close()
                moveToRelative(-13.083f, 0f)
                verticalLineTo(14f)
                verticalLineTo(6.25f)
                verticalLineToRelative(27.5f)
                verticalLineToRelative(-27.5f)
                close()
            }
        }.build()
    }
}