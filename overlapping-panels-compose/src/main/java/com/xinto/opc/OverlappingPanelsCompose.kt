package com.xinto.opc

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Possible values for [OverlappingPanels]
 */
public enum class OverlappingPanelsValue {

    /**
     * The state of the overlapping panels when start panel is open.
     */
    OpenStart,

    /**
     * The state of the overlapping panels when end panel is open.
     */
    OpenEnd,

    /**
     * The state of the overlapping panels when both panels are closed.
     */
    Closed;

}

/**
 * State of the [OverlappingPanels] composable.
 *
 * @param initialValue The initial value of the state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
public class OverlappingPanelsState(
    initialValue: OverlappingPanelsValue,
    confirmStateChange: (OverlappingPanelsValue) -> Boolean = { true },
) {

    public val swipeableState: SwipeableState<OverlappingPanelsValue> =
        SwipeableState(
            initialValue = initialValue,
            animationSpec = spring(),
            confirmStateChange = confirmStateChange
        )


    /**
     * Current [value][OverlappingPanelsValue]
     */
    public val currentValue: OverlappingPanelsValue
        get() = swipeableState.currentValue

    /**
     * Target [value][OverlappingPanelsValue]
     */
    public val targetValue: OverlappingPanelsValue
        get() = swipeableState.targetValue


    /**
     * Center panel offset
     */
    public val offset: State<Float>
        get() = swipeableState.offset

    public val offsetIsPositive: Boolean
        get() = offset.value > 0f

    public val offsetIsNegative: Boolean
        get() = offset.value < 0f

    public val offsetNotZero: Boolean
        get() = offset.value != 0f

    public val panelsClosed: Boolean
        get() = currentValue == OverlappingPanelsValue.Closed

    public val endPanelOpen: Boolean
        get() = currentValue == OverlappingPanelsValue.OpenStart

    public val startPanelOpen: Boolean
        get() = currentValue == OverlappingPanelsValue.OpenEnd

    /**
     * Open the start panel with animation.
     */
    public suspend fun openStartPanel() {
        swipeableState.animateTo(OverlappingPanelsValue.OpenEnd)
    }

    /**
     * Open the end panel with animation.
     */
    public suspend fun openEndPanel() {
        swipeableState.animateTo(OverlappingPanelsValue.OpenStart)
    }

    /**
     * Close the panels with animation.
     */
    public suspend fun closePanels() {
        swipeableState.animateTo(OverlappingPanelsValue.Closed)
    }

    public companion object {

        public fun Saver(
            confirmStateChange: (OverlappingPanelsValue) -> Boolean
        ): Saver<OverlappingPanelsState, OverlappingPanelsValue> {
            return Saver(
                save = { it.currentValue },
                restore = { OverlappingPanelsState(it, confirmStateChange) }
            )
        }
    }
}

/**
 * @param initialValue Initial state of the panels, can only be one of [OverlappingPanelsValue]
 * @param confirmStateChange Whether to consume the change.
 */
@ExperimentalMaterialApi
@Composable
public fun rememberOverlappingPanelsState(
    initialValue: OverlappingPanelsValue = OverlappingPanelsValue.Closed,
    confirmStateChange: (OverlappingPanelsValue) -> Boolean = { true },
): OverlappingPanelsState {
    return rememberSaveable(saver = OverlappingPanelsState.Saver(confirmStateChange)) {
        OverlappingPanelsState(initialValue, confirmStateChange)
    }
}

/**
 * @param panelStart Content for the start panel (swapped with `panelEnd` for the RTL layout).
 * @param panelCenter Content for the center panel.
 * @param panelEnd Content for the center panel (swapped with `panelStart` for the RTL layout).
 * @param panelsState state of the Overlapping Panels.
 * @param modifier optional modifier for the Overlapping Panels.
 * @param gesturesEnabled Whether to enable swipe gestures.
 * @param velocityThreshold Minimum swipe speed required to open/close side panels.
 * @param resistance Controls how much resistance will be applied when swiping past the bounds.
 * @param sidePanelWidthFraction Maximum width in fractions for side panels to occupy when opened.
 * @param centerPanelAlpha Opacity of the center panel when side panels are closed and opened.
 * @param centerPanelElevation Elevation of the center panel.
 */
