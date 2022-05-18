package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable

@Serializable
data class BoardCreateRequest(
    val name: String,
    val description: String
)

