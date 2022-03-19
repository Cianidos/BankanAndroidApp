package com.example.bankan.screens.autheneication.viewmodel

sealed class AuthenticationEvent {
    data class ChangeAuthenticationMode(val newAuthenticationMode: AuthenticationMode): AuthenticationEvent()
    data class NicknameChanged(val nickname: String) : AuthenticationEvent()
    data class EmailChanged(val emailAddress: String): AuthenticationEvent()
    data class PasswordChanged(val password: String): AuthenticationEvent()

    object Authenticate: AuthenticationEvent()
    object ErrorDismissed: AuthenticationEvent()
}
