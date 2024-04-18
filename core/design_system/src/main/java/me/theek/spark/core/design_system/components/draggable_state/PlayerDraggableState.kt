package me.theek.spark.core.design_system.components.draggable_state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.ui.unit.Density

/**
 * State used to control the player view draggable actions.
 */
@OptIn(ExperimentalFoundationApi::class)
class PlayerDraggableState(
    val maxHeight: Float,
    density: Density,
    initialValue: BottomSheetStates = BottomSheetStates.MINIMISED
) : DraggableState(
    density = density,
    initialValue = initialValue,
    anchors = DraggableAnchors {
        BottomSheetStates.MINIMISED at maxHeight
        BottomSheetStates.EXPANDED at 0f
    }
)

enum class BottomSheetStates {
    EXPANDED,
    MINIMISED
}