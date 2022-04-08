package com.xinto.opencord.di

import com.xinto.opencord.ast.rule.*
import com.xinto.opencord.util.SimpleAstParser
import org.koin.dsl.module

val simpleAstModule = module {

    fun provideSimpleAst(): SimpleAstParser {
        val parser = SimpleAstParser()
        parser.rules {
            rule(createEscapeRule())
            rule(createSpoilerRule())
            rule(createBoldTextRule())
            rule(createUnderlineTextRule())
            rule(createItalicTextRule())
            rule(createStrikeThroughRule())
            rule(createEmoteRule())
            rule(createUserMentionRule())
            rule(createChannelMentionRule())
            rule(createOtherRule())
        }
        return parser
    }

    single { provideSimpleAst() }

}