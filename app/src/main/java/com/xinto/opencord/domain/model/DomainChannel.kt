package com.xinto.opencord.domain.model

sealed class DomainChannel : Comparable<DomainChannel> {

    abstract val id: ULong
    abstract val name: String
    abstract val position: Int
    abstract val parentId: ULong?
    abstract val permissions: List<DomainPermission>

    val canView
        get() = permissions.contains(DomainPermission.VIEW_CHANNEL)

    val canSendMessages
        get() = permissions.contains(DomainPermission.SEND_MESSAGES)

    data class TextChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
        override val permissions: List<DomainPermission>,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class VoiceChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
        override val permissions: List<DomainPermission>,
    ) : DomainChannel()

    data class AnnouncementChannel(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val parentId: ULong?,
        override val permissions: List<DomainPermission>,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class Category(
        override val id: ULong,
        override val name: String,
        override val position: Int,
        override val permissions: List<DomainPermission>,
    ) : DomainChannel() {
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
