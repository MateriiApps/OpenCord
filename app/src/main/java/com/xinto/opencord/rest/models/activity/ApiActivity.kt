package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.activity.types.DomainActivityCustom
import com.xinto.opencord.domain.activity.types.DomainActivityGame
import com.xinto.opencord.domain.activity.types.DomainActivityListening
import com.xinto.opencord.domain.activity.types.DomainActivityStreaming
import com.xinto.opencord.rest.models.ApiSnowflake
import com.xinto.opencord.rest.models.emoji.ApiEmoji
import com.xinto.opencord.rest.models.emoji.toApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivity(
    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: Int,

    @SerialName("url")
    val url: String? = null,

    @SerialName("created_at")
    val createdAt: Long? = null,

    @SerialName("timestamps")
    val timestamps: ApiActivityTimestamp? = null,

    @SerialName("application_id")
    val applicationId: ApiSnowflake? = null,

    @SerialName("details")
    val details: String? = null,

    @SerialName("state")
    val state: String? = null,

    @SerialName("emoji")
    val emoji: ApiEmoji? = null,

    @SerialName("party")
    val party: ApiActivityParty? = null,

    @SerialName("assets")
    val assets: ApiActivityAssets? = null,

    @SerialName("secrets")
    val secrets: ApiActivitySecrets? = null,

    @SerialName("instance")
    val instance: Boolean? = null,

    @SerialName("flags")
    val flags: Int? = null,

    // TODO: verify whether its List<String name> or List<ApiActivityButton> since I've seen both
//    @SerialName("buttons")
//    val buttons: List<ApiActivityButton>? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("metadata")
    val metadata: ApiActivityMetadata? = null,

    @SerialName("sync_id")
    val syncId: String? = null,
)

fun DomainActivity.toApi(): ApiActivity {
    return when (this) {
        is DomainActivityGame -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            state = state,
            details = details,
            applicationId = ApiSnowflake(applicationId),
            party = party?.toApi(),
            assets = assets?.toApi(),
            secrets = secrets?.toApi(),
            timestamps = timestamps?.toApi(),
        )
        is DomainActivityStreaming -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            url = url,
            state = state,
            details = details,
            assets = assets.toApi(),
        )
        is DomainActivityListening -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            id = id,
            flags = flags,
            state = state,
            details = details,
            syncId = syncId,
            party = party.toApi(),
            assets = assets.toApi(),
            metadata = metadata?.toApi(),
            timestamps = timestamps.toApi(),
        )
        is DomainActivityCustom -> ApiActivity(
            type = type.value,
            name = name,
            createdAt = createdAt,
            state = status,
            emoji = emoji?.toApi(),
        )
        else -> {
            throw IllegalArgumentException("Cannot convert an unknown activity type to an api model!")
        }
    }
}
