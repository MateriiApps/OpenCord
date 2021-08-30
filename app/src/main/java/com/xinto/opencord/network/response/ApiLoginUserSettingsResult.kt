package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.domain.entity.NetworkDtoMapper
import com.xinto.opencord.domain.model.DomainLoginUserSettingsResult

data class ApiLoginUserSettingsResult(
    @SerializedName("locale") val locale: String,
    @SerializedName("theme") val theme: String,
) : NetworkDtoMapper<DomainLoginUserSettingsResult> {

    override val dtoModel
        get() = DomainLoginUserSettingsResult(
            locale = locale,
            theme = theme
        )

}
