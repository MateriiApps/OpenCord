package com.xinto.opencord.di

import com.xinto.opencord.ast.rule.*
import com.xinto.simpleast.Node
import com.xinto.simpleast.Parser
import com.xinto.simpleast.createParser
import org.koin.dsl.module

val simpleAstModule = module {
    fun provideSimpleAst(): Parser<Any?, Node<Any?>, Any?> {
        return createParser {
            rule(createEscapeRule())
            rule(createSpoilerRule())
            rule(createBoldTextRule())
            rule(createUnderlineTextRule())
            rule(createItalicAsteriskTextRule())
            rule(createItalicUnderscoreTextRule())
            rule(createStrikeThroughRule())
            rule(createEmoteRule())
            rule(createUserMentionRule())
            rule(createEveryoneMentionRule())
            rule(createChannelMentionRule())
            rule(createOtherRule())
        }
    }

    single { provideSimpleAst() }
}
