package me.theek.spark.feature.music_player.components.draggable_player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
fun rememberDraggablePlayerState(
    density: Density,
    constraintsScope: BoxWithConstraintsScope,
    rowHeight: Dp
): DraggablePlayerState {

    val maxHeight = with(density) { constraintsScope.maxHeight.toPx() }
    val rowHeightInPx = with(density) { rowHeight.toPx() }

    val draggablePlayerState = rememberSaveable(
        saver = DraggablePlayerStateSaver(
            density = density,
            maxHeight = maxHeight
        )
    ) {
        DraggablePlayerState(
            density = density,
            maxHeight = maxHeight - rowHeightInPx
        )
    }

    // Update maxHeight dynamically using SideEffect
    SideEffect {
        draggablePlayerState.updateMaxHeight(maxHeight - rowHeightInPx)
    }

    return draggablePlayerState
}

internal class DraggablePlayerStateSaver(
    private val density: Density,
    private val maxHeight: Float
) : Saver<DraggablePlayerState, DragAnchors> {

    override fun restore(value: DragAnchors): DraggablePlayerState {
        return DraggablePlayerState(
            density = density,
            maxHeight = maxHeight,
            initialValue = value
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun SaverScope.save(value: DraggablePlayerState): DragAnchors {
        return value.state.currentValue
    }
}