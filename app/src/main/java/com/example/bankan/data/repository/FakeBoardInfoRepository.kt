package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBoardInfoRepository : BoardInfoRepository {
    private var list = MutableStateFlow(
        listOf(BoardInfo(name = "One"), BoardInfo(name = "Two"))
    )

    override fun getOne(): Flow<BoardInfo> {
        TODO("Not yet implemented")
    }

    override fun getOne(boardId: Int): Flow<BoardInfo> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<BoardInfo>> {
        return list
    }

    override suspend fun add(boardInfo: BoardInfo) {
        list.value = list.value + boardInfo
    }

    override suspend fun delete(localId: Int) {
        list.value = list.value.toMutableList().apply { removeAt(index = localId) }
    }

    override suspend fun delete(boardInfo: BoardInfo) {
        TODO("Not yet implemented")
    }
}