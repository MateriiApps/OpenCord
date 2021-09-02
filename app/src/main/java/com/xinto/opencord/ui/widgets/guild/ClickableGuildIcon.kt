package com.xinto.opencord.ui.widgets.guild

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ClickableGuildIcon(
    iconUrl: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val onBackground = MaterialTheme.colors.onBackground
    val imagePainter = rememberImagePainter(iconUrl)


    val indicatorFraction by animateFloatAsState(if (selected) 0.7f else 0.15f)

    val imageCornerRadius by animateIntAsState(
        targetValue = if (selected) 25 else 50,
        animationSpec = tween(400)
    )
    Row(
        modifier = Modifier.height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(4.dp)
                .fillMaxHeight(indicatorFraction),
            backgroundColor = onBackground,
            shape = RoundedCornerShape(
                topStartPercent = 0,
                bottomStartPercent = 0,
                topEndPercent = 100,
                bottomEndPercent = 100,
            )
        ) {}
        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(imageCornerRadius))
                .clickable(onClick = onClick),
            painter = imagePainter,
            contentDescription = "Guild Icon"
        )
    }
}