package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiChannel

sealed class DomainChannel(
    val channelId: Long,
    val channelName: String,
    val channelPosition: Int,
) : DomainResponse {

    data class TextChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel(id, name, position)

    data class VoiceChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
    ) : DomainChannel(id, name, position)

    data class AnnouncementChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel(id, name, position)

    data class Category(
        val id: Long,
        val name: String,
        val position: Int,
    ) : DomainChannel(id, name, position)

    companion object {

        fun fromApi(
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
