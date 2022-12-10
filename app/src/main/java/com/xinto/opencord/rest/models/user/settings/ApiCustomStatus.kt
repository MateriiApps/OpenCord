package com.xinto.opencord.rest.models.user.settings

import com.xinto.opencord.domain.usersettings.DomainCustomStatus
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCustomStatus(
    @SerialName("text")
    val text: String?,

    @SerialName("expires_at")
    val expiresAt: String?,

    @SerialName("emoji_id")
    val emojiId: ApiSnowflake?,

    @SerialName("emoji_name")
    val emojiName: String?
)

fun DomainCustomStatus.toApi(): ApiCustomStatus {
    return ApiCustomStatus(
        text = text,
        expiresAt = null, // TODO: make timestamp serializer
        emojiId = emojiId?.let { ApiSnowflake(it) },
        emojiName = emojiName,
    )
}
