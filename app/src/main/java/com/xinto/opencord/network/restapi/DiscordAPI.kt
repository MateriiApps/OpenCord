package com.xinto.opencord.network.restapi

import com.xinto.opencord.network.body.MessageBody
import com.xinto.opencord.network.response.ApiChannel
import com.xinto.opencord.network.response.ApiGuild
import com.xinto.opencord.network.response.ApiMeGuild
import com.xinto.opencord.network.response.ApiMessage
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiscordAPI {

    @GET("users/@me/guilds")
    suspend fun getMeGuilds(): List<ApiMeGuild>

    @GET("guilds/{guildId}")
    suspend fun getGuild(@Path("guildId") guildId: Long): ApiGuild

    @GET("guilds/{guildId}/channels")
    suspend fun getGuildChannels(@Path("guildId") guildId: Long): List<ApiChannel>

    @GET("channels/{channelId}/messages")
    suspend fun getChannelMessages(@Path("channelId") channelId: Long): List<ApiMessage>

    @POST("channels/{channelId}/messages")
    suspend fun postChannelMessage(
        @Path("channelId") channelId: Long,
        @Body messageBody: MessageBody,
    )
}