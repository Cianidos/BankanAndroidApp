package com.example.bankan.data.network.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class ListInfoResponse(
    val name: String,
    val description: String,
)

