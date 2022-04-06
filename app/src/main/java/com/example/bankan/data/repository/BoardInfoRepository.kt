package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import kotlinx.coroutines.flow.Flow

interface BoardInfoRepository {
    fun getAll(): Flow<List<BoardInfo>>
    suspend fun add(boardInfo: BoardInfo)
    suspend fun delete(localId: Int)
    suspend fun delete(boardInfo: BoardInfo)
}