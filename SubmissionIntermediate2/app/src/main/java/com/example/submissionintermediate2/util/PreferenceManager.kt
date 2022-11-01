package com.example.submissionintermediate2.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.submissionintermediate2.model.UserModel
import com.example.submissionintermediate2.view.PreferenceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager(private val dataStore: DataStore<Preferences>) : PreferenceInterface {
    override fun getSessionUser(): Flow<UserModel> {
        return dataStore.data.map { preference ->
            UserModel(
                userId = preference[ID_KEY] ?: "",
                name = preference[NAME_KEY] ?: "",
                token = preference[TOKEN_KEY] ?: "",
            )
        }
    }

    override suspend fun setSessionUser(user: UserModel?) {
        dataStore.edit { preference ->
            preference[ID_KEY] = user?.userId ?: ""
            preference[NAME_KEY] = user?.name ?: ""
            preference[TOKEN_KEY] = user?.token ?: ""
        }
    }

    override fun getIsDarkMode(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[APPEARANCE_KEY] ?: false
        }
    }
    override suspend fun setIsDarkMode(isDarkMode: Boolean?) {
        dataStore.edit { preference ->
            preference[APPEARANCE_KEY] = isDarkMode ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferenceManager? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")

        private val APPEARANCE_KEY = booleanPreferencesKey("appearance")

        fun getInstance(dataStore: DataStore<Preferences>): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferenceManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}