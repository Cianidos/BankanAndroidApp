package com.example.bankan.screens.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.models.BoardData
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.ListData
import com.example.bankan.data.repository.BoardInfoRepository
import com.example.bankan.data.repository.CardInfoRepository
import com.example.bankan.data.repository.ListInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UiState {

}

@OptIn(FlowPreview::class)
class BoardScreenViewModel : ViewModel(), KoinComponent {
    private val boardRepository: BoardInfoRepository by inject()
    private val listRepository: ListInfoRepository by inject()
    private val cardRepository: CardInfoRepository by inject()

    private val _data: MutableStateFlow<BoardData> =
        MutableStateFlow(BoardData(BoardInfo(""), listOf()))

    val data = _data.asStateFlow()

    private val _boardId = MutableStateFlow(1)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.getOne().collect {
                _boardId.value = it.localId
            }
            _boardId.collect { id ->
                boardRepository.getOne(boardId = id).map { boardInfo ->
                    listRepository.getAll(boardId = boardInfo.localId).map { listInfoList ->
                        BoardData(
                            boardInfo, listInfoList.map { listInfo ->
                                cardRepository.getAll(listId = listInfo.localId)
                                    .map { cardInfoList -> ListData(listInfo, cardInfoList) }
                            }.asFlow().flattenConcat().toList()
                        )
                    }
                }.flattenConcat().collect {
                    _data.value = it
                }
            }
        }
    }

    fun setCurrentBoard(boardId: Int) {
        if (boardId > 0)
            _boardId.value = boardId
    }
}