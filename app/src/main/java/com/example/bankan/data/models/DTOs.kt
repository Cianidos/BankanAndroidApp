package com.example.bankan.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

data class BoardData(val info: BoardInfo, val content: List<ListData>)

@Entity
data class BoardInfo(
    val name: String,
    val description: String = "",
    val isOpen: Boolean = false,
    val creationDate: Date = Date.from(Instant.EPOCH),
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)

data class ListData(val info: ListInfo, val content: List<CardInfo>)

@Entity
data class ListInfo(
    val name: String,
    val description: String = "",

    val boardId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)

@Entity
data class CardInfo(
    val name: String,
    val description: String = "",

    val listId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)
