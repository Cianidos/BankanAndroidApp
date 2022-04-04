package com.example.bankan.data.models

import java.time.Instant
import java.util.*

data class BoardData(val info: BoardInfo, val content: List<ListData>)

data class BoardInfo(
    val id: Int = -1,
    val name: String,
    val description: String = "",
    val isOpen: Boolean = false,
    val creationDate: Date = Date.from(Instant.EPOCH)
)

data class ListData(val info: ListInfo, val content: List<CardInfo>)

data class ListInfo(val id: Int = -1, val name: String, val description: String = "")

data class CardInfo(val id: Int = -1, val name: String, val description: String = "")
