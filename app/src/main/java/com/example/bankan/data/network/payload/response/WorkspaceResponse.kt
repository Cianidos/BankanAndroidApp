package com.example.bankan.data.network.payload.response

import java.util.*


data class WorkspaceResponse(
    val id: Int,
    val name: String,
    val listOfBoardEntities: List<BoardInfoResponseAndId>
)

class BoardInfoResponseAndId(
    var id: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var isOpen: Boolean = false,
    var creationData: Date? = null
)
