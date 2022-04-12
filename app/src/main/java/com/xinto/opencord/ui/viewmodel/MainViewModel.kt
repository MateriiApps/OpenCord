package com.xinto.opencord.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.gateway.DiscordGateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val gateway: DiscordGateway
) : ViewModel() {

    override fun onCleared() {
        viewModelScope.launch {
            gateway.disconnect()
        }
    }

    init {
        viewModelScope.launch {
            gateway.connect()
        }
    }
}