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
fun rememberSort(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "sort",
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
                moveTo(13.708f, 29.333f)
                horizontalLineTo(6.542f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.395f)
                quadToRelative(-0.396f, -0.396f, -0.396f, -0.938f)
                quadToRelative(0f, -0.542f, 0.396f, -0.938f)
                quadToRelative(0.396f, -0.395f, 0.938f, -0.395f)
                horizontalLineToRelative(7.166f)
                quadToRelative(0.542f, 0f, 0.938f, 0.395f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.938f)
                quadToRelative(0f, 0.583f, -0.396f, 0.958f)
                reflectiveQuadToRelative(-0.938f, 0.375f)
                close()
                moveTo(33.5f, 12.792f)
                horizontalLineTo(6.542f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.959f)
                quadToRelative(0f, -0.541f, 0.396f, -0.937f)
                reflectiveQuadToRelative(0.938f, -0.396f)
                horizontalLineTo(33.5f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.584f, -0.375f, 0.959f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveToRelative(-9.875f, 8.25f)
                horizontalLineTo(6.542f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.959f)
                quadToRelative(0f, -0.541f, 0.396f, -0.916f)
                reflectiveQuadToRelative(0.938f, -0.375f)
                horizontalLineToRelative(17.083f)
                quadToRelative(0.542f, 0f, 0.917f, 0.395f)
                quadToRelative(0.375f, 0.396f, 0.375f, 0.938f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
            }
        }.build()
    }
}