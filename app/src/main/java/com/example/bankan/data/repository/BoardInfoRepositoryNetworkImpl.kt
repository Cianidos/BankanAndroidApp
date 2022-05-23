package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.network.BoardInfoInternetProvider
import com.example.bankan.data.store.room.AuthedBoardInfoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BoardInfoRepositoryNetworkImpl(other: BoardInfoRepository) : KoinComponent, BoardInfoRepository by other {
    private val authedBoardInfoDao: AuthedBoardInfoDao by inject()
    private val boardInfoInternetProvider: BoardInfoInternetProvider by inject()

    override fun getOne(): Flow<BoardInfo> {
        CoroutineScope(Dispatchers.IO).launch {
            boardInfoInternetProvider.refreshBoards()
        }
        return authedBoardInfoDao.getOne()
    }

    override fun getOne(boardId: Int): Flow<BoardInfo> {
        CoroutineScope(Dispatchers.IO).launch {
            boardInfoInternetProvider.refreshBoards()
        }
        return authedBoardInfoDao.getOne(boardId)
    }

    override fun getAll(): Flow<List<BoardInfo>> {
        CoroutineScope(Dispatchers.IO).launch {
            boardInfoInternetProvider.refreshBoards()
        }
        return authedBoardInfoDao.getAll()
    }

    override suspend fun add(boardInfo: BoardInfo) {
        boardInfoInternetProvider.addBoard(boardInfo)
        boardInfoInternetProvider.refreshBoards()
    }

    override suspend fun delete(localId: Int) {
        boardInfoInternetProvider.deleteBoard(localId)
        authedBoardInfoDao.deleteByLocalId(localId)
    }

    override suspend fun delete(boardInfo: BoardInfo) {
        boardInfoInternetProvider.deleteBoard(boardInfo)
        authedBoardInfoDao.delete(boardInfo)
    }

    override suspend fun update(boardInfo: BoardInfo) {
        boardInfoInternetProvider.update(boardInfo)
        boardInfoInternetProvider.refreshBoards()
    }
}