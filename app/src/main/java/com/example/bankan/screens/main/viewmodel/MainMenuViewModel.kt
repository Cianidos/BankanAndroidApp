package com.example.bankan.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.screens.board.data.BoardInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BoardInfoRepository {
    fun getAllBoards(): List<BoardInfo>
    fun addBoard(boardInfo: BoardInfo)
    fun deleteBoard(index: Int)
}

class FakeBoardInfoRepository : BoardInfoRepository {
    val list: MutableList<BoardInfo> = mutableListOf(BoardInfo(name = "One"), BoardInfo(name = "Two"))
    override fun getAllBoards(): List<BoardInfo> {
        return list
    }

    override fun addBoard(boardInfo: BoardInfo) {
        list.add(boardInfo)
    }

    override fun deleteBoard(index: Int) {
    }
}

enum class MainMenuStates {
    View, Loading, EnteringName
}

class MainMenuViewModel : ViewModel(), KoinComponent {

    private val boardInfoRepository: BoardInfoRepository by inject()

    private val _state = MutableStateFlow(MainMenuStates.Loading)
    val state = _state.asStateFlow()

    private val _boardInfoList =
        MutableStateFlow<List<BoardInfo>>(listOf())
    val boardInfoList = _boardInfoList.asStateFlow()

    private val _newBoardName = MutableStateFlow("")
    val newBoardName = _newBoardName.asStateFlow()

    init {
        loadBoards()
    }

    fun loadBoards() {
        viewModelScope.launch(Dispatchers.IO) {
            val boards = boardInfoRepository.getAllBoards()
            _state.value = MainMenuStates.View
            _boardInfoList.value = boards
        }
    }

    fun createNewBoard() {
        _state.value = MainMenuStates.EnteringName
    }

    fun changeNewBoardName(updatedName: String) {
        _newBoardName.value = updatedName
    }

    fun submitNewBoard() {
        _state.value = MainMenuStates.View
        boardInfoRepository.addBoard(BoardInfo(_newBoardName.value))
        _newBoardName.value = ""
    }

    fun discardNewBoard() {
        _state.value = MainMenuStates.View
        _newBoardName.value = ""
    }

    fun deleteBoard(index: Int) {
        boardInfoRepository.deleteBoard(index)
    }
}

