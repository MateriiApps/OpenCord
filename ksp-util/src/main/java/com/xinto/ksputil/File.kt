package com.xinto.ksputil

import java.io.OutputStream

fun OutputStream.appendText(text: String) {
    write(text.toByteArray())
}

fun OutputStream.appendNewline() {
    appendText("\n")
}

fun OutputStream.appendDoubleNewline() {
    appendText("\n\n")
}

fun OutputStream.appendSpace() {
    appendText(" ")
}

fun OutputStream.appendTextNewline(text: String) {
    appendText(text)
    appendNewline()
}

fun OutputStream.appendTextDoubleNewline(text: String) {
    appendText(text)
    appendDoubleNewline()
}

fun OutputStream.appendTextSpaced(text: String) {
    appendText(text)
    appendSpace()
}

inline fun OutputStream.withIndent(
    multiplier: Int = 1,
    block: OutputStream.() -> Unit
) {
    appendText("    ".repeat(multiplier))
    block(this)
}