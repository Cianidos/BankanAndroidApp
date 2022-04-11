package com.example.bankan.screens.board.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
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
    private val profileRepository: ProfileRepository by inject()

    private val _boardId =
        profileRepository.currentBoardId//.shareIn(viewModelScope, SharingStarted.Eagerly)
    private var listCounter = 0
    private var cardCounter = 0

    fun boardInfo(): Flow<BoardInfo> = _boardId.flatMapConcat { boardRepository.getOne(it!!) }
    fun listInfo(): Flow<List<ListInfo>> = _boardId.flatMapConcat { listRepository.getAll(it!!) }
    fun cardInfo(listId: Int): Flow<List<CardInfo>> = cardRepository.getAll(listId = listId)
    fun listData(): Flow<List<Pair<ListInfo, Flow<List<CardInfo>>>>> =
        listInfo().map { it.map { it to cardRepository.getAll(it.localId) } }

    fun addNewList(boardId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listRepository.add(ListInfo("hhaa $listCounter", boardId = boardId))
            Log.d("AAAAAAAAAAAAAA", "ADD LIST $listCounter")
        }
        listCounter++
    }

    fun addNewCard(listId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.add(CardInfo("hhaa $cardCounter", listId = listId))
            Log.d("AAAAAAAAAAAAAA", "ADD CARD $cardCounter")
        }
        cardCounter++
    }
}