package com.example.bankan.data.network.payload.response

data class BoardInfoResponse(
    val name: String,
    val description: String,
    val creationDate: String,
    val isOpen: Boolean
)
