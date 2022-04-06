package com.example.bankan.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class MainState {
    sealed class Authorized : MainState() {
        object LoggedIn : Authorized()
        object Guest : Authorized()
    }

    object NotAuthorized : MainState()
    object Loading : MainState()
}

class NavigationViewModel : KoinComponent, ViewModel() {
    private val profileRepository: ProfileRepository by inject()
    private val isGuest: Flow<Boolean> = profileRepository.isGuest
    private val isAuthorized = profileRepository.isAuthorized
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            isGuest.collect { guest ->
                isAuthorized.collect { authorized ->
                    _state.value = if (authorized) {
                        if (guest) MainState.Authorized.Guest else MainState.Authorized.LoggedIn
                    } else {
                        MainState.NotAuthorized
                    }
                }
            }
        }
    }
}