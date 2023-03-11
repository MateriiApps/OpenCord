package com.xinto.opencord.util

import android.content.Context
import android.os.Build
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiscordLocale : KoinComponent {
    /**
     * Gets a valid Discord locale based on the current system language,
     * otherwise if unsupported defaults to en-US
     */
    fun getSystemDiscordLocale(): String {
        val context by inject<Context>()

        val locale = if (Build.VERSION.SDK_INT > 24) {
            context.resources.configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

        val langTag = locale.toLanguageTag()

        return if (DISCORD_LOCALES.contains(langTag)) {
            langTag
        } else {
            "en-US"
        }
    }

    fun checkDiscordLocale(locale: String): String {
        return if (locale in DISCORD_LOCALES) {
            locale
        } else {
            "en-US"
        }
    }

    private val DISCORD_LOCALES = arrayOf(
        "id",
        "da",
        "de",
        "en-GB",
        "en-US",
        "es-ES",
        "fr",
        "hr",
        "it",
        "lt",
        "hu",
        "nl",
        "no",
        "pl",
        "pt-BR",
        "ro",
        "fi",
        "sv-SE",
        "vi",
        "tr",
        "cs",
        "el",
        "bg",
        "ru",
        "uk",
        "ja",
        "zh-TW",
        "th",
        "zh-CN",
        "ko",
        "hi",
    )
}
