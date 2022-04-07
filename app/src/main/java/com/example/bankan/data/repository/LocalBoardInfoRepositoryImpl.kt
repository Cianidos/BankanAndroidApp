package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.store.room.BoardInfoDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocalBoardInfoRepositoryImpl : BoardInfoRepository, KoinComponent {
    private val boardInfoDao: BoardInfoDao by inject()

    override fun getOne(): Flow<BoardInfo> {
        return boardInfoDao.getOne()
    }

    override fun getOne(boardId: Int): Flow<BoardInfo> {
        return boardInfoDao.getOne(boardId = boardId)
    }

    override fun getAll(): Flow<List<BoardInfo>> {
        return boardInfoDao.getAll()
    }

    override suspend fun add(boardInfo: BoardInfo) {
        boardInfoDao.insert(boardInfo)
    }

    override suspend fun delete(localId: Int) {
        boardInfoDao.deleteByLocalId(localId)
    }

    override suspend fun delete(boardInfo: BoardInfo) {
        boardInfoDao.delete(boardInfo)
    }
}