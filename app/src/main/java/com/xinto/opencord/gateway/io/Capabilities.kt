package com.xinto.opencord.gateway.io

@Suppress("unused")
enum class Capabilities(val value: Int) {
    LAZY_USER_NOTES(1 shl 0),
    NO_AFFINE_USER_IDS(1 shl 1),
    VERSIONED_READ_STATES(1 shl 2),
    VERSIONED_USER_GUILD_SETTINGS(1 shl 3),
    DEDUPLICATE_USER_OBJECTS(1 shl 4),
    PRIORITIZED_READY_PAYLOAD(1 shl 5), //
    MULTIPLE_GUILD_EXPERIMENT_POPULATIONS(1 shl 6),
    NON_CHANNEL_READ_STATES(1 shl 7), //
    AUTH_TOKEN_REFRESH(1 shl 8),
    USER_SETTINGS_PROTO(1 shl 9),
    CLIENT_STATE_V2(1 shl 10),
    PASSIVE_GUILD_UPDATE(1 shl 11),
}
