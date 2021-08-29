package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.entity.NetworkDtoMapper

data class ApiGuild(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val iconHash: String,
) : NetworkDtoMapper<DomainGuild> {

    override val dtoModel =
        DomainGuild(
            id = id,
            name = name,
            iconName = "$iconHash.png"
        )

}