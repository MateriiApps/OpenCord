package com.xinto.opencord.di

import com.xinto.opencord.store.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val storeModule = module {
    singleOf(::MessageStoreImpl) bind MessageStore::class
    singleOf(::LastMessageStoreImpl) bind LastMessageStore::class
    singleOf(::ChannelStoreImpl) bind ChannelStore::class
    singleOf(::GuildStoreImpl) bind GuildStore::class
    singleOf(::UserSettingsStoreImpl) bind UserSettingsStore::class
    singleOf(::CurrentUserStoreImpl) bind CurrentUserStore::class
    singleOf(::SessionStoreImpl) bind SessionStore::class
    singleOf(::UnreadStoreImpl) bind UnreadStore::class
    singleOf(::ReactionStoreImpl) bind ReactionStore::class
}
