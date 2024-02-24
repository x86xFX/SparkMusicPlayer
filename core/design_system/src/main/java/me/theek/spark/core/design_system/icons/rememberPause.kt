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
fun rememberPause(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "pause",
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
                moveTo(24.5f, 31.458f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.791f)
                quadToRelative(-0.771f, -0.792f, -0.771f, -1.875f)
                verticalLineTo(11.208f)
                quadToRelative(0f, -1.083f, 0.771f, -1.875f)
                quadToRelative(0.771f, -0.791f, 1.854f, -0.791f)
                horizontalLineToRelative(4.292f)
                quadToRelative(1.083f, 0f, 1.875f, 0.791f)
                quadToRelative(0.791f, 0.792f, 0.791f, 1.875f)
                verticalLineToRelative(17.584f)
                quadToRelative(0f, 1.083f, -0.791f, 1.875f)
                quadToRelative(-0.792f, 0.791f, -1.875f, 0.791f)
                close()
                moveToRelative(-13.292f, 0f)
                quadToRelative(-1.083f, 0f, -1.875f, -0.791f)
                quadToRelative(-0.791f, -0.792f, -0.791f, -1.875f)
                verticalLineTo(11.208f)
                quadToRelative(0f, -1.083f, 0.791f, -1.875f)
                quadToRelative(0.792f, -0.791f, 1.875f, -0.791f)
                horizontalLineTo(15.5f)
                quadToRelative(1.083f, 0f, 1.854f, 0.791f)
                quadToRelative(0.771f, 0.792f, 0.771f, 1.875f)
                verticalLineToRelative(17.584f)
                quadToRelative(0f, 1.083f, -0.771f, 1.875f)
                quadToRelative(-0.771f, 0.791f, -1.854f, 0.791f)
                close()
            }
        }.build()
    }
}