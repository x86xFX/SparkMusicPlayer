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
fun rememberRepeatOne(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "repeat_one",
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
                moveTo(19.375f, 17.167f)
                horizontalLineToRelative(-1.292f)
                quadToRelative(-0.416f, 0f, -0.729f, -0.292f)
                quadToRelative(-0.312f, -0.292f, -0.312f, -0.75f)
                reflectiveQuadToRelative(0.312f, -0.771f)
                quadToRelative(0.313f, -0.312f, 0.771f, -0.312f)
                horizontalLineToRelative(2.292f)
                quadToRelative(0.458f, 0f, 0.771f, 0.312f)
                quadToRelative(0.312f, 0.313f, 0.312f, 0.771f)
                verticalLineToRelative(7.708f)
                quadToRelative(0f, 0.417f, -0.312f, 0.729f)
                quadToRelative(-0.313f, 0.313f, -0.771f, 0.313f)
                quadToRelative(-0.417f, 0f, -0.729f, -0.313f)
                quadToRelative(-0.313f, -0.312f, -0.313f, -0.77f)
                close()
                moveToRelative(9.5f, 11.375f)
                verticalLineTo(23.25f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.917f, -0.395f)
                quadToRelative(0.583f, 0f, 0.958f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.916f)
                verticalLineToRelative(6.667f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                horizontalLineTo(10.333f)
                lineToRelative(2.334f, 2.333f)
                quadToRelative(0.416f, 0.417f, 0.437f, 0.958f)
                quadToRelative(0.021f, 0.542f, -0.396f, 0.959f)
                quadToRelative(-0.416f, 0.416f, -0.937f, 0.416f)
                quadToRelative(-0.521f, 0f, -0.896f, -0.375f)
                lineTo(6.167f, 30.75f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.896f)
                reflectiveQuadToRelative(0.375f, -0.937f)
                lineToRelative(4.666f, -4.625f)
                quadToRelative(0.375f, -0.417f, 0.917f, -0.417f)
                reflectiveQuadToRelative(0.958f, 0.417f)
                quadToRelative(0.417f, 0.375f, 0.417f, 0.916f)
                quadToRelative(0f, 0.542f, -0.417f, 0.917f)
                lineToRelative(-2.375f, 2.417f)
                close()
                moveTo(11.167f, 11.375f)
                verticalLineToRelative(5.292f)
                quadToRelative(0f, 0.541f, -0.375f, 0.937f)
                reflectiveQuadTo(9.875f, 18f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                verticalLineToRelative(-6.666f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.916f, -0.375f)
                horizontalLineToRelative(19.875f)
                lineToRelative(-2.375f, -2.333f)
                quadToRelative(-0.375f, -0.375f, -0.395f, -0.938f)
                quadToRelative(-0.021f, -0.562f, 0.395f, -0.979f)
                quadToRelative(0.375f, -0.375f, 0.917f, -0.396f)
                quadToRelative(0.542f, -0.021f, 0.917f, 0.354f)
                lineToRelative(4.708f, 4.709f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.916f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                lineToRelative(-4.667f, 4.667f)
                quadToRelative(-0.416f, 0.375f, -0.958f, 0.375f)
                reflectiveQuadToRelative(-0.917f, -0.375f)
                quadToRelative(-0.416f, -0.417f, -0.416f, -0.959f)
                quadToRelative(0f, -0.541f, 0.375f, -0.916f)
                lineToRelative(2.416f, -2.417f)
                close()
            }
        }.build()
    }
}