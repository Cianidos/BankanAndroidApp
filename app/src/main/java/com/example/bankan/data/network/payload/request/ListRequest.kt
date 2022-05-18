package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable

// TODO: add exceptions on length of name and description
@Serializable
data class ListRequest(
    val name: String,
    val description: String
)

