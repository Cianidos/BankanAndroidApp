package com.example.bankan.screens.autheneication.viewmodel

import androidx.annotation.StringRes
import com.example.bankan.R

enum class PasswordRequirements(
    @StringRes val label: Int
) {
    CAPITAL_LETTER(R.string.password_requirement_capital),
    NUMBER(R.string.password_requirement_digit),
    EIGHT_CHARACTERS(R.string.password_requirement_characters)
}