package com.example.bankan.screens

import com.example.bankan.screens.autheneication.viewmodel.AuthenticationViewModel
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import com.example.bankan.screens.main.viewmodel.AnimationViewModel
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import com.example.bankan.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val screensModule = module {
    viewModelOf(::AuthenticationViewModel)

    viewModel { MainMenuViewModel() }
    viewModel { AnimationViewModel() }

    viewModel { BoardScreenViewModel() }

    viewModel { SettingsViewModel() }
}
