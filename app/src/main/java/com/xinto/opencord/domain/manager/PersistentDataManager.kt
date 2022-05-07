package com.xinto.opencord.domain.manager

import android.content.SharedPreferences
import com.xinto.opencord.domain.manager.base.BasePreferenceManager

interface PersistentDataManager {
    var persistentGuildId: ULong
    var persistentChannelId: ULong
    var collapsedCategories: List<ULong>
    fun addCollapsedCategory(id: ULong)
    fun removeCollapsedCategory(id: ULong)
}

class PersistentDataManagerImpl(
    persistentPrefs: SharedPreferences
) : BasePreferenceManager(persistentPrefs), PersistentDataManager {
    override var persistentGuildId: ULong
        get() = getLong(CURRENT_GUILD_ID_KEY, CURRENT_GUILD_ID_DEFAULT).toULong()
        set(value) {
            putLong(CURRENT_GUILD_ID_KEY, value.toLong())
        }

    override var persistentChannelId: ULong
        get() = getLong(CURRENT_CHANNEL_ID_KEY, CURRENT_CHANNEL_ID_DEFAULT).toULong()
        set(value) {
            putLong(CURRENT_CHANNEL_ID_KEY, value.toLong())
        }

    override var collapsedCategories: List<ULong>
        get() = getStringSet(COLLAPSED_CATEGORIES_ID_KEY, emptySet())!!
            .mapNotNull { it.toULongOrNull() }
        set(value) {
            putStringSet(
                COLLAPSED_CATEGORIES_ID_KEY,
                value.map { it.toString() }.toSet()
            )
        }

    override fun addCollapsedCategory(id: ULong) {
        collapsedCategories = collapsedCategories + id
    }

    override fun removeCollapsedCategory(id: ULong) {
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