package com.xinto.partialgen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
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
            val classShortName = classDeclaration.qualifiedName?.getShortName() ?: "<ERROR>"
            val classQualifiedName = classDeclaration.qualifiedName?.asString() ?: "<ERROR>"

            val partialClassShortName = classDeclaration.simpleName.asString() + "Partial"

            val imports = mutableListOf(classQualifiedName)
            val classAnnotations = classDeclaration.annotations
                .mapNotNull { it.sourceAnnotation(imports) }.toList()

            val dependencies = Dependencies(true, classDeclaration.containingFile!!)

            val transformedConstructorParams = constructorParams.map { parameter ->
                val name = parameter.name?.asString() ?: "<ERROR>"
                val type = parameter.type.sourceType(imports) ?: "<ERROR>"

                val annotations = parameter.annotations
                    .mapNotNull { annotation -> annotation.sourceAnnotation(imports) }
                    .toList()

                Triple(name, type, annotations)
            }

            codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = partialClassShortName
            ).use { file ->
                file.writeTextSpaced("package")
                file.writeTextDoubleNewline(packageName)

                imports.sort()
                imports.forEach {
                    file.writeTextSpaced("import")
                    file.writeTextNewline(it)
                }
                file.writeText("\n")

                file.writeTextNewline("/**")
                file.writeTextNewline(" * Generated Partial class for [${classShortName}]")
                file.writeTextNewline(" * DO NOT EDIT MANUALLY")
                file.writeTextNewline(" */")

                file.appendDataClass(
                    partialClassName = partialClassShortName,
                    classAnnotations = classAnnotations,
                    constructorParams = transformedConstructorParams
                )

                file.appendMergeFunction(
                    classShortName = classShortName,
                    partialClassShortName = partialClassShortName,
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
                    writeTextDoubleNewline("? = null,")
                }
            }
            writeTextDoubleNewline(")")
        }

        private fun OutputStream.appendMergeFunction(
            classShortName: String,
            partialClassShortName: String,
            constructorParams: List<Triple<String, String, List<String>>>
        ) {
            writeTextSpaced("fun")
            writeText(classShortName)
            writeText(".")
            writeTextSpaced("merge(partial:")
            writeText(partialClassShortName)
            writeTextSpaced("):")
            writeTextSpaced(classShortName)
            writeTextNewline("{")
            withIndent {
                writeTextSpaced("return")
                writeText(classShortName)
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
            writeTextNewline("}")
        }

        private fun KSTypeReference.sourceType(imports: MutableList<String>): String? {
            val resolvedType = resolve()
            val typeName = resolvedType.declaration.qualifiedName
            val typeArguments = element?.typeArguments ?: emptyList()
            return typeName?.let {
                buildString {
                    append(resolvedType.declaration.addImport(imports))

                    if (typeArguments.isNotEmpty()) {
                        append(typeArguments.joinToString(prefix = "<", postfix = ">") {
                            it.type?.resolve()?.declaration?.addImport(imports) ?: "<ERROR>"
                        })
                    }
                }
            }
        }

        /**
         * Adds a new non-conflicting import in-place and returns the new reference string
         */
        private fun KSDeclaration.addImport(imports: MutableList<String>): String {
            val qualifiedName = qualifiedName?.asString()
            val shortName = simpleName.getShortName()

            if (qualifiedName == "kotlin" || imports.contains(qualifiedName))
                return shortName

            if (
                qualifiedName == null ||
                qualifiedName == "java.lang" ||
                imports.any { it.endsWith(".$shortName") }
            ) {
                return qualifiedName ?: "<ERROR>"
            }

            imports.add(qualifiedName)
            return shortName
        }

        private fun KSAnnotation.sourceAnnotation(imports: MutableList<String>): String? {
            val resolvedType = annotationType.resolve()

            var annotationName = resolvedType.declaration.qualifiedName?.asString()

            // Skip our annotation since it results in recursive codegen
            if (annotationName == PARTIAL_ANNOTATION_IDENTIFIER)
                return null
            else {
                annotationName = resolvedType.declaration.addImport(imports)
            }

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
