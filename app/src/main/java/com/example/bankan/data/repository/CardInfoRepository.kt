package com.example.bankan.data.repository

import com.example.bankan.data.models.CardInfo
import kotlinx.coroutines.flow.Flow

interface CardInfoRepository {
    fun getAll(listId: Int): Flow<List<CardInfo>>
    suspend fun add(cardInfo: CardInfo)
    suspend fun delete(localId: Int)
    suspend fun delete(cardInfo: CardInfo)
    suspend fun deleteByListId(listLocalId: Int)
}