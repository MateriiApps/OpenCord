package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.toDomain
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.manager.ToastManager
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class MentionsViewModel(
    persistentDataManager: PersistentDataManager,
    guilds: GuildStore,
    private val toasts: ToastManager,
    private val api: DiscordApiService,
) : BasePersistenceViewModel(persistentDataManager) {
    var includeRoles by mutableStateOf(true)
        private set
    var includeEveryone by mutableStateOf(true)
        private set
    var includeAllServers by mutableStateOf(true)
        private set
    var currentGuildName by mutableStateOf<String?>(null)
        private set

    var messages by mutableStateOf(emptyFlow<PagingData<DomainMessage>>())
        private set

    fun toggleRoles() {
        includeRoles = !includeRoles
        initPager()
    }

    fun toggleEveryone() {
        includeEveryone = !includeEveryone
        initPager()
    }

    fun toggleCurrentServer() {
        if (includeAllServers && persistentGuildId <= 0) {
            toasts.showToast("No server currently selected!")
        } else {
            includeAllServers = !includeAllServers
            initPager()
        }
    }

    init {
        initPager()

        if (persistentGuildId > 0) {
            viewModelScope.launch {
                val guild = guilds.fetchGuild(persistentGuildId)
                    ?: return@launch

                currentGuildName = guild.name
            }
        }
    }

    private fun initPager() {
        messages = Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 25,
                enablePlaceholders = false,
                initialLoadSize = 25,
            ),
            pagingSourceFactory = {
                val guildId = if (!includeAllServers) persistentGuildId else null
                MentionsPagingSource(api, includeRoles, includeEveryone, guildId)
            },
        ).flow.cachedIn(viewModelScope)
    }

    private class MentionsPagingSource(
        private val api: DiscordApiService,
        private val includeRoles: Boolean,
        private val includeEveryone: Boolean,
        private val guildId: Long?,
    ) : PagingSource<Long, DomainMessage>() {
        override fun getRefreshKey(state: PagingState<Long, DomainMessage>) = null

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, DomainMessage> {
            return try {
                val beforeMessageId = params.key
                val messages = api.getUserMentions(
                    includeRoles = includeRoles,
                    includeEveryone = includeEveryone,
                    guildId = guildId,
                    beforeId = beforeMessageId,
                )

                LoadResult.Page(
                    data = messages.map { it.toDomain() },
                    prevKey = null,
                    nextKey = if (messages.size < params.loadSize) {
                        null
                    } else {
                        messages.lastOrNull()?.id?.value
                    },
                )
            } catch (e: Exception) {
                e.printStackTrace()
                LoadResult.Error(e)
            }
        }
    }
}
