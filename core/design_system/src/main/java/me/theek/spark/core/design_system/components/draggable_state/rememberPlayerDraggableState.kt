package me.theek.spark.core.design_system.components.draggable_state

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity

/**
 * Create a PlayerDraggableState for managing the draggable state of the player view.
 * It also manages the state of the PlayerDraggableState between recompositions.
 */
@Composable
fun rememberPlayerDraggableState(constraintsScope: BoxWithConstraintsScope): PlayerDraggableState {
    val density = LocalDensity.current
    val maxHeight = with(LocalDensity.current) {
        constraintsScope.maxHeight.toPx()
    }
    return rememberSaveable(
        saver = PlayerDraggableStateSaver(
            maxHeight = maxHeight,
            density = density
        )
    ) {
        PlayerDraggableState(
            maxHeight = maxHeight,
            density = density
        )
    }
}