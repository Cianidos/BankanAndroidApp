package com.example.bankan.data.repository

import com.example.bankan.data.models.ListInfo
import kotlinx.coroutines.flow.Flow

interface ListInfoRepository {
    fun getAll(boardId: Int): Flow<List<ListInfo>>
    suspend fun add(listInfo: ListInfo)
    suspend fun delete(localId: Int)
    suspend fun delete(listInfo: ListInfo)
}

