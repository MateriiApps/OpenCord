package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.component.FilledIconButton
import com.xinto.opencord.ui.component.OCBasicTextField

@Composable
fun WidgetChatInput(
    value: String,
    onValueChange: (value: String) -> Unit,
    sendEnabled: Boolean,
    onSendClick: () -> Unit,
    hint: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OCBasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            maxLines = 4,
        )
        AnimatedVisibility(
            modifier = Modifier.size(48.dp),
            visible = value.isNotBlank(),
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
        ) {
            FilledIconButton(
                onClick = onSendClick,
                enabled = sendEnabled
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = null
                )
            }
        }
    }
}