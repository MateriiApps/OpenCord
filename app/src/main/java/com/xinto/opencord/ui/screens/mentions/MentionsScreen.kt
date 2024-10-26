package com.xinto.opencord.ui.screens.mentions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.ui.navigation.AppNavigator
import com.xinto.opencord.ui.screens.home.panels.messagemenu.HomeMessageMenu
import com.xinto.opencord.ui.screens.mentions.component.MentionsFilterMenu
import com.xinto.opencord.ui.screens.mentions.component.MentionsPageMessage
import com.xinto.opencord.ui.screens.mentions.component.MentionsTopBar
import com.xinto.opencord.ui.util.CompositePaddingValues
import com.xinto.opencord.ui.util.VoidablePaddingValues
import dev.olshevski.navigation.reimagined.pop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MentionsScreen(
    modifier: Modifier = Modifier,
    navigator: AppNavigator,
) {
    val viewModel: MentionsViewModel = koinViewModel()
    MentionsScreen(
        modifier = modifier,
        onBackClick = {
            navigator.pop()
        },
        currentGuildName = viewModel.currentGuildName,
        messages = viewModel.messages,
        includeEveryone = viewModel.includeEveryone,
        onToggleEveryone = viewModel::toggleEveryone,
        includeRoles = viewModel.includeRoles,
        onToggleRoles = viewModel::toggleRoles,
        includeAllServers = viewModel.includeAllServers,
        onToggleAllServers = viewModel::toggleCurrentServer,
    )
}

@Composable
fun MentionsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    currentGuildName: String?,
    messages: Flow<PagingData<DomainMessage>>,
    includeEveryone: Boolean,
    onToggleEveryone: () -> Unit,
    includeRoles: Boolean,
    onToggleRoles: () -> Unit,
    includeAllServers: Boolean,
    onToggleAllServers: () -> Unit
) {
    var messageMenuTarget by remember { mutableStateOf<Long?>(null) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val filterMenuState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (messageMenuTarget != null) {
        HomeMessageMenu(
            messageId = messageMenuTarget!!,
            onDismiss = { messageMenuTarget = null },
        )
    }

    if (filterMenuState.isVisible || filterMenuState.targetValue != SheetValue.Hidden) {
        MentionsFilterMenu(
            filterMenuState = filterMenuState,
            onDismissRequest = {
                scope.launch {
                    filterMenuState.hide()
                }
            },
            includeEveryone = includeEveryone,
            includeRoles = includeRoles,
            includeAllServers = includeAllServers,
            onToggleEveryone = onToggleEveryone,
            onToggleRoles = onToggleRoles,
            onToggleAllServers = onToggleAllServers
        )
    }

    Scaffold(
        topBar = {
            MentionsTopBar(
                includeAllServers = includeAllServers,
                currentGuildName = currentGuildName,
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
                onFilterClick = {
                    scope.launch {
                        filterMenuState.show()
                    }
                }
            )
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        val messages = messages.collectAsLazyPagingItems()
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

