package com.xinto.opencord.domain.model

data class DomainLoginResult(
    val token: String,
    val mfa: Boolean,
    val userSettings: DomainLoginUserSettingsResult,
)