package com.xinto.opencord.simpleast.core.exception

class ParseException(
    message: String,
    source: CharSequence?,
    cause: Throwable? = null,
) : RuntimeException("Error while parsing: $message \n Source: $source", cause)