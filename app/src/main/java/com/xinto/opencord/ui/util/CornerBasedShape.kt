package com.xinto.opencord.ui.util

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.LayoutDirection

object EmptyCornerBasedShape : CornerBasedShape(
    topStart = CornerSize(0f),
    topEnd = CornerSize(0f),
    bottomStart = CornerSize(0f),
    bottomEnd = CornerSize(0f),
) {

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ): CornerBasedShape {
        return this
    }

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ): Outline {
        return Outline.Generic(Path())
    }
}