package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.toDomain
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.flow.emptyFlow

class MentionsViewModel(
    val api: DiscordApiService,
) : ViewModel() {
    var includeRoles by mutableStateOf(true)
        private set
    var includeEveryone by mutableStateOf(true)
        private set

    var messages by mutableStateOf(emptyFlow<PagingData<DomainMessage>>())
        private set

    fun toggleIncludeRoles() {
        includeRoles = !includeRoles
        initPager()
    }

    fun toggleIncludeEveryone() {
        includeEveryone = !includeEveryone
        initPager()
    }

    init {
        initPager()
    }

    private fun initPager() {
        messages = Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = {
                MentionsPagingSource(api, includeRoles, includeEveryone, null)
            },
        ).flow.cachedIn(viewModelScope)
    }

    private companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 25,
            prefetchDistance = 0,
            enablePlaceholders = true,
            initialLoadSize = 25,
        )
    }

    private class MentionsPagingSource(
        private val api: DiscordApiService,
        private val includeRoles: Boolean,
        private val includeEveryone: Boolean,
        private val guildId: Long?,
    ) : PagingSource<Long, DomainMessage>() {
        override fun getRefreshKey(state: PagingState<Long, DomainMessage>): Long? {
            return null
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, DomainMessage> {
            return try {
                val messageId = params.key
                val messages = api.getUserMentions(
                    includeRoles = includeRoles,
                    includeEveryone = includeEveryone,
                    guildId = guildId,
                    beforeId = messageId,
                )

                LoadResult.Page(
                    data = messages.map { it.toDomain() },
                    prevKey = messageId?.minus(1),
                    nextKey = if (messages.isEmpty()) null else messages.lastOrNull()?.id?.value,
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }
}
