package com.xinto.opencord.ui.screens.home.panels.chat.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.OCBasicTextField

@Composable
fun HomeChatPanelInput(
    modifier: Modifier = Modifier,
    hint: @Composable () -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OCBasicTextField(
            modifier = Modifier.weight(1f),
            value = text,
            onValueChange = onTextChange,
            maxLines = 7,
            decorationBox = { innerTextField ->
                Surface(shape = MaterialTheme.shapes.large) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        innerTextField()
                        CompositionLocalProvider(
                            LocalContentColor provides LocalContentColor.current.copy(alpha = 0.7f),
                            LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                        ) {
                            if (text.isEmpty()) {
                                hint()
                            }
                        }
                    }
                }
            },
        )
        AnimatedVisibility(
            visible = text.isNotEmpty(),
            enter = slideInHorizontally { it * 2 },
            exit = slideOutHorizontally { it * 2 },
        ) {
            FilledIconButton(onClick = onSend) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = null,
                )
            }
        }
    }
}
