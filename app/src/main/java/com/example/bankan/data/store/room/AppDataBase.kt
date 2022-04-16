package com.example.bankan.data.store.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.bankan.data.models.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.util.*

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

//    @TypeConverter
//    fun tagToString(value: CardTag): String = JsonCoder.encodeToString(value)
//
//    @TypeConverter
//    fun stringToTag(value: String): CardTag = JsonCoder.decodeFromString(value)

    @TypeConverter
    fun listTagToString(value: List<CardTag>): String = JsonCoder.encodeToString(value)

    @TypeConverter
    fun stringToListTag(value: String): List<CardTag> = JsonCoder.decodeFromString(value)
}


@Database(
    entities = [BoardInfo::class, ListInfo::class, CardInfo::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun boardInfoDao(): BoardInfoDao
    abstract fun listInfoDao(): ListInfoDao
    abstract fun cardInfoDao(): CardInfoDao
}
