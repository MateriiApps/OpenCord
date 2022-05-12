package com.xinto.opencord.rest.dto

import com.xinto.partialgen.Partial
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Partial
data class ApiEmoji(
    @SerialName("id")
    val id: ApiSnowflake?,

    @SerialName("name")
    val name: String?,
)