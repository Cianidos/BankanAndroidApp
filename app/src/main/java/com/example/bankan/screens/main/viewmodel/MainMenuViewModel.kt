package com.example.bankan.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bankan.data.models.BoardInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BoardInfoRepository {
    fun getAllBoards(): List<BoardInfo>
    fun addBoard(boardInfo: BoardInfo)
    fun deleteBoard(index: Int)
}

class FakeBoardInfoRepository : BoardInfoRepository {
    private var list: List<BoardInfo> =
        mutableListOf(BoardInfo(name = "One"), BoardInfo(name = "Two"))

    override fun getAllBoards(): List<BoardInfo> {
        return list
    }

    override fun addBoard(boardInfo: BoardInfo) {
        list = list + boardInfo
    }

    override fun deleteBoard(index: Int) {
        list = list.toMutableList().apply { removeAt(index = index) }
    }
}

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
            boardInfoRepository.getAllBoards(),
            ""
        )
    )
    val uiModel = _uiModel.asStateFlow()

    private fun loadBoards() {
        _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.Loading)
        val boards = boardInfoRepository.getAllBoards()
        _uiModel.value = _uiModel.value.copy(boardInfoList = boards, state = MainMenuUiStates.View)
    }

    fun createNewBoard() {
        _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.EnteringName)
    }

    fun changeNewBoardName(updatedName: String) {
        _uiModel.value = _uiModel.value.copy(newBoardName = updatedName)
    }

    fun submitNewBoard() {
        _uiModel.value = _uiModel.value.copy(state = MainMenuUiStates.View)
        boardInfoRepository.addBoard(BoardInfo(name = _uiModel.value.newBoardName))
        _uiModel.value = _uiModel.value.copy(newBoardName = "")
        loadBoards()
    }

    fun discardNewBoard() {
        _uiModel.value = _uiModel.value.copy(newBoardName = "", state = MainMenuUiStates.View)
    }

    fun deleteBoard(index: Int) {
        boardInfoRepository.deleteBoard(index)
        loadBoards()
    }
}

