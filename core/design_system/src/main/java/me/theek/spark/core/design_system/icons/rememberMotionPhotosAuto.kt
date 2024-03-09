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
fun rememberMotionPhotosAuto(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "motion_photos_auto",
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
                moveTo(15.333f, 25.792f)
                quadToRelative(0.292f, 0f, 0.542f, -0.209f)
                quadToRelative(0.25f, -0.208f, 0.333f, -0.416f)
                lineToRelative(1.125f, -2.959f)
                horizontalLineToRelative(5.375f)
                lineToRelative(1.084f, 2.959f)
                quadToRelative(0.083f, 0.25f, 0.333f, 0.437f)
                quadToRelative(0.25f, 0.188f, 0.583f, 0.188f)
                quadToRelative(0.584f, 0f, 0.834f, -0.375f)
                reflectiveQuadToRelative(0.041f, -0.917f)
                lineToRelative(-4.208f, -10.958f)
                quadToRelative(-0.167f, -0.417f, -0.563f, -0.688f)
                quadToRelative(-0.395f, -0.271f, -0.812f, -0.271f)
                quadToRelative(-0.417f, 0f, -0.812f, 0.271f)
                quadToRelative(-0.396f, 0.271f, -0.521f, 0.688f)
                lineToRelative(-4.167f, 11f)
                quadToRelative(-0.208f, 0.5f, 0.042f, 0.875f)
                reflectiveQuadToRelative(0.791f, 0.375f)
                close()
                moveToRelative(2.584f, -5.334f)
                lineToRelative(2.041f, -5.583f)
                horizontalLineToRelative(0.084f)
                lineToRelative(2.083f, 5.583f)
                close()
                moveTo(20f, 36.375f)
                quadToRelative(-3.417f, 0f, -6.396f, -1.292f)
                quadToRelative(-2.979f, -1.291f, -5.208f, -3.5f)
                quadToRelative(-2.229f, -2.208f, -3.5f, -5.208f)
                reflectiveQuadTo(3.625f, 20f)
                quadToRelative(0f, -1.208f, 0.146f, -2.375f)
                reflectiveQuadToRelative(0.521f, -2.375f)
                quadToRelative(0.166f, -0.5f, 0.666f, -0.729f)
                quadToRelative(0.5f, -0.229f, 1f, -0.021f)
                quadToRelative(0.5f, 0.167f, 0.73f, 0.646f)
                quadToRelative(0.229f, 0.479f, 0.104f, 0.979f)
                quadToRelative(-0.25f, 0.958f, -0.396f, 1.917f)
                quadTo(6.25f, 19f, 6.25f, 20f)
                quadToRelative(0f, 5.75f, 4f, 9.75f)
                reflectiveQuadToRelative(9.75f, 4f)
                quadToRelative(5.75f, 0f, 9.75f, -4f)
                reflectiveQuadToRelative(4f, -9.75f)
                quadToRelative(0f, -5.75f, -4f, -9.75f)
                reflectiveQuadToRelative(-9.75f, -4f)
                quadToRelative(-1f, 0f, -1.958f, 0.167f)
                quadToRelative(-0.959f, 0.166f, -1.917f, 0.375f)
                quadToRelative(-0.5f, 0.166f, -0.979f, -0.063f)
                reflectiveQuadToRelative(-0.688f, -0.687f)
                quadToRelative(-0.25f, -0.584f, 0.021f, -1.084f)
                quadToRelative(0.271f, -0.5f, 0.896f, -0.666f)
                quadToRelative(1.083f, -0.334f, 2.208f, -0.5f)
                quadToRelative(1.125f, -0.167f, 2.292f, -0.167f)
                quadToRelative(3.417f, 0f, 6.437f, 1.292f)
                quadToRelative(3.021f, 1.291f, 5.25f, 3.5f)
                quadToRelative(2.23f, 2.208f, 3.521f, 5.208f)
                quadToRelative(1.292f, 3f, 1.292f, 6.375f)
                quadToRelative(0f, 3.417f, -1.292f, 6.396f)
                quadToRelative(-1.291f, 2.979f, -3.5f, 5.208f)
                quadToRelative(-2.208f, 2.229f, -5.208f, 3.5f)
                reflectiveQuadTo(20f, 36.375f)
                close()
                moveTo(9.125f, 11.208f)
                quadToRelative(-0.875f, 0f, -1.5f, -0.625f)
                reflectiveQuadTo(7f, 9.042f)
                quadToRelative(0f, -0.875f, 0.625f, -1.479f)
                quadToRelative(0.625f, -0.605f, 1.5f, -0.605f)
                reflectiveQuadToRelative(1.5f, 0.605f)
                quadToRelative(0.625f, 0.604f, 0.625f, 1.52f)
                quadToRelative(0f, 0.875f, -0.625f, 1.5f)
                reflectiveQuadToRelative(-1.5f, 0.625f)
                close()
                moveTo(20f, 20f)
                close()
            }
        }.build()
    }
}