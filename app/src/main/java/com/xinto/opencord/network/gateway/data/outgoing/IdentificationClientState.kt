package com.xinto.opencord.network.gateway.data.outgoing

data class IdentificationClientState(
    val guildHashes: Map<String, String>,
    val highestLastMessageId: Int,
    val readStateVersion: Int,
    val useruserGuildSettingsVersion: Int,
)