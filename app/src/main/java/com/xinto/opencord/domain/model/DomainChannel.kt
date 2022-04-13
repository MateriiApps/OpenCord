package com.xinto.opencord.domain.model

sealed interface DomainChannel : Comparable<DomainChannel> {

    val id: ULong
    val name: String
    val position: Int
    val parentId: ULong?

    data class TextChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
        val nsfw: Boolean,
    ) : DomainChannel

    data class VoiceChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
    ) : DomainChannel

    data class AnnouncementChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
        val nsfw: Boolean,
    ) : DomainChannel

    data class Category(
        override val id: ULong,
        override val name: String,
        override val position: Int,
    ) : DomainChannel {
        override val parentId: ULong? = null
    }

    override fun compareTo(other: DomainChannel): Int {
        val thisPositionInList = sortedChannelTypes.indexOf(this::class)
        val otherPositionInList = sortedChannelTypes.indexOf(other::class)

        return thisPositionInList.compareTo(otherPositionInList)
    }

    companion object {
        private val sortedChannelTypes = listOf(
            AnnouncementChannel::class,
            TextChannel::class,
            VoiceChannel::class
        )
    }
}
