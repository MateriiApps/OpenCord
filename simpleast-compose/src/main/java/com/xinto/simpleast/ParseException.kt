package com.xinto.simpleast

public class ParseException(
    message: String,
    source: CharSequence?,
    cause: Throwable? = null,
) : RuntimeException("Error while parsing: $message \n Source: $source", cause)