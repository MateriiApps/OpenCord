package com.xinto.opencord.di

import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::GuildsViewModel)
    viewModelOf(::ChannelsViewModel)
    viewModelOf(::ChannelPinsViewModel)
    viewModelOf(::CurrentUserViewModel)
}
