package com.xinto.opencord.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CompositePaddingValues(private vararg val items: PaddingValues) : PaddingValues {
    override fun calculateBottomPadding(): Dp {
        return items.fold(0.dp) { acc, paddingValues ->
            acc + paddingValues.calculateBottomPadding()
        }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return items.fold(0.dp) { acc, paddingValues ->
            acc + paddingValues.calculateLeftPadding(layoutDirection)
        }
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return items.fold(0.dp) { acc, paddingValues ->
            acc + paddingValues.calculateRightPadding(layoutDirection)
        }
    }

    override fun calculateTopPadding(): Dp {
        return items.fold(0.dp) { acc, paddingValues ->
            acc + paddingValues.calculateTopPadding()
        }
    }
}
