package com.xinto.partialgen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.xinto.ksputil.*
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

            val imports = mutableListOf(
                classQualifiedName,
                "com.xinto.partialgen.PartialValue",
                "com.xinto.partialgen.getOrElse",
            )
            val classAnnotations = classDeclaration.annotations
                .mapNotNull { annotation ->
                    annotation.sourceAnnotation().let { (type, import) ->
                        if (import != null) {
                            imports.add(import)
                        }

                        if (import == PARTIAL_ANNOTATION_IDENTIFIER)
                            return@let null

                        type
                    }
                }.toList()

            val dependencies = Dependencies(true, classDeclaration.containingFile!!)

            val transformedConstructorParams = constructorParams.map { parameter ->
                val name = parameter.name?.asString() ?: "<ERROR>"
                val type = parameter.type.sourceType().let { (type, import) ->
                    imports.addAll(import.filterNotNull())

                    type
                }

                val annotations = parameter.annotations
                    .mapNotNull { annotation ->
                        annotation.sourceAnnotation().let { (type, import) ->
                            if (import != null) {
                                imports.add(import)
                            }

                            type
                        }
                    }.toList()

                Triple(name, type, annotations)
            }

            codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = partialClassShortName
            ).use { file ->
                file.writePackage(packageName)

                file.writeImports(imports)

                file.writeWarning(
                    thing = "Partial class",
                    target = classShortName,
                )

                file.writeDataClass(
                    partialClassName = partialClassShortName,
                    classAnnotations = classAnnotations,
                    constructorParams = transformedConstructorParams
                )

                file.writeMergeFunction(
                    classShortName = classShortName,
                    partialClassShortName = partialClassShortName,
                    constructorParams = transformedConstructorParams
                )
            }
        }

        private fun OutputStream.writeDataClass(
            partialClassName: String,
            classAnnotations: List<String>,
            constructorParams: List<Triple<String, String, List<String>>>
        ) {
            classAnnotations.forEach { annotation ->
                appendTextNewline(annotation)
            }

            appendTextSpaced("data class")
            appendText(partialClassName)
            appendTextNewline("(")
            constructorParams.forEach { (name, type, annotations) ->
                annotations.forEach { annotation ->
                    withIndent {
                        appendTextNewline(annotation)
                    }
                }
                withIndent {
                    appendTextSpaced("val")
                    appendText(name)
                    appendText(": PartialValue<")
                    appendText(type)
                    appendText("> = PartialValue.Missing<")
                    appendText(type)
                    appendTextNewline(">(),")
                }
            }
            appendTextDoubleNewline(")")
        }

        private fun OutputStream.writeMergeFunction(
            classShortName: String,
            partialClassShortName: String,
            constructorParams: List<Triple<String, String, List<String>>>
        ) {
            appendTextSpaced("fun")
            appendText(classShortName)
            appendText(".")
            appendTextSpaced("merge(partial:")
            appendText(partialClassShortName)
            appendTextSpaced("):")
            appendTextSpaced(classShortName)
            appendTextNewline("{")
            withIndent {
                appendTextSpaced("return")
                appendText(classShortName)
                appendTextNewline("(")
                constructorParams.forEach { (name, _, _) ->
                    withIndent(2) {
                        appendTextSpaced(name)
                        appendTextSpaced("=")
                        appendText("partial.")
                        appendText(name)
                        appendTextSpaced(".getOrElse {")
                        appendTextSpaced(name)
                        appendText("}")
                        appendTextNewline(",")
                    }
                }
            }
            withIndent {
                appendTextNewline(")")
            }
            appendTextNewline("}")
        }
    }

    companion object {
        const val PARTIAL_ANNOTATION_IDENTIFIER = "com.xinto.partialgen.Partial"
    }
}
