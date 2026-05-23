package com.example.ufmcontroller.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appScope: CoroutineScope,
) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val authToken: StateFlow<String?> = dataStore.data
        .map { preferences -> preferences[AUTH_TOKEN] }
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun clearAuthToken() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }
}