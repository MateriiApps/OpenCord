package com.xinto.opencord.ui.screens.mentions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
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
import com.xinto.opencord.ui.screens.home.panels.messagemenu.MessageMenu
import com.xinto.opencord.ui.util.*
import com.xinto.opencord.ui.viewmodel.MentionsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MentionsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MentionsViewModel = getViewModel(),
) {
    var messageMenuTarget by remember { mutableStateOf<Long?>(null) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val filterMenuState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


    if (messageMenuTarget != null) {
        MessageMenu(
            messageId = messageMenuTarget!!,
            onDismiss = { messageMenuTarget = null },
        )
    }

    if (filterMenuState.isVisible || filterMenuState.targetValue != SheetValue.Hidden) {
        ModalBottomSheet(
            sheetState = filterMenuState,
            onDismissRequest = {
                scope.launch {
                    filterMenuState.hide()
                }
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 30.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                    )

                    Text(
                        text = "Mention Filters",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                Divider(thickness = 1.dp)

                val filterItems = remember(
                    viewModel.includeEveryone,
                    viewModel.includeRoles,
                    viewModel.includeAllServers,
                ) {
                    arrayOf(
                        Triple("Include @everyone mentions", viewModel.includeEveryone, viewModel::toggleEveryone),
                        Triple("Include role mentions", viewModel.includeRoles, viewModel::toggleRoles),
                        Triple("Include mentions from all servers", viewModel.includeAllServers, viewModel::toggleCurrentServer),
                    )
                }

                for ((name, isEnabled, onClick) in filterItems) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onClick)
                            .padding(vertical = 2.dp)
                            .padding(start = 6.dp),
                    ) {
                        Text(
                            text = name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f, fill = false),
                        )

                        Checkbox(
                            checked = isEnabled,
                            onCheckedChange = { onClick() },
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Recent mentions",
                            modifier = Modifier,
                        )

                        val serverName = if (!viewModel.includeAllServers && viewModel.currentGuildName != null) {
                            viewModel.currentGuildName ?: ""
                        } else {
                            "All servers"
                        }

                        AnimatedContent(
                            targetState = serverName,
                            transitionSpec = {
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Up) with slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                            },
                        ) {
                            Text(
                                text = serverName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium)
                                    .offset(y = (-2).dp)
                                    .padding(bottom = 1.dp),
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.navigation_back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                filterMenuState.show()
                            }
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Open mention filters",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        val messages = viewModel.messages.collectAsLazyPagingItems()
        val refreshState = rememberPullRefreshState(
            refreshing = messages.loadState.refresh == LoadState.Loading,
            onRefresh = messages::refresh,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(refreshState)
                .padding(VoidablePaddingValues(paddingValues, bottom = false)),
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = CompositePaddingValues(
                    VoidablePaddingValues(paddingValues, top = false, left = false, right = false),
                    PaddingValues(
                        horizontal = 10.dp,
                        vertical = 4.dp,
                    ),
                ),
            ) {
                when (messages.loadState.refresh) {
                    LoadState.Loading -> {} // Handled by PullRefreshIndicator
                    is LoadState.NotLoading -> {
                        items(messages, key = { it.id }) { message ->
                            if (message != null) {
                                MentionsPageMessage(
                                    message = message,
                                    onLongClick = { messageMenuTarget = message.id },
                                    modifier = Modifier.fillParentMaxWidth(),
                                )
                            }
                        }
                    }
                    is LoadState.Error -> item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(bottom = 50.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("Failed to load")
                        }
                    }
                }

                when (messages.loadState.append) {
                    LoadState.Loading -> item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                    is LoadState.Error -> item {
                        Text("Failed to load")
                    }
                    else -> {}
                }
            }

            PullRefreshIndicator(
                refreshing = messages.loadState.refresh == LoadState.Loading,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun MentionsPageMessage(
    message: DomainMessage,
    onLongClick: () -> Unit,
    modifier: Modifier,
) {
    when (message) {
        is DomainMessageRegular -> {
            Surface(
                modifier = modifier,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 1.dp,
            ) {
                MessageRegular(
                    onLongClick = onLongClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
                                        text = referencedMessage.contentRendered,
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
                        MessageContent(
                            text = message.contentRendered,
                        )
                    },
                    embeds = message.embeds.ifNotEmptyComposable { embeds ->
                        for (embed in embeds) key(embed) {
                            Embed(
                                title = embed.title,
                                url = embed.url,
                                description = embed.description,
                                color = embed.color,
                                author = embed.author.ifNotNullComposable {
                                    EmbedAuthor(
                                        name = it.name,
                                        url = it.url,
                                        iconUrl = it.iconUrl,
                                    )
                                },
                                image = embed.image.ifNotNullComposable {
                                    AttachmentPicture(
                                        url = it.displayUrl,
                                        width = it.width ?: 500,
                                        height = it.height ?: 500,
                                        modifier = Modifier
                                            .heightIn(max = 400.dp),
                                    )
                                },
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
                        for (attachment in attachments) key(attachment) {
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
