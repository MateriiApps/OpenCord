package com.xinto.opencord.rest.models.message

import com.github.materiiapps.partial.Partialize
import com.github.materiiapps.partial.Required
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageMemberJoin
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.rest.models.ApiAttachment
import com.xinto.opencord.rest.models.ApiSnowflake
import com.xinto.opencord.rest.models.embed.ApiEmbed
import com.xinto.opencord.rest.models.embed.toApi
import com.xinto.opencord.rest.models.reaction.ApiReaction
import com.xinto.opencord.rest.models.toApi
import com.xinto.opencord.rest.models.user.ApiUser
import com.xinto.opencord.rest.models.user.toApi
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Partialize
data class ApiMessage(
    @Required
    @SerialName("id")
    val id: ApiSnowflake,

    @Required
    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake? = null,

    @SerialName("timestamp")
    val timestamp: Instant,

    @SerialName("pinned")
    val pinned: Boolean,

    @SerialName("edited_timestamp")
    val editedTimestamp: Instant?,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: ApiUser,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>,

    @SerialName("embeds")
    val embeds: List<ApiEmbed>,

    @SerialName("type")
    val type: ApiMessageType,

    @SerialName("referenced_message")
    val referencedMessage: ApiMessage? = null,

    @SerialName("mention_everyone")
    val mentionEveryone: Boolean,

    // TODO: add mention role support
//    @SerialName("mention_roles")
//    val mentionRoles: List<ApiRole>,

    @SerialName("mentions")
    val mentions: List<ApiUser>,

    @SerialName("reactions")
    val reactions: List<ApiReaction>? = null,

    // Only present on messages sent by self from gateway
    @SerialName("nonce")
    val nonce: String? = null,
)

fun DomainMessage.toApi(): ApiMessage {
    return when (this) {
        is DomainMessageRegular -> ApiMessage(
            id = ApiSnowflake(id),
            channelId = ApiSnowflake(channelId),
            timestamp = timestamp,
            pinned = pinned,
            editedTimestamp = editedTimestamp,
            content = content,
            author = author.toApi(),
            attachments = attachments.map { it.toApi() },
            embeds = embeds.map { it.toApi() },
            type = if (isReply) ApiMessageType.Reply else ApiMessageType.Default,
            referencedMessage = referencedMessage?.toApi(),
            mentionEveryone = mentionEveryone,
//            mentionRoles = mentionRoles.map { it.toApi() },
            mentions = mentions.map { it.toApi() },
        )
        is DomainMessageMemberJoin -> ApiMessage(
            id = ApiSnowflake(id),
            channelId = ApiSnowflake(channelId),
            timestamp = timestamp,
            pinned = pinned,
            editedTimestamp = null,
            content = content,
            author = author.toApi(),
            attachments = emptyList(),
            embeds = emptyList(),
            type = ApiMessageType.GuildMemberJoin,
            referencedMessage = null,
            mentionEveryone = false,
//            mentionRoles = emptyList(),
            mentions = emptyList(),
        )
        else -> ApiMessage(
            id = ApiSnowflake(id),
            channelId = ApiSnowflake(channelId),
            timestamp = timestamp,
            pinned = pinned,
            editedTimestamp = null,
            content = content,
            author = author.toApi(),
            attachments = emptyList(),
            embeds = emptyList(),
            type = ApiMessageType.GuildMemberJoin,
            referencedMessage = null,
            mentionEveryone = false,
//            mentionRoles = emptyList(),
            mentions = emptyList(),
        )
    }
}
