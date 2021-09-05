package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse

data class DomainLoginResult(
    val token: String,
    val mfa: Boolean,
    val userSettings: DomainLoginUserSettingsResult,
) : DomainResponse