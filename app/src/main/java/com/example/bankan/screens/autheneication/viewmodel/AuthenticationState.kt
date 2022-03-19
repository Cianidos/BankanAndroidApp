package com.example.bankan.screens.autheneication.viewmodel

data class AuthenticationState(
    val authenticationMode: AuthenticationMode = AuthenticationMode.SIGN_IN,
    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordRequirements: List<PasswordRequirements> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    fun isFormValid(): Boolean {
        return password.isNotEmpty() && email.isNotEmpty() &&
                (authenticationMode == AuthenticationMode.SIGN_IN ||
                        passwordRequirements.containsAll(
                            PasswordRequirements.values().toList()
                        ))
    }
}