package com.xinto.ksputil

import java.io.OutputStream

fun OutputStream.writePackage(packageName: String) {
    appendTextSpaced("package")
    appendTextDoubleNewline(packageName)
}

fun OutputStream.writeImports(imports: List<String>) {
    imports.sorted().distinct().forEach { import ->
        appendTextSpaced("import")
        appendTextNewline(import)
    }
    appendNewline()
}

fun OutputStream.writeWarning(thing: String, target: String) {
    appendTextNewline("/**")
    appendTextNewline(" * Generated $thing for [$target].")
    appendTextNewline(" * DO NOT EDIT MANUALLY!!!")
    appendTextNewline(" */")
}