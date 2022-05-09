package com.xinto.enumgetter

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.xinto.ksputil.*
import java.io.OutputStream

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
                .map { Pair(it.simpleName.getShortName(), it.resolveImport()) }
            
            val packageName = classDeclaration.packageName.asString()
            val classShortName = classDeclaration.qualifiedName?.getShortName() ?: "<ERROR>"
            val classQualifiedName = classDeclaration.qualifiedName?.asString() ?: "<ERROR>"

            val imports = mutableListOf(classQualifiedName)
            
            val transformedConstructorParams = constructorParams.map { parameter ->
                val name = parameter.name?.asString() ?: "<ERROR>"
                val type = parameter.type.sourceType().let { (type, import) ->
                    imports.addAll(import.filterNotNull())

                    type
                }

                Pair(name, type)
            }
            val transformedEnumFields = enumFields.map { (type, import) ->
                if (import != null) {
                    imports.add(import)
                }
                
                type
            }.toList()

            codeGenerator.createNewFile(
                dependencies = Dependencies(
                    aggregating = true,
                    classDeclaration.containingFile!!
                ),
                packageName = packageName,
                fileName = classShortName + "EnumGetter"
            ).use { file ->
                file.writePackage(packageName)

                file.writeImports(imports)

                file.writeWarning(
                    thing = "enum getter",
                    target = classShortName
                )

                file.writeGetterFunction(
                    className = classShortName,
                    constructorParams = transformedConstructorParams,
                    enumFields = transformedEnumFields
                )
            }
        }
        
        private fun OutputStream.writeGetterFunction(
            className: String,
            constructorParams: List<Pair<String, String>>,
            enumFields: List<String>,
        ) {
            var functionName = "fromValue"
            if (constructorParams.size > 1)
                functionName += "s"

            val functionParams = constructorParams
                .joinToString { (name, type) ->
                    "$name: $type"
                }

            appendTextSpaced("fun")
            appendText(className)
            appendText(".Companion.")
            appendText(functionName)
            appendText("(")
            appendText(functionParams)
            appendTextSpaced("):")
            appendText(className)
            appendTextNewline("? {")
            withIndent {
                appendTextNewline("return when {")
            }
            enumFields.forEach { enumField ->
                withIndent(2) {
                    val condition = constructorParams
                        .joinToString(separator = " && ") { (paramName, _) ->
                            "$enumField.$paramName == $paramName"
                        }

                    appendTextSpaced(condition)
                    appendTextSpaced("->")
                    appendTextNewline(enumField)
                }
            }
            withIndent(2) {
                appendTextNewline("else -> null")
            }
            withIndent {
                appendTextNewline("}")
            }
            appendText("}")
        }
    }

    companion object {
        const val GETTERGEN_ANNOTATION_IDENTIFIER = "com.xinto.enumgetter.GetterGen"
    }

}