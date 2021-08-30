package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.domain.entity.NetworkDtoMapper
import com.xinto.opencord.domain.model.DomainLoginResult

data class ApiLoginResult(
    @SerializedName("token") val token: String,
    @SerializedName("mfa") val mfa: Boolean = false,
    @SerializedName("user_settings") val user_settings: ApiLoginUserSettingsResult,
) : NetworkDtoMapper<DomainLoginResult> {

    override val dtoModel
        get() = DomainLoginResult(
            token = token,
            mfa = mfa,
            userSettings = user_settings.dtoModel,
        )

}