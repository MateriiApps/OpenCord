package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiChannel

sealed class DomainChannel(
    val channelId: Long,
    val channelName: String,
    val channelPosition: Int,
    val channelParentId: Long?
) : DomainResponse, Comparable<DomainChannel> {

    override fun compareTo(other: DomainChannel): Int {
        val thisPositionInList = sortedChannelTypes.indexOf(this::class)
        val otherPositionInList = sortedChannelTypes.indexOf(other::class)

        return thisPositionInList.compareTo(otherPositionInList)
    }

    data class TextChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel(id, name, position, parentId)

    data class VoiceChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
    ) : DomainChannel(id, name, position, parentId)

    data class AnnouncementChannel(
        val id: Long,
        val name: String,
        val position: Int,
        val parentId: Long?,
        val nsfw: Boolean,
    ) : DomainChannel(id, name, position, parentId)

    data class Category(
        val id: Long,
        val name: String,
        val position: Int,
    ) : DomainChannel(id, name, position, null)

    companion object {

        private val sortedChannelTypes = listOf(
            AnnouncementChannel::class,
            TextChannel::class,
            VoiceChannel::class
        )

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
