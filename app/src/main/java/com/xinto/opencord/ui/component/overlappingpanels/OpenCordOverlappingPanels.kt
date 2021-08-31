package com.xinto.opencord.ui.component.overlappingpanels

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class OverlappingPanelValue {
    OpenRight, OpenLeft, Closed
}

@OptIn(ExperimentalMaterialApi::class)
class OverlappingPanelState(
    initialValue: OverlappingPanelValue,
    confirmStateChange: (OverlappingPanelValue) -> Boolean = { true }
) {

    val swipeableState = SwipeableState(
        initialValue = initialValue,
        animationSpec = spring(),
        confirmStateChange = confirmStateChange
    )

    val currentValue
        get() = swipeableState.currentValue

    val offset
        get() = swipeableState.offset

    val isClosed
        get() = currentValue == OverlappingPanelValue.Closed

    suspend fun closePanel() {
        swipeableState.animateTo(OverlappingPanelValue.Closed)
    }

    suspend fun openRightPanel() {
        swipeableState.animateTo(OverlappingPanelValue.OpenRight)
    }

    suspend fun openLeftPanel() {
        swipeableState.animateTo(OverlappingPanelValue.OpenLeft)
    }

    companion object {

        fun Saver(confirmStateChange: (OverlappingPanelValue) -> Boolean) =
            Saver<OverlappingPanelState, OverlappingPanelValue>(
                save = { it.currentValue },
                restore = { OverlappingPanelState(it, confirmStateChange) }
            )

    }
}

@Composable
fun rememberOverlappingPanelState(
    initialValue: OverlappingPanelValue,
    confirmStateChange: (OverlappingPanelValue) -> Boolean = { true }
): OverlappingPanelState {
    return rememberSaveable(saver = OverlappingPanelState.Saver(confirmStateChange)) {
        OverlappingPanelState(initialValue, confirmStateChange)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OpenCordOverlappingPanels(
    modifier: Modifier = Modifier,
    panelState: OverlappingPanelState = rememberOverlappingPanelState(initialValue = OverlappingPanelValue.Closed),
    panelLeft: @Composable BoxScope.() -> Unit,
    panelMiddle: @Composable BoxScope.() -> Unit,
    panelRight: @Composable BoxScope.() -> Unit,
) {

    val middlePanelAlpha by animateFloatAsState(if (panelState.isClosed) 1f else 0.8f)

    BoxWithConstraints(
        modifier = modifier
            .swipeable(
                panelState.swipeableState,
                orientation = Orientation.Horizontal,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                velocityThreshold = 400.dp,
                anchors = mapOf(
                    700f to OverlappingPanelValue.OpenLeft,
                    0f to OverlappingPanelValue.Closed,
                    -700f to OverlappingPanelValue.OpenRight
                )
            )
    ) {
        Box(
            modifier = Modifier.alpha(if (panelState.offset.value > 0f) 1f else 0f),
            content = panelLeft
        )
        Box(
            modifier = Modifier.alpha(if (panelState.offset.value < 0f) 1f else 0f),
            content = panelRight
        )
        Box(
            modifier = Modifier
                .alpha(middlePanelAlpha)
                .offset {
                    IntOffset(
                        x = panelState.offset.value.roundToInt(),
                        y = 0
                    )
                },
            content = panelMiddle
        )

    }
}