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
fun rememberQueueMusic(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "queue_music",
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
                moveTo(26.625f, 33.125f)
                quadToRelative(-2.042f, 0f, -3.458f, -1.375f)
                quadToRelative(-1.417f, -1.375f, -1.417f, -3.375f)
                quadToRelative(0f, -1.958f, 1.375f, -3.354f)
                quadToRelative(1.375f, -1.396f, 3.375f, -1.396f)
                quadToRelative(0.583f, 0f, 1.167f, 0.125f)
                quadToRelative(0.583f, 0.125f, 1.166f, 0.375f)
                verticalLineToRelative(-12.75f)
                quadToRelative(0f, -0.5f, 0.375f, -0.854f)
                reflectiveQuadToRelative(0.917f, -0.354f)
                horizontalLineTo(35f)
                quadToRelative(0.625f, 0f, 1.042f, 0.437f)
                quadToRelative(0.416f, 0.438f, 0.416f, 1.063f)
                reflectiveQuadToRelative(-0.416f, 1.062f)
                quadToRelative(-0.417f, 0.438f, -1.042f, 0.438f)
                horizontalLineToRelative(-3.542f)
                verticalLineToRelative(15.25f)
                quadToRelative(0f, 1.958f, -1.416f, 3.333f)
                quadToRelative(-1.417f, 1.375f, -3.417f, 1.375f)
                close()
                moveTo(6.542f, 12.792f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.917f)
                quadToRelative(0f, -0.583f, 0.396f, -0.958f)
                reflectiveQuadToRelative(0.938f, -0.375f)
                horizontalLineToRelative(17.083f)
                quadToRelative(0.542f, 0f, 0.937f, 0.396f)
                quadToRelative(0.396f, 0.395f, 0.396f, 0.937f)
                reflectiveQuadToRelative(-0.396f, 0.917f)
                quadToRelative(-0.395f, 0.375f, -0.937f, 0.375f)
                close()
                moveToRelative(0f, 6.75f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.396f)
                quadToRelative(-0.396f, -0.396f, -0.396f, -0.938f)
                quadToRelative(0f, -0.541f, 0.396f, -0.937f)
                reflectiveQuadToRelative(0.938f, -0.396f)
                horizontalLineToRelative(17.083f)
                quadToRelative(0.542f, 0f, 0.937f, 0.396f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.937f)
                quadToRelative(0f, 0.542f, -0.396f, 0.938f)
                quadToRelative(-0.395f, 0.396f, -0.937f, 0.396f)
                close()
                moveToRelative(0f, 6.75f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.396f)
                quadToRelative(-0.396f, -0.396f, -0.396f, -0.938f)
                quadToRelative(0f, -0.541f, 0.396f, -0.937f)
                reflectiveQuadToRelative(0.938f, -0.396f)
                horizontalLineToRelative(10.291f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.542f, -0.375f, 0.938f)
                quadToRelative(-0.375f, 0.396f, -0.917f, 0.396f)
                close()
            }
        }.build()
    }
}