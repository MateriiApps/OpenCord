package com.xinto.opencord.rest.models.user.settings

import com.xinto.opencord.domain.usersettings.DomainFriendSources
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiFriendSources(
    @SerialName("all")
    val all: Boolean? = null,

    @SerialName("mutual_friends")
    val mutualFriends: Boolean? = null,

    @SerialName("mutual_guilds")
    val mutualGuilds: Boolean? = null,
)

fun DomainFriendSources.toApi(): ApiFriendSources {
    return ApiFriendSources(
        all = all,
        mutualFriends = mutualFriends,
        mutualGuilds = mutualGuilds,
    )
}
