package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.store.room.BoardInfoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BoardInfoRepository {
    fun getAllBoards(): Flow<List<BoardInfo>>
    suspend fun addBoard(boardInfo: BoardInfo)
    suspend fun deleteBoard(localId: Int)
    suspend fun deleteBoard(boardInfo: BoardInfo)
}

class LocalBoardInfoRepositoryImpl : BoardInfoRepository, KoinComponent {
    private val boardInfoDao: BoardInfoDao by inject()

    override fun getAllBoards(): Flow<List<BoardInfo>> {
        return boardInfoDao.getAll()
    }

    override suspend fun addBoard(boardInfo: BoardInfo) {
        boardInfoDao.insert(boardInfo)
    }

    override suspend fun deleteBoard(localId: Int) {
        boardInfoDao.deleteByLocalId(localId)
    }

    override suspend fun deleteBoard(boardInfo: BoardInfo) {
        boardInfoDao.deleteBoards(boardInfo)
    }
}

class FakeBoardInfoRepository : BoardInfoRepository {
    private var list = MutableStateFlow(
        listOf(BoardInfo(name = "One"), BoardInfo(name = "Two"))
    )

    override fun getAllBoards(): Flow<List<BoardInfo>> {
        return list
    }

    override suspend fun addBoard(boardInfo: BoardInfo) {
        list.value = list.value + boardInfo
    }

    override suspend fun deleteBoard(localId: Int) {
        list.value = list.value.toMutableList().apply { removeAt(index = localId) }
    }

    override suspend fun deleteBoard(boardInfo: BoardInfo) {
        TODO("Not yet implemented")
    }
}