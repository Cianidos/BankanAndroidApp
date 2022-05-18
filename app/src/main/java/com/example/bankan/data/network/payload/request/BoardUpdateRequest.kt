package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable

@Serializable
data class BoardUpdateRequest(
    val name: String? = null,
    val description: String? = null
)

