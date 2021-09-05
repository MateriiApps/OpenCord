package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiChannel

sealed class DomainChannel : DomainResponse {

    data class TextChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class VoiceChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
    ) : DomainChannel()

    data class AnnouncementChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class Category(
        val id: Long,
        val name: String,
        val position: Int,
    ) : DomainChannel()

    companion object {

        fun toDomainChannel(
            apiChannel: ApiChannel
        ) = with(apiChannel) {
            when (type) {
                2 -> VoiceChannel(
                    id = id.toLong(),
                    name = name,
                    position = position,
                    parentId = parent_id
                )
                4 -> Category(
                    id = id.toLong(),
                    name = name,
                    position = position,
                )
                5 -> AnnouncementChannel(
                    id = id.toLong(),
                    name = name,
                    position = position,
                    parentId = parent_id,
                    nsfw = nsfw
                )
                else -> TextChannel(
                    id = id.toLong(),
                    name = name,
                    position = position,
                    parentId = parent_id,
                    nsfw = nsfw
                )
            }
        }


    }

}
