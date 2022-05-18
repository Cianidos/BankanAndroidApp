package com.example.bankan.data.network.payload.response

import com.example.bankan.data.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*


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
    @Serializable(DateSerializer::class)
    var creationData: Date? = null
)
