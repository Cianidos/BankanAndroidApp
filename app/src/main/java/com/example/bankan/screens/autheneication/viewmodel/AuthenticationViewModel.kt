package com.example.bankan.screens.autheneication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationState())
    val uiState = _uiState.asStateFlow()

    private fun changeAuthenticationMode(newAuthenticationMode: AuthenticationMode) {
        _uiState.value = _uiState.value.copy(
            authenticationMode = newAuthenticationMode
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
//        when (_uiState.value.authenticationMode) {
//            AuthenticationMode.SIGN_UP -> TODO()
//            AuthenticationMode.SIGN_IN -> TODO()
//            AuthenticationMode.GUEST -> TODO()
//        }
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


    private fun dismissError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }


    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ChangeAuthenticationMode -> {
                changeAuthenticationMode(authenticationEvent.newAuthenticationMode)
            }
            is AuthenticationEvent.EmailChanged -> {
                updateEmail(authenticationEvent.emailAddress)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(authenticationEvent.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate()
            }
            is AuthenticationEvent.ErrorDismissed -> {
                dismissError()
            }
        }
    }

}
