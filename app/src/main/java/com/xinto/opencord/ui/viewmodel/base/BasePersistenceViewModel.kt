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

    // TODO: once everything's in a DB, this could be transformed into Map<guild, List<collapsedCategories>>
    protected var persistentCollapsedCategories
        get() = persistentDataManager.collapsedCategories
        set(value) {
            persistentDataManager.collapsedCategories = value
        }

    protected fun setCategoryCollapsed(categoryId: ULong, collapsed: Boolean) {
        val categories = persistentDataManager.collapsedCategories.toMutableList()

        if (!collapsed)
            categories.remove(categoryId)
        else if (!categories.contains(categoryId))
            categories.add(categoryId)

        persistentDataManager.collapsedCategories = categories
    }
}