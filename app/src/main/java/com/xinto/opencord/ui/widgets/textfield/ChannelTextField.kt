package com.xinto.opencord.ui.widgets.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
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
    Row(
        modifier = Modifier
            .padding(8.dp)
            .navigationBarsWithImePadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OpenCordBasicTextField(
            modifier = Modifier
                .weight(1f),
            value = value,
            onValueChange = onValueChange,
            maxLines = 4
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterVertically),
            visible = value.isNotBlank(),
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .then(Modifier.size(40.dp)),
                onClick = onSendClick
            ) {
                Icon(imageVector = Icons.Rounded.Send, contentDescription = "Send")
            }
        }
    }
}