package com.example.bankan.data.store.room

import androidx.room.*
import com.example.bankan.data.models.BoardInfo
import kotlinx.coroutines.flow.Flow
import java.util.*

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(entities = [BoardInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun boardInfoDao(): BoardInfoDao
}

@Dao
interface BoardInfoDao {
    @Insert
    suspend fun insert(board: BoardInfo)
    @Update
    suspend fun update(vararg boards: BoardInfo)

    @Delete
    suspend fun deleteBoards(vararg boards: BoardInfo)

    @Query("DELETE FROM BoardInfo WHERE localId = :boardId")
    suspend fun deleteByLocalId(boardId: Int)

    @Query("SELECT * FROM BoardInfo")
    fun getAll(): Flow<List<BoardInfo>>
}