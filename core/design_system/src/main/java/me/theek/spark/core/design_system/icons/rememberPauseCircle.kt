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
fun rememberPauseCircle(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "pause_circle",
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
                moveTo(16.708f, 26.542f)
                quadToRelative(0.584f, 0f, 0.959f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                verticalLineTo(14.708f)
                quadToRelative(0f, -0.5f, -0.375f, -0.875f)
                reflectiveQuadToRelative(-0.959f, -0.375f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                verticalLineToRelative(10.542f)
                quadToRelative(0f, 0.5f, 0.375f, 0.875f)
                reflectiveQuadToRelative(0.916f, 0.375f)
                close()
                moveToRelative(6.584f, 0f)
                quadToRelative(0.541f, 0f, 0.916f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                verticalLineTo(14.708f)
                quadToRelative(0f, -0.5f, -0.375f, -0.875f)
                reflectiveQuadToRelative(-0.916f, -0.375f)
                quadToRelative(-0.584f, 0f, -0.959f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                verticalLineToRelative(10.542f)
                quadToRelative(0f, 0.5f, 0.375f, 0.875f)
                reflectiveQuadToRelative(0.959f, 0.375f)
                close()
                moveTo(20f, 36.375f)
                quadToRelative(-3.375f, 0f, -6.375f, -1.292f)
                quadToRelative(-3f, -1.291f, -5.208f, -3.521f)
                quadToRelative(-2.209f, -2.229f, -3.5f, -5.208f)
                quadTo(3.625f, 23.375f, 3.625f, 20f)
                reflectiveQuadToRelative(1.292f, -6.375f)
                quadToRelative(1.291f, -3f, 3.521f, -5.208f)
                quadToRelative(2.229f, -2.209f, 5.208f, -3.5f)
                quadTo(16.625f, 3.625f, 20f, 3.625f)
                reflectiveQuadToRelative(6.375f, 1.292f)
                quadToRelative(3f, 1.291f, 5.208f, 3.521f)
                quadToRelative(2.209f, 2.229f, 3.5f, 5.208f)
                quadToRelative(1.292f, 2.979f, 1.292f, 6.354f)
                reflectiveQuadToRelative(-1.292f, 6.375f)
                quadToRelative(-1.291f, 3f, -3.521f, 5.208f)
                quadToRelative(-2.229f, 2.209f, -5.208f, 3.5f)
                quadToRelative(-2.979f, 1.292f, -6.354f, 1.292f)
                close()
            }
        }.build()
    }
}