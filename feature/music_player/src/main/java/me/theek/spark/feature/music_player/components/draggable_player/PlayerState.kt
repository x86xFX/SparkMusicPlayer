package me.theek.spark.feature.music_player.components.draggable_player

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
open class PlayerState(
    val density: Density,
    val initialValue: DragAnchors = DragAnchors.MINIMIZED,
    anchors: DraggableAnchors<DragAnchors>
) {
    val state = AnchoredDraggableState(
        initialValue = initialValue,
        anchors = anchors,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        animationSpec = tween(),
    )

    suspend fun animateTo(targetValue: DragAnchors) {
        this.state.animateTo(targetValue)
    }
}

@OptIn(ExperimentalFoundationApi::class)
class DraggablePlayerState(
    density: Density,
    private var maxHeight: Float,
    initialValue: DragAnchors = DragAnchors.MINIMIZED,
) : PlayerState(
    density = density,
    initialValue = initialValue,
    anchors = DraggableAnchors {
        DragAnchors.EXPANDED at maxHeight * 0f
        DragAnchors.MINIMIZED at maxHeight * 1f
    }
) {

    fun updateMaxHeight(newMaxHeight: Float) {
        if (maxHeight != newMaxHeight) {
            maxHeight = newMaxHeight
            state.updateAnchors(
                DraggableAnchors {
                    DragAnchors.EXPANDED at maxHeight * 0f
                    DragAnchors.MINIMIZED at maxHeight * 1f
                }
            )
        }
    }
}

enum class DragAnchors {
    EXPANDED,
    MINIMIZED
}