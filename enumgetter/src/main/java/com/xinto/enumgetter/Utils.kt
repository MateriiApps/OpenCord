package com.xinto.enumgetter

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.Location
import com.google.devtools.ksp.symbol.NonExistLocation

internal fun Location.toConsoleString(): String {
    return when (this) {
        is FileLocation -> "$filePath:$lineNumber"
        is NonExistLocation -> "<NO LOCATION>"
    }
}
