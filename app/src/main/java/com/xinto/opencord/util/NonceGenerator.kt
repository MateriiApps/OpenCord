package com.xinto.opencord.util

object NonceGenerator {
    private const val DISCORD_EPOCH = 1420070400000

    fun nowSnowflake(): String {
        val timestamp = System.currentTimeMillis() - DISCORD_EPOCH
        val snowflake = timestamp.toULong() shl 22
        return snowflake.toString()
    }
}
