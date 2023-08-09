package com.example.learning3

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.learning3.data.ColorConfig
import com.example.learning3.data.DarkThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreThemePrefs(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("themePrefs")
        val DYNAMIC_THEME_KEY = booleanPreferencesKey("dynamic_theme")
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        val COLOR_KEY = stringPreferencesKey("color")
    }

    val isDynamicThemeEnabled: Flow<Boolean> = context.dataStore.data
        .map {
            it[DYNAMIC_THEME_KEY] ?: true
        }

    suspend fun setDynamicThemeEnabled(value: Boolean) {
        context.dataStore.edit {
            it[DYNAMIC_THEME_KEY] = value
        }
    }

    val getThemeMode: Flow<String> = context.dataStore.data
        .map {
            it[THEME_MODE_KEY] ?: DarkThemeConfig.AUTO.toString()
        }

    suspend fun setThemeMode(value: String) {
        context.dataStore.edit {
            it[THEME_MODE_KEY] = value
        }
    }

    val getColor: Flow<String> = context.dataStore.data
        .map {
            it[COLOR_KEY] ?: ColorConfig.BROWN.toString()
        }

    suspend fun setColor(value: String) {
        context.dataStore.edit {
            it[COLOR_KEY] = value
        }
    }

}