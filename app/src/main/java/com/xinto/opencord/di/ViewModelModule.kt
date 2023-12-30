package com.xinto.opencord.di

import androidx.lifecycle.SavedStateHandle
import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::GuildsViewModel)
    viewModelOf(::ChannelsViewModel)
    viewModelOf(::ChannelPinsViewModel)
    viewModelOf(::CurrentUserViewModel)
    viewModelOf(::MessageMenuViewModel)
    viewModelOf(::MentionsViewModel)
    viewModelOf(::ChatInputViewModel)
    viewModelOf(::ImageViewerViewModel)
    viewModel { (handle: SavedStateHandle) -> NavigationViewModel(handle) }
}
