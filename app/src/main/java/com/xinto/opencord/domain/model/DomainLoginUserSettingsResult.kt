package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse

data class DomainLoginUserSettingsResult(
    val locale: String,
    val theme: String,
) : DomainResponse
