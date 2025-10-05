package maia.dmt.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import maia.dmt.core.data.mapper.toSerializable
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.LoginSuccessfulRequest

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
): SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeAuthInfo(): Flow<LoginSuccessfulRequest?> {
        return dataStore.data.map { preferences ->
            val serializedJson = preferences[authInfoKey]
            serializedJson?.let {
                json.decodeFromString(it)
            }
        }
    }

    override suspend fun set(info: LoginSuccessfulRequest?) {
        if(info == null) {
            dataStore.edit {
                it.remove(authInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(info.toSerializable())
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
        }
    }
}