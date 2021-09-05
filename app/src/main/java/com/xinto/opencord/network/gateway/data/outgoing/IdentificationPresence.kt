package com.xinto.opencord.network.gateway.data.outgoing

data class IdentificationPresence(
    val status: String,
    val since: Int,
    val activities: List<String>,
    val afk: Boolean,
)