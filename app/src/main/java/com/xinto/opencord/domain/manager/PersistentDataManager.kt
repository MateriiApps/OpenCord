package com.xinto.opencord.domain.manager

import android.content.SharedPreferences
import com.xinto.opencord.domain.manager.base.BasePreferenceManager

interface PersistentDataManager {

    var persistentGuildId: ULong

    var persistentChannelId: ULong

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

    companion object {
        const val CURRENT_GUILD_ID_KEY = "current_guild_id"
        const val CURRENT_GUILD_ID_DEFAULT = 0L

        const val CURRENT_CHANNEL_ID_KEY = "current_channel_id"
        const val CURRENT_CHANNEL_ID_DEFAULT = 0L
    }
}