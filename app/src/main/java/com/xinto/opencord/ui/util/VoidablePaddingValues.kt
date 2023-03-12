package com.xinto.opencord.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Disable any (top/bottom/left/right) padding for [PaddingValues],
 * without touching the rest of the values.
 */
class VoidablePaddingValues(
    private val existing: PaddingValues,
    private val top: Boolean = true,
    private val bottom: Boolean = true,
    private val left: Boolean = true,
    private val right: Boolean = true,
) : PaddingValues {
    override fun calculateBottomPadding(): Dp {
        return if (!bottom) 0.dp else {
            existing.calculateBottomPadding()
        }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return if (!left) 0.dp else {
            existing.calculateLeftPadding(layoutDirection)
        }
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return if (!right) 0.dp else {
            existing.calculateRightPadding(layoutDirection)
        }
    }

    override fun calculateTopPadding(): Dp {
        return if (!top) 0.dp else {
            existing.calculateTopPadding()
        }
    }
}
