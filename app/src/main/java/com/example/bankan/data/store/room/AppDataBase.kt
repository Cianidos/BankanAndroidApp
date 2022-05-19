package com.example.bankan.data.store.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.CardTag
import com.example.bankan.data.models.ListInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? = date?.toEpochDay()

//    @TypeConverter
//    fun tagToString(value: CardTag): String = JsonCoder.encodeToString(value)
//
//    @TypeConverter
//    fun stringToTag(value: String): CardTag = JsonCoder.decodeFromString(value)

    @TypeConverter
    fun listTagToString(value: List<CardTag>): String = Json.encodeToString(value)

    @TypeConverter
    fun stringToListTag(value: String): List<CardTag> = Json.decodeFromString(value)
}


@Database(
    entities = [BoardInfo::class, ListInfo::class, CardInfo::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun boardInfoDao(): BoardInfoDao
    abstract fun listInfoDao(): ListInfoDao
    abstract fun cardInfoDao(): CardInfoDao
}
