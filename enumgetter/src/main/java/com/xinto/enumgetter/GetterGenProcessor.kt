package com.xinto.enumgetter

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

class GetterGenProcessor(val codeGenerator: CodeGenerator) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GETTERGEN_ANNOTATION_IDENTIFIER)
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
            if (classDeclaration.classKind != ClassKind.ENUM_CLASS)
                return

            val constructorParams = classDeclaration.primaryConstructor?.parameters
                ?: return
            val enumFields = classDeclaration.declarations
                .filter { it is KSClassDeclaration && it.classKind == ClassKind.ENUM_ENTRY }
                .map { it.qualifiedName?.asString() ?: "<ERROR>" }

            val packageName = classDeclaration.packageName.asString()
            val classQualifiedName = classDeclaration.qualifiedName?.asString() ?: "<ERROR>"

            val transformedConstructorParams = constructorParams.map { parameter ->
                val name = parameter.name?.asString() ?: "<ERROR>"
                val type = parameter.type.sourceType() ?: "<ERROR>"

                Pair(name, type)
            }

            codeGenerator.createNewFile(
                dependencies = Dependencies(
                    aggregating = true,
                    classDeclaration.containingFile!!
                ),
                packageName = packageName,
                fileName = classQualifiedName
            ).use { file ->
                val functionParams = transformedConstructorParams
                    .joinToString { (name, type) ->
                        "$name: $type"
                    }

                var functionName = "fromValue"
                if (transformedConstructorParams.size > 1)
                    functionName += "s"

                file.writeTextSpaced("package")
                file.writeTextDoubleNewline(packageName)

                file.writeTextSpaced("fun")
                file.writeText(classQualifiedName)
                file.writeText(".Companion.")
                file.writeText(functionName)
                file.writeText("(")
                file.writeText(functionParams)
                file.writeTextSpaced("):")
                file.writeText(classQualifiedName)
                file.writeTextNewline("? {")
                file.withIndent {
                    writeTextNewline("return when {")
                }
                enumFields.forEach { enumField ->
                    file.withIndent(2) {
                        val condition = transformedConstructorParams
                            .joinToString(separator = " && ") { (paramName, _) ->
                                "$enumField.$paramName == $paramName"
                            }

                        writeTextSpaced(condition)
                        writeTextSpaced("->")
                        writeTextNewline(enumField)
                    }
                }
                file.withIndent(2) {
                    writeText("else -> null")
                }
                file.withIndent {
                    writeTextNewline("}")
                }
                file.writeText("}")
            }
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

    }

    companion object {
        const val GETTERGEN_ANNOTATION_IDENTIFIER = "com.xinto.enumgetter.GetterGen"
    }

}