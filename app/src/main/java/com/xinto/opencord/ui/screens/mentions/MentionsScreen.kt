package com.xinto.opencord.ui.screens.mentions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinto.opencord.R
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.components.attachment.AttachmentVideo
import com.xinto.opencord.ui.components.embed.Embed
import com.xinto.opencord.ui.components.embed.EmbedAuthor
import com.xinto.opencord.ui.components.embed.EmbedField
import com.xinto.opencord.ui.components.message.MessageAuthor
import com.xinto.opencord.ui.components.message.MessageAvatar
import com.xinto.opencord.ui.components.message.MessageContent
import com.xinto.opencord.ui.components.message.MessageRegular
import com.xinto.opencord.ui.components.message.reply.MessageReferenced
import com.xinto.opencord.ui.components.message.reply.MessageReferencedAuthor
import com.xinto.opencord.ui.components.message.reply.MessageReferencedContent
import com.xinto.opencord.ui.viewmodel.MentionsViewModel
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render
import org.koin.androidx.compose.getViewModel

@Composable
fun MentionsScreen(
    onBackClick: () -> Unit,
    viewModel: MentionsViewModel = getViewModel(),
) {
    val messages = viewModel.messages.collectAsLazyPagingItems()
    val listState = messages.rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recent mentions") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.navigation_back),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
//            state = listState,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(messages, key = { it.id }) { message ->
                if (message != null) {
                    MessageTemp(
                        message = message,
                        modifier = Modifier.fillParentMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}

@Composable
fun MessageTemp(message: DomainMessage, modifier: Modifier) {
    when (message) {
        is DomainMessageRegular -> {
            Surface(
                modifier = modifier,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 1.dp,
            ) {
                MessageRegular(
                    modifier = Modifier.fillMaxWidth(),
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
                                        text = render(
                                            nodes = referencedMessage.contentNodes,
                                            renderContext = null,
                                        ).toAnnotatedString(),
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
                    content = message.contentNodes.ifNotEmptyComposable { nodes ->
                        MessageContent(
                            text = render(
                                builder = AnnotatedString.Builder(),
                                nodes = nodes,
                                renderContext = null,
                            ).toAnnotatedString(),
                        )
                    },
                    embeds = message.embeds.ifNotEmptyComposable { embeds ->
                        for (embed in embeds) {
                            Embed(
                                title = embed.title,
                                description = embed.description,
                                color = embed.color,
                                author = embed.author.ifNotNullComposable { EmbedAuthor(name = it) },
                                fields = embed.fields.ifNotNullComposable {
                                    for (field in it) {
                                        EmbedField(
                                            name = field.name,
                                            value = field.value,
                                        )
                                    }
                                },
                            )
                        }
                    },
                    attachments = message.attachments.ifNotEmptyComposable { attachments ->
                        for (attachment in attachments) {
                            when (attachment) {
                                is DomainPictureAttachment -> {
                                    AttachmentPicture(
                                        modifier = Modifier
                                            .heightIn(max = 250.dp),
                                        url = attachment.proxyUrl,
                                        width = attachment.width,
                                        height = attachment.height,
                                    )
                                }
                                is DomainVideoAttachment -> {
                                    AttachmentVideo(url = attachment.url)
                                }
                                else -> {}
                            }
                        }
                    },
                )
            }
        }
        else -> {}
    }
}
