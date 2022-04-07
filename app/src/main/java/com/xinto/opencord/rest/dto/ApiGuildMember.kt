package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuildMember(
    @SerialName("user")
    val user: ApiUser?,

    @SerialName("nick")
    val nick: String?,

    @SerialName("avatar")
    val avatar: String?
)