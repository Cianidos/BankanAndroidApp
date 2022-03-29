package com.example.bankan.screens.main

import com.example.bankan.screens.main.viewmodel.AnimationViewModel
import com.example.bankan.screens.main.viewmodel.BoardInfoRepository
import com.example.bankan.screens.main.viewmodel.FakeBoardInfoRepository
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mainMenuModule = module {
    viewModel { MainMenuViewModel() }
    viewModel { AnimationViewModel() }
    single { FakeBoardInfoRepository() as BoardInfoRepository }
}