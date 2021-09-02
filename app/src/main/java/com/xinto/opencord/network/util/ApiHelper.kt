package com.xinto.opencord.network.util

import com.xinto.opencord.network.result.DiscordAPIResult
import retrofit2.HttpException

inline fun <T> getResultOrError(
    result: () -> T
) = try {
    DiscordAPIResult.Success(result())
} catch (e: HttpException) {
    DiscordAPIResult.Error(e)
}
