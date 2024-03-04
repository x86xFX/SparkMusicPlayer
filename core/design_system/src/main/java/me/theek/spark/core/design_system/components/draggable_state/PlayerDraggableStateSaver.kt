package me.theek.spark.core.design_system.components.draggable_state

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.unit.Density

/**
 * Saver used to save the state of a PlayerDraggableState between recompositions.
 */
internal class PlayerDraggableStateSaver(
    private val maxHeight: Float,
    private val density: Density
): Saver<PlayerDraggableState, BottomSheetStates> {
    override fun restore(value: BottomSheetStates): PlayerDraggableState {
        return PlayerDraggableState(
            maxHeight = maxHeight,
            density = density,
            initialValue = value
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun SaverScope.save(value: PlayerDraggableState): BottomSheetStates {
        return value.state.currentValue
    }
}