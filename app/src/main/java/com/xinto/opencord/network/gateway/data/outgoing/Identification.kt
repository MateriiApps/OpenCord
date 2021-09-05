package com.xinto.opencord.network.gateway.data.outgoing

data class Identification(
    val token: String,
    val capabilities: Int,
    val largeThreshold: Int,
    val compress: Boolean,
    val properties: IdentificationProperties,
//    val presence: IdentificationPresence,
    val clientState: IdentificationClientState,
)
