package com.xinto.opencord.rest.models

import kotlinx.serialization.Serializable

// Only used for fetching the fingerprint in auth
@Serializable
data class ApiExperiments(
    val fingerprint: String,
)
