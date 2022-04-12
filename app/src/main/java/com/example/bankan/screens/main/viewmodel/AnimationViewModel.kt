package com.example.bankan.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnimationViewModel : ViewModel() {
    private var job: Job? = null

    private val _times = MutableStateFlow(0)
    val times = _times.asStateFlow()

    private val _list = MutableStateFlow(
        listOf(
            "first board",
            "next",
            "1",
            "",
            "board next door"
        )
    )

    val list = _list.asStateFlow()

    fun deleteAtIndex(idx: Int) {
        _list.value = list.value.toMutableList().apply { removeAt(idx) }
    }

    init {
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(timeMillis = 50)
                _times.value += 1
            }
        }
    }
}