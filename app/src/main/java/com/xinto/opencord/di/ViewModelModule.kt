package com.xinto.opencord.di

import com.xinto.opencord.ui.screens.home.panels.channel.HomeChannelsPanelViewModel
import com.xinto.opencord.ui.screens.home.panels.chat.HomeChatPanelViewModel
import com.xinto.opencord.ui.screens.home.panels.guild.GuildsViewModel
import com.xinto.opencord.ui.screens.home.panels.messagemenu.MessageMenuViewModel
import com.xinto.opencord.ui.screens.home.panels.user.HomeUserPanelViewModel
import com.xinto.opencord.ui.screens.login.LoginViewModel
import com.xinto.opencord.ui.screens.mentions.MentionsViewModel
import com.xinto.opencord.ui.screens.pins.PinsScreenViewModel
import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeChatPanelViewModel)
    viewModelOf(::GuildsViewModel)
    viewModelOf(::HomeChannelsPanelViewModel)
    viewModelOf(::PinsScreenViewModel)
    viewModelOf(::HomeUserPanelViewModel)
    viewModelOf(::MessageMenuViewModel)
    viewModelOf(::MentionsViewModel)
}
