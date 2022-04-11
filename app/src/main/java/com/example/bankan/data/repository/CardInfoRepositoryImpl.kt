package com.example.bankan.data.repository

import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.store.room.CardInfoDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CardInfoRepositoryImpl : CardInfoRepository, KoinComponent {

    private val cardDao: CardInfoDao by inject()

    override fun getAll(listId: Int): Flow<List<CardInfo>> {
        return cardDao.getAll(listId)
    }

    override suspend fun add(cardInfo: CardInfo) {
        cardDao.insert(cardInfo)
    }

    override suspend fun delete(localId: Int) {
        cardDao.deleteByLocalId(localId)
    }

    override suspend fun delete(cardInfo: CardInfo) {
        cardDao.delete(cardInfo)
    }

    override suspend fun deleteByListId(listLocalId: Int) {
        cardDao.deleteByListId(listLocalId)
    }

}
