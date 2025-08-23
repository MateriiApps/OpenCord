package com.xinto.opencord.ui.screens.mentions.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.toDomain
import com.xinto.opencord.rest.service.DiscordApiService

class MentionsPagingSource(
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
