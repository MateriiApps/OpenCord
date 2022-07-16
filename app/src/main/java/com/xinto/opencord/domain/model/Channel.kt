package com.xinto.opencord.domain.model

sealed class DomainChannel : Comparable<DomainChannel>, Mentionable {
    abstract val id: Long
    abstract val guildId: Long?
    abstract val name: String
    abstract val position: Int
    abstract val parentId: Long?
    abstract val permissions: List<DomainPermission>

    val canView
        get() = permissions.contains(DomainPermission.VIEW_CHANNEL)

    val canSendMessages
        get() = permissions.contains(DomainPermission.SEND_MESSAGES)

    override val formattedMention
        get() = "<#$id>"

    data class TextChannel(
        override val id: Long,
        override val guildId: Long?,
        override val name: String,
        override val position: Int,
        override val parentId: Long?,
        override val permissions: List<DomainPermission>,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class VoiceChannel(
        override val id: Long,
        override val guildId: Long?,
        override val name: String,
        override val position: Int,
        override val parentId: Long?,
        override val permissions: List<DomainPermission>,
    ) : DomainChannel()

    data class AnnouncementChannel(
        override val id: Long,
        override val guildId: Long?,
        override val name: String,
        override val position: Int,
        override val parentId: Long?,
        override val permissions: List<DomainPermission>,
        val nsfw: Boolean,
    ) : DomainChannel()

    data class Category(
        override val id: Long,
        override val guildId: Long?,
        override val name: String,
        override val position: Int,
        override val permissions: List<DomainPermission>,
    ) : DomainChannel() {
        override val parentId: Long? = null

        val capsName = name.uppercase()
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
