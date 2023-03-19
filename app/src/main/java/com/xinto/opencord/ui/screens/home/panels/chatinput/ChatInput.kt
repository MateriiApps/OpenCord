package com.xinto.opencord.ui.screens.home.panels.chatinput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.OCBasicTextField
import com.xinto.opencord.ui.viewmodel.ChatInputViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    hint: @Composable () -> Unit,
    viewModel: ChatInputViewModel = getViewModel(),
) {
    val isEmpty by remember { derivedStateOf { viewModel.pendingContent.isEmpty() } }
    val sendEnabled by remember { derivedStateOf { !isEmpty && viewModel.sendEnabled } }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OCBasicTextField(
            modifier = Modifier.weight(1f),
            value = viewModel.pendingContent,
            onValueChange = viewModel::setPendingMessage,
            maxLines = 7,
            decorationBox = { innerTextField ->
                InputInnerTextField(
                    isEmpty = isEmpty,
                    hint = hint,
                    innerTextField = innerTextField,
                )
            },
        )

        AnimatedSendButton(
            visible = sendEnabled,
            enabled = viewModel.sendEnabled,
            onClick = viewModel::sendMessage,
        )
    }
}

@Composable
private fun InputInnerTextField(
    isEmpty: Boolean,
    hint: @Composable () -> Unit,
    innerTextField: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp),
            ) {
                innerTextField()
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.medium,
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                ) {
                    if (isEmpty) {
                        hint()
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedSendButton(
    visible: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally { it * 2 },
        exit = slideOutHorizontally { it * 2 },
    ) {
        FilledIconButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            Icon(
                imageVector = Icons.Rounded.Send,
                contentDescription = null,
            )
        }
    }
}
