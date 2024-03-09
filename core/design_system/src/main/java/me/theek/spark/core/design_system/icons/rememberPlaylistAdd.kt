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
fun rememberPlaylistAdd(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "playlist_add",
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
                moveTo(28.25f, 33.125f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                verticalLineToRelative(-5.541f)
                horizontalLineToRelative(-5.541f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.396f)
                reflectiveQuadToRelative(-0.375f, -0.938f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadToRelative(0.959f, -0.396f)
                horizontalLineToRelative(5.541f)
                verticalLineToRelative(-5.583f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                quadToRelative(0.541f, 0f, 0.916f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(5.583f)
                horizontalLineToRelative(5.584f)
                quadToRelative(0.541f, 0f, 0.916f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.542f, -0.375f, 0.938f)
                quadToRelative(-0.375f, 0.396f, -0.916f, 0.396f)
                horizontalLineToRelative(-5.584f)
                verticalLineToRelative(5.541f)
                quadToRelative(0f, 0.542f, -0.395f, 0.917f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveTo(6.542f, 26.292f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.396f)
                quadToRelative(-0.396f, -0.396f, -0.396f, -0.938f)
                quadToRelative(0f, -0.541f, 0.396f, -0.937f)
                reflectiveQuadToRelative(0.938f, -0.396f)
                horizontalLineToRelative(9.583f)
                quadToRelative(0.542f, 0f, 0.937f, 0.396f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.937f)
                quadToRelative(0f, 0.542f, -0.396f, 0.938f)
                quadToRelative(-0.395f, 0.396f, -0.937f, 0.396f)
                close()
                moveToRelative(0f, -6.75f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.396f)
                quadToRelative(-0.396f, -0.396f, -0.396f, -0.938f)
                quadToRelative(0f, -0.541f, 0.396f, -0.937f)
                reflectiveQuadToRelative(0.938f, -0.396f)
                horizontalLineTo(23f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.542f, -0.375f, 0.938f)
                quadToRelative(-0.375f, 0.396f, -0.917f, 0.396f)
                close()
                moveToRelative(0f, -6.75f)
                quadToRelative(-0.542f, 0f, -0.938f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.959f)
                quadToRelative(0f, -0.541f, 0.396f, -0.916f)
                reflectiveQuadToRelative(0.938f, -0.375f)
                horizontalLineTo(23f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                quadToRelative(0.375f, 0.395f, 0.375f, 0.937f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                quadToRelative(-0.375f, 0.375f, -0.917f, 0.375f)
                close()
            }
        }.build()
    }
}