package com.example.bankan.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.bankan.data.network.MyHttpClient
import com.example.bankan.data.network.payload.request.LoginRequest
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
    val sessionToken: Flow<String?>
    val currentBoardId: Flow<Int?>

    suspend fun setUserName(name: String)
    suspend fun authorize(login: String, password: String): Boolean
    suspend fun continueAsGuest(userName: String)
    suspend fun setNewCurrentBoardId(localId: Int)
    suspend fun logout()
}

object PreferencesKeys {
    val sessionToken = stringPreferencesKey("SESSION_TOKEN")
    val isAuthorized = booleanPreferencesKey("AUTHORIZED")
    val isGuest = booleanPreferencesKey("IS_GUEST")
    val userId = intPreferencesKey("USER_ID")
    val username = stringPreferencesKey("USERNAME")
    val boardId = intPreferencesKey("CURRENT_BOARD_ID")
}


class ProfileRepositoryInDataStoreNoInternetImpl : ProfileRepository, KoinComponent {

    private val preferences: DataStore<Preferences> by inject()

    override val isAuthorized: Flow<Boolean>
        get() = preferences.data.map {
            it[PreferencesKeys.isAuthorized] ?: true
        }
    override val isGuest: Flow<Boolean>
        get() = preferences.data.map { it[PreferencesKeys.isGuest] ?: false }

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

    override val sessionToken: Flow<String?>
        get() = preferences.data.map {
            it[PreferencesKeys.sessionToken]
        }

    override val currentBoardId: Flow<Int?>
        get() = preferences.data.map {
            it[PreferencesKeys.boardId]
        }

    override suspend fun authorize(login: String, password: String):Boolean {
        return MyHttpClient.AuthenticationApi.authenticateUser(LoginRequest(login, password))
            ?.let { resp ->
                preferences.edit {
                    it[PreferencesKeys.isAuthorized] = true
                    it[PreferencesKeys.sessionToken] = resp.accessToken
                    it[PreferencesKeys.isGuest] = false
                    it[PreferencesKeys.username] = resp.login
                    it[PreferencesKeys.userId] = resp.id
                }
            } != null
    }

    override suspend fun continueAsGuest(userName: String) {
        preferences.edit {
            it[PreferencesKeys.isGuest] = true
            it[PreferencesKeys.isAuthorized] = true
            it[PreferencesKeys.username] = userName
        }
    }

    override suspend fun setNewCurrentBoardId(localId: Int) {
        preferences.edit {
            it[PreferencesKeys.boardId] = localId
        }
    }

    override suspend fun logout() {
        preferences.edit {
            it[PreferencesKeys.isAuthorized] = false
            it[PreferencesKeys.isGuest] = false
            it[PreferencesKeys.sessionToken] = ""
        }
    }
}