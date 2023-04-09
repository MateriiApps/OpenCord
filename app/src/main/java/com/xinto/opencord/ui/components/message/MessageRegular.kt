package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.components.message.reply.MessageReplyBranch

@Composable
fun MessageRegular(
    modifier: Modifier = Modifier,
    mentioned: Boolean = false,
    topMerged: Boolean = false,
    bottomMerged: Boolean = false,
    shape: Shape = MaterialTheme.shapes.large,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    reply: (@Composable () -> Unit)? = null,
    avatar: (@Composable () -> Unit)? = null,
    author: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
    attachments: (@Composable () -> Unit)? = null,
    embeds: (@Composable () -> Unit)? = null,
    reactions: (@Composable () -> Unit)? = null,
) {
    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer

    Box(
        modifier = Modifier
            .clip(shape)
            .combinedClickable(
                enabled = onClick != null || onLongClick != null,
                onClick = {},
                onLongClick = onLongClick,
            )
            .then(modifier)
            .drawBehind {
                drawRect(if (mentioned) backgroundColor else Color.Unspecified)
            },
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(
                    start = 8.dp,
                    top = if (!topMerged) 8.dp else 1.5.dp,
                    end = 8.dp,
                    bottom = if (!bottomMerged) 8.dp else 1.5.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (reply != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    MessageReplyBranch(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .width(24.dp)
                            .fillMaxHeight(0.5f),
                    )
                    reply()
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                if (avatar != null) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        avatar()
                    }
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (author != null) {
                            author()
                        }
                        if (content != null) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                                content()
                            }
                        }
                    }
                    if (attachments != null) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            attachments()
                        }
                    }
                    if (embeds != null) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            embeds()
                        }
                    }
                    if (reactions != null) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            reactions()
                        }
                    }
                }
            }
        }
    }
}
