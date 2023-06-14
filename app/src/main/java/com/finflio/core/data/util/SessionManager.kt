package com.finflio.core.data.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.finflio.core.data.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val USER_DATA = stringPreferencesKey("user-data")
    }

    suspend fun saveUserData(data: UserSettings) {
        dataStore.edit { pref ->
            pref[USER_DATA] = Json.encodeToString(
                UserSettings.serializer(),
                data
            )
        }
    }

    fun getUserData(): Flow<UserSettings> {
        val userData: Flow<String> = dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[USER_DATA] ?: ""
        }
        return channelFlow {
            userData.collectLatest {
                send(
                    try {
                        Json.decodeFromString(
                            UserSettings.serializer(),
                            it
                        )
                    } catch (e: SerializationException) {
                        println(e.message)
                        e.printStackTrace()
                        UserSettings()
                    }
                )
            }
        }
    }

    suspend fun clearDatastore() {
        dataStore.edit {
            it.clear()
        }
    }
}