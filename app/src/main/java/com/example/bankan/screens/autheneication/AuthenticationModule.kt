package com.example.bankan.screens.autheneication

import com.example.bankan.screens.autheneication.viewmodel.AuthenticationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val authenticationModule = module {
    viewModelOf(::AuthenticationViewModel)
}