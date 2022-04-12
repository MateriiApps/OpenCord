package com.xinto.opencord.ui.viewmodel.base

import androidx.lifecycle.ViewModel
import com.xinto.opencord.domain.manager.PersistentDataManager

abstract class BasePersistenceViewModel(
    private val persistentDataManager: PersistentDataManager
) : ViewModel() {

    protected var persistentGuildId
        get() = persistentDataManager.persistentGuildId
        set(value) {
            persistentDataManager.persistentGuildId = value
        }

    protected var persistentChannelId
        get() = persistentDataManager.persistentChannelId
        set(value) {
            persistentDataManager.persistentChannelId = value
        }

}