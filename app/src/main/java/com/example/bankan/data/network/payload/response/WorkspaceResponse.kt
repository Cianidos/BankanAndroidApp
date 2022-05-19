package com.example.bankan.data.network.payload.response

import com.example.bankan.data.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class WorkspaceResponse(
    val id: Int,
    val name: String,
    val listOfBoardEntities: List<BoardInfoResponseAndId>
)

@Serializable
class BoardInfoResponseAndId(
    var id: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var isOpen: Boolean = false,
    @Serializable(LocalDateSerializer::class)
    var creationData: LocalDate? = null
)
