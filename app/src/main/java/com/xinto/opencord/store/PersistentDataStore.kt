package com.xinto.opencord.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PersistentDataStore {

    fun observeCurrentGuild(): Flow<Long>
    fun observeCurrentChannel(): Flow<Long>
    fun observeCollapsedCategories(): Flow<List<Long>>

    suspend fun updateCurrentGuild(guildId: Long)
    suspend fun updateCurrentChannel(channelId: Long)
    suspend fun toggleCategory(categoryId: Long)
    suspend fun collapseCategory(categoryId: Long)
    suspend fun expandCategory(categoryId: Long)

}

class PersistentDataStoreImpl(
    private val context: Context
) : PersistentDataStore {

    private val Context.dataStore by preferencesDataStore("persistent")

    override fun observeCurrentGuild(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[CURRENT_GUILD_KEY] ?: 0L
        }
    }

    override fun observeCurrentChannel(): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[CURRENT_CHANNEL_KEY] ?: 0L
        }
    }

    override fun observeCollapsedCategories(): Flow<List<Long>> {
        return context.dataStore.data.map { preferences ->
            (preferences[COLLAPSED_CATEGORIES_KEY] ?: emptySet()).mapNotNull {
                it.toLongOrNull()
            }
        }
    }

    override suspend fun updateCurrentGuild(guildId: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_GUILD_KEY] = guildId
        }
    }

    override suspend fun updateCurrentChannel(channelId: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_CHANNEL_KEY] = channelId
        }
    }

    override suspend fun toggleCategory(categoryId: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[COLLAPSED_CATEGORIES_KEY] ?: emptySet()
            val stringCategoryId = categoryId.toString()
            if (current.contains(stringCategoryId))  {
                preferences[COLLAPSED_CATEGORIES_KEY] = current - stringCategoryId
            } else {
                preferences[COLLAPSED_CATEGORIES_KEY] = current + stringCategoryId
            }
        }
    }

    override suspend fun expandCategory(categoryId: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[COLLAPSED_CATEGORIES_KEY] ?: emptySet()
            preferences[COLLAPSED_CATEGORIES_KEY] = current - categoryId.toString()
        }
    }

    override suspend fun collapseCategory(categoryId: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[COLLAPSED_CATEGORIES_KEY] ?: emptySet()
            preferences[COLLAPSED_CATEGORIES_KEY] = current + categoryId.toString()
        }
    }

    private companion object {
        val CURRENT_GUILD_KEY = longPreferencesKey("CURRENT_GULD")
        val CURRENT_CHANNEL_KEY = longPreferencesKey("CURRENT_CHANNEL")
        val COLLAPSED_CATEGORIES_KEY = stringSetPreferencesKey("COLLAPSED_CATEGORIES")
    }

}