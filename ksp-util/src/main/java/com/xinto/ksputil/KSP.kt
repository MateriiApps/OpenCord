package com.xinto.ksputil

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference

typealias Type = String
typealias Import = String

/**
 * @return A [Pair] consisting of the [Type] and its [Import]s.
 */
fun KSTypeReference.sourceType(): Pair<Type, List<Import?>> {
    val declaration = resolve().declaration
    val typeName = declaration.qualifiedName
    val typeArguments = element?.typeArguments ?: emptyList()

    val imports = mutableListOf<Import?>()

    val type = typeName?.let {
        buildString {

            imports.add(declaration.resolveImport())
            append(declaration.simpleName.getShortName())

            if (typeArguments.isNotEmpty()) {
                append(typeArguments.joinToString(prefix = "<", postfix = ">") {
                    it.type?.resolve()?.declaration?.let { declaration ->
                        imports.add(declaration.resolveImport())

                        declaration.simpleName.getShortName()
                    } ?: "<ERROR>"
                })
            }
        }
    } ?: "<ERROR>"

    imports.add(declaration.resolveImport())

    return Pair(type, imports)
}

/**
 * @return A [Pair] consisting of the [Type] and its [Import].
 */
fun KSAnnotation.sourceAnnotation(): Pair<Type, Import?> {
    val declaration = annotationType.resolve().declaration

    val type = buildString {
        append('@')
        append(declaration.simpleName.getShortName())

        append(arguments.joinToString(prefix = "(", postfix = ")") {
            when (val value = it.value) {
                is String -> "\"${value}\""
                is KSType -> {
                    /* FIXME
                        There's a compiler bug in Kotlin IR which produces a
                        "Collection contains no element matching the predicate."
                        error if a KClass parameter is passed to an annotation.
                     */
                    ""
//                            val t = value.declaration.qualifiedName?.asString() + "::class"
//                            if (t == "kotlinx.serialization.KSerializer::class") "" else t
                }
                else -> value.toString()
            }
        })
    }
    val import = declaration.resolveImport()

    return Pair(type, import)
}

fun KSDeclaration.resolveImport(): String? {
    val qualifiedName = qualifiedName?.asString()
    val shortName = simpleName.getShortName()

    if (qualifiedName == "kotlin.$shortName")
        return null

    return qualifiedName ?: "<ERROR>"
}