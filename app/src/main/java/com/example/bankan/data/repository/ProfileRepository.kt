package com.example.bankan.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


// TODO remake to sealed class UserState

interface ProfileRepository {
    val isAuthorized: Flow<Boolean>
    val isGuest: Flow<Boolean>
    val userId: Flow<Int>
    val userName: Flow<String>
    val sessionToken: String

    suspend fun setUserName(name: String)
    fun authorize(login: String, password: String)
    suspend fun continueAsGuest(userName: String)
}

object PreferencesKeys {
    val isAuthorized = booleanPreferencesKey("AUTHORIZED")
    val isGuest = booleanPreferencesKey("AUTHORIZED")
    val userId = intPreferencesKey("USER_ID")
    val username = stringPreferencesKey("USERNAME")
}


class ProfileRepositoryInDataStoreNoInternetImpl : ProfileRepository, KoinComponent {

    private val preferences: DataStore<Preferences> by inject()

    override val isAuthorized: Flow<Boolean>
        get() = preferences.data.map {
            it[PreferencesKeys.isAuthorized] ?: false
        }
    override val isGuest: Flow<Boolean>
        get() = preferences.data.map {   it[PreferencesKeys.isGuest] ?: false }

    override val userId: Flow<Int>
        get() = preferences.data.map {
            it[PreferencesKeys.userId] ?: -1
        }

    override val userName: Flow<String>
        get() = preferences.data.map {
            it[PreferencesKeys.username] ?: ""
        }

    override suspend fun setUserName(name: String) {
        preferences.edit {
            it[PreferencesKeys.username] = name
        }
    }

    override val sessionToken: String
        get() = TODO("Not yet implemented")

    override fun authorize(login: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun continueAsGuest(userName: String) {
        preferences.edit {
            it[PreferencesKeys.isGuest] = true
            it[PreferencesKeys.isAuthorized] = true
            it[PreferencesKeys.username] = userName
        }
    }
}