package com.xinto.opencord.domain.channel

import androidx.compose.runtime.Immutable
import com.xinto.opencord.db.entity.channel.EntityChannel
import com.xinto.opencord.domain.Mentionable
import com.xinto.opencord.rest.models.ApiChannel

@Immutable
abstract class DomainChannel : Comparable<DomainChannel>, Mentionable {
    abstract val id: Long
    abstract val guildId: Long?
    abstract val name: String
    abstract val position: Int
    abstract val parentId: Long?

    override val formattedMention
        get() = "<#$id>"

    /**
     * The sorting priority of this channel type (lower is better).
     * Should always return the same for each type.
     * https://github.com/discord/discord-api-docs/issues/4613#issuecomment-1059997612
     */
    abstract val sortingPriority: Short

    override fun compareTo(other: DomainChannel): Int {
        val typeCmp = this.sortingPriority compareTo other.sortingPriority

        return if (typeCmp != 0) typeCmp else {
            val posCmp = this.position compareTo other.position

            if (posCmp != 0) posCmp else {
                this.id compareTo other.id
            }
        }
    }
}

fun ApiChannel.toDomain(): DomainChannel {
    return when (type) {
        2 -> DomainVoiceChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
        )
        4 -> DomainCategoryChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
        )
        5 -> DomainAnnouncementChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            nsfw = nsfw,
        )
        else -> DomainTextChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            nsfw = nsfw,
        )
    }
}

fun EntityChannel.toDomain(): DomainChannel {
    return when (type) {
        2 -> DomainVoiceChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
        )
        4 -> DomainCategoryChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
        )
        5 -> DomainAnnouncementChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
            nsfw = nsfw,
        )
        else -> DomainTextChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
            nsfw = nsfw,
        )
    }
}
