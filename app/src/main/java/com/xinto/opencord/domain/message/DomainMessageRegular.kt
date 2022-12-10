package com.xinto.opencord.domain.message

import com.github.materiiapps.partial.Partialize
import com.github.materiiapps.partial.map
import com.xinto.opencord.domain.attachment.DomainAttachment
import com.xinto.opencord.domain.attachment.toDomain
import com.xinto.opencord.domain.embed.DomainEmbed
import com.xinto.opencord.domain.embed.toDomain
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.domain.user.toDomain
import com.xinto.opencord.rest.dto.ApiMessagePartial
import com.xinto.opencord.util.SimpleAstParser
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Partialize
data class DomainMessageRegular(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val pinned: Boolean,
    override val content: String,
    override val author: DomainUser,
    val editedTimestamp: Instant?,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>,
    val isReply: Boolean,
    val referencedMessage: DomainMessageRegular?,
    val mentionEveryone: Boolean,
//    val mentionedRoles: List<DomainRole>,
    val mentions: List<DomainUser>,
) : DomainMessage(), KoinComponent {
    private val parser: SimpleAstParser = get()

    val isEdited = editedTimestamp != null
    val contentNodes = parser.parse(content, null)
}

// TODO: turn this into DomainMessagePartial once partial heierarchy is done
fun ApiMessagePartial.toDomain(): DomainMessageRegularPartial {
    return DomainMessageRegularPartial(
        id = id.map { it.value },
        content = content,
        channelId = channelId.map { it.value },
        author = author.map { it.toDomain() },
        timestamp = timestamp,
        editedTimestamp = editedTimestamp,
        attachments = attachments.map { attachments ->
            attachments.map { it.toDomain() }
        },
        embeds = embeds.map { embeds ->
            embeds.map { it.toDomain() }
        },
    )
}
