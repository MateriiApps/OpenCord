package com.xinto.opencord.domain.model

data class DomainEmbed(
    val title: String?,
    val description: String?,
    val url: String?,
    val color: Int?,
) {

    data class Thumbnail(
        val url: String,
        val proxyUrl: String,
        val height: Int?,
        val width: Int?
    ) {

    }

    data class Footer(
        val text: String
    ) {

    }

    data class Field(
        val name: String,
        val value: String,
    ) {

    }

    companion object {

    }

}
