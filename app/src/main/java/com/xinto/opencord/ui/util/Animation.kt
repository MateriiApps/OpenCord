package com.xinto.opencord.ui.util

import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity

@Composable
fun animateCornerBasedShapeAsState(targetValue: CornerBasedShape): State<CornerBasedShape> {
    val size = Size.Unspecified
    val density = LocalDensity.current

    return animateValueAsState(
        targetValue = targetValue,
        typeConverter = TwoWayConverter(
            convertToVector = {
                AnimationVector(
                    v1 = it.topStart.toPx(size, density),
                    v2 = it.topEnd.toPx(size, density),
                    v3 = it.bottomStart.toPx(size, density),
                    v4 = it.bottomEnd.toPx(size, density),
                )
            },
            convertFromVector = {
                when (targetValue) {
                    is RoundedCornerShape -> {
                        RoundedCornerShape(
                            topStart = it.v1,
                            topEnd = it.v2,
                            bottomStart = it.v3,
                            bottomEnd = it.v4,
                        )
                    }
                    is CutCornerShape -> {
                        CutCornerShape(
                            topStart = it.v1,
                            topEnd = it.v2,
                            bottomStart = it.v3,
                            bottomEnd = it.v4,
                        )
                    }
                    else -> EmptyCornerBasedShape
                }
            },
        ),
    )
}
