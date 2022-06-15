package com.xinto.opencord.domain.manager

import android.content.SharedPreferences
import com.xinto.opencord.domain.manager.base.BasePreferenceManager

interface PersistentDataManager {
    var persistentGuildId: Long
    var persistentChannelId: Long
    var collapsedCategories: List<Long>
    fun addCollapsedCategory(id: Long)
    fun removeCollapsedCategory(id: Long)
}

class PersistentDataManagerImpl(
    persistentPrefs: SharedPreferences
) : BasePreferenceManager(persistentPrefs), PersistentDataManager {
    override var persistentGuildId: Long
        get() = getLong(CURRENT_GUILD_ID_KEY, CURRENT_GUILD_ID_DEFAULT)
        set(value) {
            putLong(CURRENT_GUILD_ID_KEY, value)
        }

    override var persistentChannelId: Long
        get() = getLong(CURRENT_CHANNEL_ID_KEY, CURRENT_CHANNEL_ID_DEFAULT)
        set(value) {
            putLong(CURRENT_CHANNEL_ID_KEY, value)
        }

    override var collapsedCategories: List<Long>
        get() = getStringSet(COLLAPSED_CATEGORIES_ID_KEY, emptySet())!!
            .mapNotNull { it.toLongOrNull() }
        set(value) {
            putStringSet(
                COLLAPSED_CATEGORIES_ID_KEY,
                value.map { it.toString() }.toSet()
            )
        }

    override fun addCollapsedCategory(id: Long) {
        collapsedCategories = collapsedCategories + id
    }

    override fun removeCollapsedCategory(id: Long) {
        collapsedCategories = collapsedCategories - id
    }

    companion object {
        const val CURRENT_GUILD_ID_KEY = "current_guild_id"
        const val CURRENT_GUILD_ID_DEFAULT = 0L

        const val CURRENT_CHANNEL_ID_KEY = "current_channel_id"
        const val CURRENT_CHANNEL_ID_DEFAULT = 0L

        const val COLLAPSED_CATEGORIES_ID_KEY = "collapsed_categories"
    }
}