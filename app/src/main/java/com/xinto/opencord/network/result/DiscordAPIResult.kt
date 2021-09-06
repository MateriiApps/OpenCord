package com.xinto.opencord.network.result

import retrofit2.HttpException

sealed class DiscordAPIResult<out T> {

    object Loading : DiscordAPIResult<Nothing>()
    data class Success<out V>(val data: V) : DiscordAPIResult<V>()
    data class Error(val e: HttpException) : DiscordAPIResult<Nothing>()

}
