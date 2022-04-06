package com.example.bankan.data.store.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bankan.data.models.CardInfo
import kotlinx.coroutines.flow.Flow

interface CardInfoDao {
    @Insert
    suspend fun insert(card: CardInfo)

    @Update
    suspend fun update(vararg cards: CardInfo)

    @Delete
    suspend fun delete(vararg cards: CardInfo)

    @Query("DELETE FROM CardInfo WHERE localId = :cardId")
    suspend fun deleteByLocalId(cardId: Int)

    @Query("SELECT * FROM CardInfo WHERE listId = :listId")
    fun getAll(listId: Int): Flow<List<CardInfo>>
}