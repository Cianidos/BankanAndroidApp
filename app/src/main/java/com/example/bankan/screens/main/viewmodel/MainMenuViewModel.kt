package com.example.bankan.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.repository.BoardInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class MainMenuUiModel(
    val state: MainMenuUiStates,
    val boardInfoList: List<BoardInfo>,
    val newBoardName: String
)

enum class MainMenuUiStates {
    View, Loading, EnteringName
}

class MainMenuViewModel : ViewModel(), KoinComponent {

    private val boardInfoRepository: BoardInfoRepository by inject()

    private val _uiModel = MutableStateFlow(
        MainMenuUiModel(
            MainMenuUiStates.View,
            listOf(),
            ""
        )
    )

    val uiModel = _uiModel.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            boardInfoRepository.getAll().distinctUntilChanged().collect {
                _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.Loading)
                _uiModel.value = _uiModel.value.copy(boardInfoList = it, state = MainMenuUiStates.View)
            }
        }
    }

    fun createNewBoard() {
        _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.EnteringName)
    }

    fun changeNewBoardName(updatedName: String) {
        _uiModel.value = _uiModel.value.copy(newBoardName = updatedName)
    }

    fun submitNewBoard() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.View)
            boardInfoRepository.add(BoardInfo(name = _uiModel.value.newBoardName))
            _uiModel.value = _uiModel.value.copy(newBoardName = "")
        }
    }

    fun discardNewBoard() {
        _uiModel.value = _uiModel.value.copy(newBoardName = "", state = MainMenuUiStates.View)
    }

    fun deleteBoard(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            boardInfoRepository.delete(index)
        }
    }
}

