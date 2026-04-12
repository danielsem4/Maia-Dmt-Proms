package maia.dmt.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import maia.dmt.core.data.dto.LoginSuccessfulRequestSerializable
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.mapper.toSerializable
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.dto.LoginSuccessfulRequest

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
): SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")
    private val activeClinicIdKey = stringPreferencesKey("KEY_ACTIVE_CLINIC_ID")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeAuthInfo(): Flow<LoginSuccessfulRequest?> {
        return dataStore.data.map { preferences ->
            val serializedJson = preferences[authInfoKey]
            serializedJson?.let {
                json.decodeFromString<LoginSuccessfulRequestSerializable>(it).toDomain()
            }
        }
    }

    override suspend fun set(info: LoginSuccessfulRequest?, activeClinicId: String?) {
        if(info == null) {
            dataStore.edit {
                it.remove(authInfoKey)
                it.remove(activeClinicIdKey)
            }
            return
        }

        val serialized = json.encodeToString(info.toSerializable())
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
            if (!activeClinicId.isNullOrEmpty()) {
                prefs[activeClinicIdKey] = activeClinicId
            }
        }
    }

    override suspend fun getActiveClinicId(): String? {
        val storedId = dataStore.data.firstOrNull()?.get(activeClinicIdKey)
        if (!storedId.isNullOrEmpty()) return storedId

        // Fallback: read from stored auth info (handles auto-login path)
        return observeAuthInfo().firstOrNull()?.user?.clinics?.firstOrNull()?.id
    }

    override suspend fun setActiveClinicId(clinicId: String?) {
        dataStore.edit { prefs ->
            if (clinicId != null) {
                prefs[activeClinicIdKey] = clinicId
            } else {
                prefs.remove(activeClinicIdKey)
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        return observeAuthInfo().firstOrNull()?.tokens?.access
    }

    override suspend fun getRefreshToken(): String? {
        return observeAuthInfo().firstOrNull()?.tokens?.refresh
    }
}
