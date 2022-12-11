package com.xinto.opencord.rest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuild(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("name")
    val name: String,

    @SerialName("icon")
    val icon: String?,

    @SerialName("banner")
    val banner: String? = null,

    @SerialName("premium_tier")
    val premiumTier: Int,

    @SerialName("premium_subscription_count")
    val premiumSubscriptionCount: Int? = null,

    @SerialName("channels")
    val channels: List<ApiChannel>,
)
