package me.theek.spark.core.design_system.components.draggable_state

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Manage the state of a Draggable element.
 */
@OptIn(ExperimentalFoundationApi::class)
open class DraggableState(
    val density: Density,
    val initialValue: BottomSheetStates = BottomSheetStates.MINIMISED,
    anchors: DraggableAnchors<BottomSheetStates>
) {
    val state = AnchoredDraggableState(
        initialValue = initialValue,
        anchors = anchors,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        animationSpec = tween(300),
    )

    /**
     * Change the draggable state by switching to a target value at a given velocity.
     */
    suspend fun animateTo(
        targetValue: BottomSheetStates,
        velocity: Float = 300F
    ) {
        this.state.animateTo(targetValue, velocity)
    }
}