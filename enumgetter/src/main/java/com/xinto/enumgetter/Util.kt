package com.xinto.enumgetter

import java.io.OutputStream

fun OutputStream.writeText(text: String) {
    write(text.toByteArray())
}

fun OutputStream.writeTextNewline(text: String) {
    writeText("$text\n")
}

fun OutputStream.writeTextDoubleNewline(text: String) {
    writeText("$text\n\n")
}

fun OutputStream.writeTextSpaced(text: String) {
    writeText("$text ")
}

inline fun OutputStream.withIndent(
    multiplier: Int = 1,
    block: OutputStream.() -> Unit
) {
    writeText("    ".repeat(multiplier))
    block(this)
}