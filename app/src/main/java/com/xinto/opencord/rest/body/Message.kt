package com.xinto.opencord.rest.body

import com.xinto.opencord.rest.models.ApiSnowflake
import com.xinto.opencord.util.NonceGenerator
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class MessageBody(
    val content: String,
    val flags: Int = 0,
    val tts: Boolean = false,

    @EncodeDefault
    @SerialName("sticker_ids")
    val stickers: List<ApiSnowflake> = emptyList(),

    @EncodeDefault
    val nonce: String = NonceGenerator.nowSnowflake(),
)
