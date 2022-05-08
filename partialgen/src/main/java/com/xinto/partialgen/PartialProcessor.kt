package com.xinto.partialgen

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import java.io.OutputStream

class PartialProcessor(val codeGenerator: CodeGenerator) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(PARTIAL_ANNOTATION_IDENTIFIER)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                it.accept(PartialVisitor(), Unit)
            }
        return ret
    }

    inner class PartialVisitor : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.classKind != ClassKind.CLASS)
                return

            val constructorParams = classDeclaration.primaryConstructor?.parameters
                ?: return

            val packageName = classDeclaration.packageName.asString()

            val partialClassName = classDeclaration.simpleName.asString() + "Partial"

            val fullClassQualifiedName = classDeclaration.qualifiedName?.asString() ?: "<ERROR>"
            val fullClassAnnotations = classDeclaration.annotations.mapNotNull {
                it.sourceAnnotation()
            }.toList()

            val dependencies = Dependencies(true, classDeclaration.containingFile!!)

            val transformedConstructorParams = constructorParams.map { parameter ->
                val name = parameter.name?.asString() ?: "<ERROR>"
                val type = parameter.type.sourceType() ?: "<ERROR>"
                val annotations = parameter.annotations.mapNotNull { annotation ->
                    annotation.sourceAnnotation()
                }.toList()

                Triple(name, type, annotations)
            }

            codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = partialClassName
            ).use { file ->
                file.writeTextSpaced("package")
                file.writeTextDoubleNewline(packageName)

                file.appendDataClass(
                    partialClassName = partialClassName,
                    classAnnotations = fullClassAnnotations,
                    constructorParams = transformedConstructorParams
                )

                file.appendMergeFunction(
                    fullClassName = fullClassQualifiedName,
                    partialClassName = partialClassName,
                    constructorParams = transformedConstructorParams
                )
            }
        }

        private fun OutputStream.appendDataClass(
            partialClassName: String,
            classAnnotations: List<String>,
            constructorParams: List<Triple<String, String, List<String>>>
        ) {
            classAnnotations.forEach { annotation ->
                writeTextNewline(annotation)
            }

            writeTextSpaced("data class")
            writeText(partialClassName)
            writeTextNewline("(")
            constructorParams.forEach { (name, type, annotations) ->
                annotations.forEach { annotation ->
                    withIndent {
                        writeTextNewline(annotation)
                    }
                }
                withIndent {
                    writeTextSpaced("val")
                    writeText(name)
                    writeTextSpaced(":")
                    writeText(type)
                    writeTextNewline("? = null,")
                }
            }
            writeTextDoubleNewline(")")
        }

        private fun OutputStream.appendMergeFunction(
            fullClassName: String,
            partialClassName: String,
            constructorParams: List<Triple<String, String, List<String>>>
        ) {
            writeTextSpaced("fun")
            writeText(fullClassName)
            writeText(".")
            writeTextSpaced("merge(partial:")
            writeText(partialClassName)
            writeTextSpaced("):")
            writeTextSpaced(fullClassName)
            writeTextNewline("{")
            withIndent {
                writeTextSpaced("return")
                writeText(fullClassName)
                writeTextNewline("(")
                constructorParams.forEach { (name, _, _) ->
                    withIndent(2) {
                        writeTextSpaced(name)
                        writeTextSpaced("=")
                        writeText("partial.")
                        writeTextSpaced(name)
                        writeTextSpaced("?:")
                        writeText(name)
                        writeTextNewline(",")
                    }
                }
            }
            withIndent {
                writeTextNewline(")")
            }
            writeTextDoubleNewline("}")
        }

        private fun KSTypeReference.sourceType(): String? {
            val resolvedType = resolve()
            val typeName = resolvedType.declaration.qualifiedName
            val typeArguments = element?.typeArguments ?: emptyList()
            return typeName?.let {
                buildString {
                    append(it.asString())
                    if (typeArguments.isNotEmpty()) {
                        append(typeArguments.joinToString(prefix = "<", postfix = ">") {
                            val typeArg = it.type?.resolve()?.declaration
                            typeArg?.qualifiedName?.asString() ?: "<ERROR>"
                        })
                    }
                }
            }
        }

        private fun KSAnnotation.sourceAnnotation(): String? {
            val resolvedType = annotationType.resolve()

            val annotationName = resolvedType.declaration.qualifiedName?.asString()
                ?: "<ERROR>"

            // Skip our annotation since it results in recursive codegen
            if (annotationName == PARTIAL_ANNOTATION_IDENTIFIER)
                return null

            return buildString {
                append('@')
                append(annotationName)

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
        }
    }

    companion object {
        const val PARTIAL_ANNOTATION_IDENTIFIER = "com.xinto.partialgen.Partial"
    }

}