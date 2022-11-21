package com.xinto.opencord.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainMessageRegular
import com.xinto.opencord.ui.viewmodel.ChannelPinsViewModel
import com.xinto.opencord.ui.widget.*
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render
import org.koin.androidx.compose.getViewModel

@Composable
fun ChannelPinsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChannelPinsViewModel = getViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(R.string.pins_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        when (viewModel.state) {
            is ChannelPinsViewModel.State.Loading -> {
                ChannelPinsLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is ChannelPinsViewModel.State.Loaded -> {
                ChannelPinsLoaded(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    pins = viewModel.pins.values.sortedByDescending {
                        it.timestamp
                    }
                )
            }
            is ChannelPinsViewModel.State.Error -> {
                ChannelPinsError(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun ChannelPinsLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ChannelPinsLoaded(
    pins: List<DomainMessage>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(pins) { message ->
            when (message) {
                is DomainMessageRegular -> {
                    Surface(
                        modifier = Modifier.fillParentMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 1.dp
                    ) {
                        WidgetChatMessage(
                            modifier = Modifier.fillMaxWidth(),
                            reply = message.isReply.ifComposable {
                                val referencedMessage = message.referencedMessage
                                if (referencedMessage != null) {
                                    WidgetMessageReply(
                                        avatar = {
                                            WidgetMessageAvatar(url = referencedMessage.author.avatarUrl)
                                        },
                                        author = {
                                            WidgetMessageReplyAuthor(author = referencedMessage.author.username)
                                        },
                                        content = {
                                            WidgetMessageReplyContent(
                                                text = render(
                                                    nodes = referencedMessage.contentNodes,
                                                    renderContext = null
                                                ).toAnnotatedString()
                                            )
                                        }
                                    )
                                } else {
                                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                                        Text(stringResource(R.string.message_reply_unknown))
                                    }
                                }
                            },
                            avatar = {
                                WidgetMessageAvatar(url = message.author.avatarUrl)
                            },
                            author = {
                                WidgetMessageAuthor(
                                    author = message.author.username,
                                    timestamp = message.formattedTimestamp,
                                    edited = message.isEdited
                                )
                            },
                            content = message.contentNodes.ifNotEmptyComposable { nodes ->
                                WidgetMessageContent(
                                    text = render(
                                        builder = AnnotatedString.Builder(),
                                        nodes = nodes,
                                        renderContext = null
                                    ).toAnnotatedString()
                                )
                            },
                            embeds = message.embeds.ifNotEmptyComposable { embeds ->
                                for (embed in embeds) {
                                    WidgetEmbed(
                                        title = embed.title,
                                        description = embed.description,
                                        color = embed.color,
                                        author = embed.author.ifNotNullComposable {
                                            WidgetEmbedAuthor(name = it.name)
                                        },
                                        fields = embed.fields.ifNotNullComposable {
                                            for (field in it) {
                                                WidgetEmbedField(
                                                    name = field.name,
                                                    value = field.value
                                                )
                                            }
                                        }
                                    )
                                }
                            },
                            attachments = message.attachments.ifNotEmptyComposable { attachments ->
                                for (attachment in attachments) {
                                    when (attachment) {
                                        is DomainAttachment.Picture -> {
                                            WidgetAttachmentPicture(
                                                modifier = Modifier
                                                    .heightIn(max = 250.dp),
                                                url = attachment.proxyUrl,
                                                width = attachment.width,
                                                height = attachment.height
                                            )
                                        }
                                        is DomainAttachment.Video -> {
                                            WidgetAttachmentVideo(url = attachment.url)
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ChannelPinsError(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.error,
            LocalTextStyle provides MaterialTheme.typography.titleMedium
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.pins_loading_error),
                textAlign = TextAlign.Center
            )
        }
    }
}