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
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 25,
                enablePlaceholders = false,
                initialLoadSize = 25,
            ),
            pagingSourceFactory = {
                MentionsPagingSource(api, includeRoles, includeEveryone, null)
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
