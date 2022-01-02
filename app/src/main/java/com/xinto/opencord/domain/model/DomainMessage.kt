package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiMessage

data class DomainMessage(
    val id: Long,
    val channelId: Long,
    val content: String,
    val author: DomainUser,
    val attachments: List<DomainAttachment>
) : DomainResponse {

    companion object {

        fun fromApi(
            apiMessage: ApiMessage,
        ) = with(apiMessage) {
            DomainMessage(
                id = id,
                content = content,
                channelId = channelId,
                author = DomainUser.fromApi(author),
                attachments = attachments.map { apiAttachment ->
                    DomainAttachment.fromApi(apiAttachment)
                }
            )
        }

    }

}
