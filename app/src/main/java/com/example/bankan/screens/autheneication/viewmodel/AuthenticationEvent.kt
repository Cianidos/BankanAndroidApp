package com.example.bankan.screens.autheneication.viewmodel

sealed class AuthenticationEvent {
    class ChangeAuthenticationMode(val newAuthenticationMode: AuthenticationMode): AuthenticationEvent()
    class EmailChanged(val emailAddress: String): AuthenticationEvent()
    class PasswordChanged(val password: String): AuthenticationEvent()
    object Authenticate: AuthenticationEvent()
    object ErrorDismissed: AuthenticationEvent()
}
