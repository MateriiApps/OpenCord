package com.xinto.opencord.ui.screens.mentions

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.manager.ToastManager
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.PersistentDataStore
import com.xinto.opencord.ui.screens.mentions.model.MentionsPagingSource
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.flow.emptyFlow

@Stable
class MentionsViewModel(
    guilds: GuildStore,
    persistentDataStore: PersistentDataStore,
    private val toasts: ToastManager,
    private val api: DiscordApiService,
) : ViewModel() {
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

    private var guildId = 0L

    fun toggleRoles() {
        includeRoles = !includeRoles
        initPager()
    }

    fun toggleEveryone() {
        includeEveryone = !includeEveryone
        initPager()
    }

    fun toggleCurrentServer() {
        if (includeAllServers && guildId <= 0) {
            toasts.showToast("No server currently selected!")
        } else {
            includeAllServers = !includeAllServers
            initPager()
        }
    }

    init {
        initPager()

        persistentDataStore.observeCurrentGuild()
            .collectIn(viewModelScope) {
                guildId = it

                if (it <= 0) return@collectIn

                val guild = guilds.fetchGuild(guildId)
                    ?: return@collectIn

                currentGuildName = guild.name
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
                val guildId = if (!includeAllServers) guildId else null
                MentionsPagingSource(api, includeRoles, includeEveryone, guildId)
            },
        ).flow.cachedIn(viewModelScope)
    }

}
