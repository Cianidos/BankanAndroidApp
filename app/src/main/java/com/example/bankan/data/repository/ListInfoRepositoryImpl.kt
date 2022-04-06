package com.example.bankan.data.repository

import com.example.bankan.data.models.ListInfo
import com.example.bankan.data.store.room.ListInfoDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ListInfoRepositoryImpl : ListInfoRepository, KoinComponent{

    private val listDao: ListInfoDao by inject()

    override fun getAll(boardId: Int): Flow<List<ListInfo>> {
        return listDao.getAll(boardId = boardId)
    }

    override suspend fun add(listInfo: ListInfo) {
        listDao.insert(listInfo)
    }

    override suspend fun delete(localId: Int) {
        listDao.deleteByLocalId(localId)
    }

    override suspend fun delete(boardInfo: ListInfo) {
        listDao.delete(boardInfo)
    }
}