@ExperimentalMaterialApi
@Composable
public fun OverlappingPanels(
    panelStart: @Composable BoxScope.() -> Unit,
    panelCenter: @Composable BoxScope.() -> Unit,
    panelEnd: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    panelsState: OverlappingPanelsState = rememberOverlappingPanelsState(initialValue = OverlappingPanelsValue.Closed),
    gesturesEnabled: Boolean = true,
    velocityThreshold: Dp = 400.dp,
    resistance: (anchors: Set<Float>) -> ResistanceConfig? = { null },
    sidePanelWidthFraction: SidePanelWidthFraction = PanelDefaults.sidePanelWidthFraction(),
    centerPanelAlpha: CenterPanelAlpha = PanelDefaults.centerPanelAlpha(),
    centerPanelElevation: Dp = 8.dp,
) {
    val resources = LocalContext.current.resources
    val layoutDirection = LocalLayoutDirection.current

    BoxWithConstraints(modifier.fillMaxSize()) {
        val fraction =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                sidePanelWidthFraction.portrait()
            else
                sidePanelWidthFraction.landscape()

        val offsetValue = (constraints.maxWidth * fraction) + PanelDefaults.MarginBetweenPanels.value

        //TODO make animation configurable
        val animatedCenterPanelAlpha by animateFloatAsState(
            targetValue =
            if (abs(panelsState.offset.value) == abs(offsetValue))
                centerPanelAlpha.sidesOpened()
            else
                centerPanelAlpha.sidesClosed(),
        )

        val anchors = mapOf(
            offsetValue to OverlappingPanelsValue.OpenEnd,
            0f to OverlappingPanelsValue.Closed,
            -offsetValue to OverlappingPanelsValue.OpenStart
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .swipeable(
                    state = panelsState.swipeableState,
                    orientation = Orientation.Horizontal,
                    velocityThreshold = velocityThreshold,
                    anchors = anchors,
                    enabled = gesturesEnabled,
                    reverseDirection = layoutDirection == LayoutDirection.Rtl,
                    resistance = resistance(anchors.keys),
                )
        ) {
            val sidePanelAlignment = organizeSidePanel(
                panelsState,
                onStartPanel = { Alignment.CenterStart },
                onEndPanel = { Alignment.CenterEnd },
                onNeither = { Alignment.Center }
            )
            val sidePanelContent = organizeSidePanel(
                panelsState,
                onStartPanel = { panelStart },
                onEndPanel = { panelEnd },
                onNeither = { {} }
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction)
                    .align(sidePanelAlignment),
                content = sidePanelContent
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .alpha(animatedCenterPanelAlpha)
                    .offset {
                        IntOffset(
                            x = panelsState.offset.value.roundToInt(),
                            y = 0
                        )
                    }
                    .shadow(centerPanelElevation),
                content = panelCenter
            )
        }
    }
}

public interface SidePanelWidthFraction {

    @Composable
    public fun portrait(): Float

    @Composable
    public fun landscape(): Float

}

public interface CenterPanelAlpha {

    @Composable
    public fun sidesOpened(): Float

    @Composable
    public fun sidesClosed(): Float

}

@ExperimentalMaterialApi
private inline fun <T> organizeSidePanel(
    panelsState: OverlappingPanelsState,
    onStartPanel: () -> T,
    onEndPanel: () -> T,
    onNeither: () -> T,
) = when {
    panelsState.offsetIsPositive -> onStartPanel()
    panelsState.offsetIsNegative -> onEndPanel()
    else -> onNeither()
}

public object PanelDefaults {

    public val MarginBetweenPanels: Dp = 16.dp

    /**
     * @param portrait Fraction to use when the device is in portrait mode.
     * @param landscape Fraction to use when the device is in landscape mode.
     */
    @Composable
    public fun sidePanelWidthFraction(
        portrait: Float = 0.85f,
        landscape: Float = 0.45f,
    ): SidePanelWidthFraction = DefaultSidePanelWidthFraction(
        portrait = portrait,
        landscape = landscape,
    )

    /**
     * @param sidesOpened Alpha to use when any of the side panels are opened.
     * @param sidesClosed Alpha to use when any of the side panels are closed.
     */
    @Composable
    public fun centerPanelAlpha(
        sidesOpened: Float = 0.7f,
        sidesClosed: Float = 1f
    ): CenterPanelAlpha = DefaultCenterPanelAlpha(
        sidesOpened = sidesOpened,
        sidesClosed = sidesClosed,
    )

}

private class DefaultSidePanelWidthFraction(
    private val portrait: Float,
    private val landscape: Float,
) : SidePanelWidthFraction {

    @Composable
    override fun portrait() = portrait

    @Composable
    override fun landscape() = landscape

}

private class DefaultCenterPanelAlpha(
    private val sidesOpened: Float,
    private val sidesClosed: Float,
) : CenterPanelAlpha {

    @Composable
    override fun sidesOpened() = sidesOpened

    @Composable
    override fun sidesClosed() = sidesClosed

}