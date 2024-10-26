package com.xinto.opencord.ui.screens.home.panels.channel

sealed interface HomeChannelsPanelState {
    object Unselected : HomeChannelsPanelState
    object Loading : HomeChannelsPanelState
    object Loaded : HomeChannelsPanelState
    object Error : HomeChannelsPanelState
}