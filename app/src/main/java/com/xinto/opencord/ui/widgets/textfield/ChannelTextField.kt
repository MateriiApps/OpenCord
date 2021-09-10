package com.xinto.opencord.ui.widgets.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.xinto.opencord.ui.component.textfield.OpenCordBasicTextField

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChannelTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    onSendClick: () -> Unit
) {
    val shapePercent by animateIntAsState(
        targetValue = when (value.lines().size) {
            0 -> 50
            1 -> 50
            2 -> 40
            3 -> 30
            else -> 25
        }
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .navigationBarsWithImePadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OpenCordBasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(shapePercent),
            maxLines = 4
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterVertically),
            visible = value.isNotBlank(),
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .clickable(onClick = onSendClick)
                    .size(36.dp),
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}