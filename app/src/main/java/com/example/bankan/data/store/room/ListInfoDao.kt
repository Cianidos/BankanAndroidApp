package com.example.bankan.data.store.room

import androidx.room.*
import com.example.bankan.data.models.ListInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface ListInfoDao {
    @Insert
    suspend fun insert(list: ListInfo)

    @Update
    suspend fun update(vararg lists: ListInfo)

    @Delete
    suspend fun delete(vararg lists: ListInfo)

    @Query("DELETE FROM ListInfo WHERE localId = :listId")
    suspend fun deleteByLocalId(listId: Int)

    @Query("SELECT * FROM ListInfo WHERE boardId = :boardId")
    fun getAll(boardId: Int): Flow<List<ListInfo>>
}