package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.domain.entity.NetworkDtoMapper
import com.xinto.opencord.domain.model.DomainGuild

data class ApiGuild(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
) : NetworkDtoMapper<DomainGuild> {

    override val dtoModel
        get() = DomainGuild(
            id = id,
            name = name,
            iconName = "$icon.png"
        )

}