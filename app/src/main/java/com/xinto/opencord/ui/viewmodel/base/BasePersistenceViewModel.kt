package com.xinto.opencord.ui.viewmodel.base

import androidx.lifecycle.ViewModel
import com.xinto.opencord.domain.manager.PersistentDataManager

abstract class BasePersistenceViewModel(
    private val persistentDataManager: PersistentDataManager
) : ViewModel() {

    protected suspend fun getPersistentGuildId(): ULong {
        return persistentDataManager.getSelectedGuildId()
    }

    protected suspend fun setPersistentGuildId(id: ULong) {
        persistentDataManager.setSelectedGuildId(id)
    }

    protected suspend fun getPersistentChannelId(): ULong {
        return persistentDataManager.getSelectedChannelId(getPersistentGuildId())
    }

    protected suspend fun setPersistentChannelId(id: ULong) {
        persistentDataManager.setSelectedChannelId(getPersistentGuildId(), id)
    }

}