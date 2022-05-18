package com.example.bankan.data.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bankan.data.ColorSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Immutable
data class BoardData(val info: BoardInfo, val content: List<ListData>)

@Immutable
@Entity
data class BoardInfo(
    val name: String,
    val description: String = "",
    val isOpen: Boolean = false,
    val creationDate: Date = Date.from(Instant.EPOCH),
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)

@Immutable
data class ListData(val info: ListInfo, val content: List<CardInfo>)

@Immutable
@Entity
data class ListInfo(
    val name: String,
    val description: String = "",

    val boardId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)


@Immutable
@Serializable
data class CardTag(
    val name: String,

    @Serializable(with = ColorSerializer::class)
    val color: Color
)


@Immutable
@Serializable
@Entity
data class CardInfo(
    val name: String,
    val description: String = "",
    val tags: List<CardTag> = emptyList(),

    val listId: Int = -1,
    val id: Int? = null,
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
)


