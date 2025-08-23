package com.xinto.opencord.ui.screens.home.panels.chat

sealed interface HomeChatPanelState {
    object Unselected : HomeChatPanelState
    object Loading : HomeChatPanelState
    object Loaded : HomeChatPanelState
    object Error : HomeChatPanelState
}