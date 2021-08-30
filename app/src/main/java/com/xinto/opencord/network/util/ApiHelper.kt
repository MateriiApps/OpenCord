package com.xinto.opencord.network.util

import com.xinto.opencord.domain.entity.NetworkDtoMapper
import com.xinto.opencord.network.result.DiscordAPIResult
import retrofit2.HttpException

inline fun <T> getResultOrError(
    result: () -> NetworkDtoMapper<T>
) = try {
    DiscordAPIResult.Success(result().dtoModel)
} catch (e: HttpException) {
    DiscordAPIResult.Error(e)
}
