package com.xinto.opencord.ext

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

inline fun <reified T> Gson.safeFromJson(
    json: String
): T? = try {
    fromJson(json, T::class.java)
} catch (e: JsonSyntaxException) {
    null
}