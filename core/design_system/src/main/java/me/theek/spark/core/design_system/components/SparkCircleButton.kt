package me.theek.spark.core.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Circular shape button that used in mainly onboarding screen.
 * @param onClick = Button onclick event
 * @param icon = Centered icon for button.
 * @param contentDescription = Content description for icon
 * @param modifier = Modifier for button
 * @param backgroundColor = Background color for button, Default color is light green. (#89F28D)
 * @param tint = Icon color, Default color is dark green. (#005317)
 */
@Composable
fun SparkCircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF89F28D),
    tint: Color = Color(0xFF005317)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        FilledTonalButton(
            modifier = Modifier.size(50.dp),
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(contentColor = tint, containerColor = backgroundColor),
            contentPadding = PaddingValues(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SparkCircleButtonPreview() {
    SparkCircleButton(
        onClick = { },
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Next"
    )
}