package com.xinto.opencord.ui.screens.home.panels.guild

sealed interface HomeGuildPanelState {
    object Loading : HomeGuildPanelState
    object Loaded : HomeGuildPanelState
    object Error : HomeGuildPanelState
}