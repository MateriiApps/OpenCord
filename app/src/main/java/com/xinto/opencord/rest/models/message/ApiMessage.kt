package com.xinto.opencord.rest.models.message

import com.github.materiiapps.partial.Partialize
import com.xinto.opencord.rest.models.ApiAttachment
import com.xinto.opencord.rest.models.ApiSnowflake
import com.xinto.opencord.rest.models.embed.ApiEmbed
import com.xinto.opencord.rest.models.user.ApiUser
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Partialize
data class ApiMessage(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

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
)
