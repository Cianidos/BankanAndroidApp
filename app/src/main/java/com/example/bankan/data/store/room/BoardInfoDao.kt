package com.example.bankan.data.store.room

import androidx.room.*
import com.example.bankan.data.models.BoardInfo
import kotlinx.coroutines.flow.Flow


@Dao
interface BoardInfoDao {
    @Insert
    suspend fun insert(board: BoardInfo)

    @Update
    suspend fun update(vararg boards: BoardInfo)

    @Delete
    suspend fun delete(vararg boards: BoardInfo)

    @Query("DELETE FROM BoardInfo WHERE localId = :boardId")
    suspend fun deleteByLocalId(boardId: Int)

    @Query("SELECT * FROM BoardInfo")
    fun getAll(): Flow<List<BoardInfo>>

    @Query("SELECT * FROM BoardInfo WHERE localId = :boardId")
    fun getOne(boardId: Int): Flow<BoardInfo>

    @Query("SELECT * FROM BoardInfo LIMIT 1")
    fun getOne(): Flow<BoardInfo>
}

@Dao
interface AuthedBoardInfoDao {
    @Insert
    suspend fun insert(board: BoardInfo)

    @Update
    suspend fun update(vararg boards: BoardInfo)

    @Delete
    suspend fun delete(vararg boards: BoardInfo)

    @Query("DELETE FROM BoardInfo WHERE localId = :boardId")
    suspend fun deleteByLocalId(boardId: Int)

    @Query("SELECT * FROM BoardInfo where id != null")
    fun getAll(): Flow<List<BoardInfo>>

    @Query("SELECT * FROM BoardInfo WHERE localId = :boardId and id != null")
    fun getOne(boardId: Int): Flow<BoardInfo>

    @Query("SELECT * FROM BoardInfo where id != 0 LIMIT 1")
    fun getOne(): Flow<BoardInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(vararg board: BoardInfo): Long

    @Transaction
    suspend fun insertOrUpdate(vararg boards: BoardInfo) {
        boards.forEach { board ->
            if (insertIgnore(board) == -1L) {
                update(board)
            }
        }
    }
}
