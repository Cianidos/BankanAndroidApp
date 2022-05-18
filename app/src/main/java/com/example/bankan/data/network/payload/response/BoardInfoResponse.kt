package com.example.bankan.data.network.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class BoardInfoResponse(
    val name: String,
    val description: String,
    val creationDate: String,
    val isOpen: Boolean
)
