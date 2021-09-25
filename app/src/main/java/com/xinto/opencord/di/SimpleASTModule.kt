package com.xinto.opencord.di

import com.xinto.opencord.ui.simpleast.impl.rule.*
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
            //rule(createEmoteRule()) //TODO Fix emote issues
            rule(createUserMentionRule())
            rule(createChannelMentionRule())
            rule(createOtherRule())
        }
        return parser
    }

    single { provideSimpleAst() }

}