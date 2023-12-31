package com.xinto.opencord.ui.screens.home.panels.user

sealed interface HomeUserPanelState {
    object Loading : HomeUserPanelState
    object Loaded : HomeUserPanelState
    object Error : HomeUserPanelState
}