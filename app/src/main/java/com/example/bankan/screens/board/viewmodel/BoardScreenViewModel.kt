package com.example.bankan.screens.board.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.models.*
import com.example.bankan.data.repository.BoardInfoRepository
import com.example.bankan.data.repository.CardInfoRepository
import com.example.bankan.data.repository.ListInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    //    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            boardRepository.getOne().collect {
//                _boardId.value = it.localId
//            }
//            _boardId.collect { id ->
//                boardRepository.getOne(boardId = id).map { boardInfo ->
//                    listRepository.getAll(boardId = boardInfo.localId).map { listInfoList ->
//                        BoardData(
//                            boardInfo, listInfoList.map { listInfo ->
//                                cardRepository.getAll(listId = listInfo.localId)
//                                    .map { cardInfoList -> ListData(listInfo, cardInfoList) }
//                            }.asFlow().flattenConcat().toList()
//                        )
//                    }
//                }.flattenConcat().collect {
//                    _data.value = it
//                }
//            }
//        }
//    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.getOne().collect {
                _boardId.value = it.localId
                launch {
                    _boardId.collect { id ->
                        launch {
                            boardRepository.getOne(boardId = id).collect { boardInfo ->
                                launch {
                                    listRepository.getAll(boardId = boardInfo.localId)
                                        .collect { listInfoList ->
                                            val lists = mutableListOf<ListData>()
                                            listInfoList.forEach { listInfo ->
                                                launch {
                                                    cardRepository.getAll(listId = listInfo.localId)
                                                        .collect { cardInfoList ->
                                                            lists += ListData(
                                                                listInfo,
                                                                cardInfoList
                                                            )
                                                        }
                                                }
                                                _data.value = BoardData(boardInfo, lists)
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setCurrentBoard(boardId: Int) {
        if (boardId > 0)
            _boardId.value = boardId
    }

    fun addNewList() {
        viewModelScope.launch(Dispatchers.IO) {
            listRepository.add(ListInfo("hhaa", boardId = _boardId.value))
            Log.d("AAAAAAAAAAAAAA", "ADD LIST")
        }
    }

    fun addNewCard(listId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.add(CardInfo("hhaa", listId = listId))
            Log.d("AAAAAAAAAAAAAA", "ADD CARD")
        }
    }
}