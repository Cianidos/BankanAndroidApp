package com.example.bankan.screens.autheneication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.repository.ProfileRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthenticationViewModel : KoinComponent, ViewModel() {

    private val profileRepository: ProfileRepository by inject()

    private val _uiState = MutableStateFlow(AuthenticationState())
    val uiState = _uiState.asStateFlow()

    private fun changeAuthenticationMode(newAuthenticationMode: AuthenticationMode) {
        _uiState.value = _uiState.value.copy(
            authenticationMode = newAuthenticationMode
        )
    }

    private fun updateNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(
            nickname = nickname
        )
    }

    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }


    private fun updatePassword(password: String) {

        val requirements = mutableListOf<PasswordRequirements>()
        if (password.length > 7) {
            requirements.add(PasswordRequirements.EIGHT_CHARACTERS)
        }
        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirements.CAPITAL_LETTER)
        }
        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirements.NUMBER)
        }
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordRequirements = requirements.toList()
        )
    }


    private fun authenticate() {
        when (_uiState.value.authenticationMode) {
            AuthenticationMode.GUEST -> {
                _uiState.value = _uiState.value.copy(isAuthenticated = true)
                viewModelScope.launch(Dispatchers.IO) {
                    profileRepository.continueAsGuest(_uiState.value.nickname)
                }
            }
//            AuthenticationMode.SIGN_UP -> TODO()
            AuthenticationMode.SIGN_IN -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = true
                )
                runBlocking {
                    if (profileRepository.authorize(
                            _uiState.value.email,
                            _uiState.value.password
                        )
                    ) {
                        _uiState.value =
                            _uiState.value.copy(isLoading = false, isAuthenticated = true)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Incorrect login or password"
                        )
                    }
                }
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = true
                )
                viewModelScope.launch(Dispatchers.IO) {
                    delay(2000L)

                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Something went wrong !"
                        )
                    }
                }
            }

        }
    }

    private fun dismissError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }


    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ChangeAuthenticationMode ->
                changeAuthenticationMode(authenticationEvent.newAuthenticationMode)
            is AuthenticationEvent.EmailChanged ->
                updateEmail(authenticationEvent.emailAddress)
            is AuthenticationEvent.PasswordChanged ->
                updatePassword(authenticationEvent.password)
            is AuthenticationEvent.Authenticate ->
                authenticate()
            is AuthenticationEvent.ErrorDismissed ->
                dismissError()
            is AuthenticationEvent.NicknameChanged ->
                updateNickname(authenticationEvent.nickname)
        }
    }

}
