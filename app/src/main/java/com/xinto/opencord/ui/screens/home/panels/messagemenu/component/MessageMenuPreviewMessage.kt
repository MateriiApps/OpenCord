package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.xinto.opencord.R
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.message.MessageAuthor
import com.xinto.opencord.ui.components.message.MessageAvatar
import com.xinto.opencord.ui.components.message.MessageContent
import com.xinto.opencord.ui.components.message.MessageRegular
import com.xinto.opencord.ui.components.message.reply.MessageReferenced
import com.xinto.opencord.ui.components.message.reply.MessageReferencedAuthor
import com.xinto.opencord.ui.components.message.reply.MessageReferencedContent
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha
import com.xinto.opencord.ui.util.ifComposable

@Composable
fun MessageMenuPreviewMessage(
    message: DomainMessage,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    when (message) {
        is DomainMessageRegular -> {
            MessageRegular(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(
                        enabled = onClick != null,
                        onClick = onClick ?: {},
                    ),
                mentioned = false,
                reply = message.isReply.ifComposable {
                    val referencedMessage = message.referencedMessage
                    if (referencedMessage != null) {
                        MessageReferenced(
                            avatar = {
                                MessageAvatar(url = referencedMessage.author.avatarUrl)
                            },
                            author = {
                                MessageReferencedAuthor(author = referencedMessage.author.username)
                            },
                            content = {
                                MessageReferencedContent(
                                    text = message.contentRendered,
                                )
                            },
                        )
                    } else {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(stringResource(R.string.message_reply_unknown))
                        }
                    }
                },
                avatar = {
                    MessageAvatar(url = message.author.avatarUrl)
                },
                author = {
                    MessageAuthor(
                        author = message.author.username,
                        timestamp = message.formattedTimestamp,
                        isEdited = message.isEdited,
                        isBot = message.author.bot,
                    )
                },
                content = {
                    if (message.contentRendered.isNotEmpty()) {
                        MessageContent(
                            text = message.contentRendered,
                        )
                    } else {
                        ProvideContentAlpha(ContentAlpha.high) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)) {
                                if (message.attachments.isNotEmpty()) {
                                    Text("${message.attachments.size} attachments")
                                } else if (message.embeds.isNotEmpty()) {
                                    Text("${message.embeds.size} embeds")
                                }
                            }
                        }
                    }
                },
            )
        }
        // TODO: render other message types
        else -> {}
    }
}
