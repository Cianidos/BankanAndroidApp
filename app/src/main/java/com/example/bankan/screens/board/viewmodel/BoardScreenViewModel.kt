package com.example.bankan.screens.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.ListInfo
import com.example.bankan.data.repository.BoardInfoRepository
import com.example.bankan.data.repository.CardInfoRepository
import com.example.bankan.data.repository.ListInfoRepository
import com.example.bankan.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@OptIn(FlowPreview::class)
class BoardScreenViewModel : ViewModel(), KoinComponent {
    private val boardRepository: BoardInfoRepository by inject()
    private val listRepository: ListInfoRepository by inject()
    private val cardRepository: CardInfoRepository by inject()
    private val profileRepository: ProfileRepository by inject()

    private val _boardId = profileRepository.currentBoardId


    var newListName: MutableStateFlow<String> = MutableStateFlow("")
    var isEnteringNewListName: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var newCardName: MutableStateFlow<String> = MutableStateFlow("")
    val isEnteringNewCardName: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val currentListCardFocus: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun boardInfo(): Flow<BoardInfo> = _boardId.flatMapConcat { boardRepository.getOne(it!!) }
    fun listInfo(): Flow<List<ListInfo>> = _boardId.flatMapConcat { listRepository.getAll(it!!) }
    fun cardInfo(listId: Int): Flow<List<CardInfo>> = cardRepository.getAll(listId = listId)
    fun listData(): Flow<List<Pair<ListInfo, Flow<List<CardInfo>>>>> =
        listInfo().map { listOfList -> listOfList.map { it to cardInfo(it.localId) } }

    fun deleteCard(cardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.delete(cardId)
        }
    }

    fun addNewList(listInfo: ListInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            listRepository.add(listInfo)
        }
    }

    fun addNewCard(cardInfo: CardInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.add(cardInfo)
        }
    }
}