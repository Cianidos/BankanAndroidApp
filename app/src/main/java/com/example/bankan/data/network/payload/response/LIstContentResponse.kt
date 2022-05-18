package com.example.bankan.data.network.payload.response

import kotlinx.serialization.Serializable

@Serializable
data class ListContentResponse(
    val data: List<CardResponse>
)