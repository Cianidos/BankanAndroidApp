package com.example.bankan.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.repository.BoardInfoRepository
import com.example.bankan.data.repository.CardInfoRepository
import com.example.bankan.data.repository.ListInfoRepository
import com.example.bankan.data.repository.ProfileRepository
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
    private val listInfoRepository: ListInfoRepository by inject()
    private val cardInfoRepository: CardInfoRepository by inject()
    private val profileRepository: ProfileRepository by inject()

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
                _uiModel.value =
                    _uiModel.value.copy(boardInfoList = it, state = MainMenuUiStates.View)
            }
        }
    }


    fun chooseCurrentBoard(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.setNewCurrentBoardId(id)
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


    fun deleteBoard(localId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                listInfoRepository.getAll(localId).collect { listOfListInfo ->
                    listOfListInfo.forEach {
                        launch {
                            cardInfoRepository.deleteByListId(it.localId)
                        }
                        listInfoRepository.delete(it.localId)
                    }
                }
            }
            launch {
                boardInfoRepository.delete(localId = localId)
            }
        }
    }
}

