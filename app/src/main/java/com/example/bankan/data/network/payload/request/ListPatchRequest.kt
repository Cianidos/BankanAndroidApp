package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable

@Serializable
data class ListPatchRequest(
    val name: String?,
    val description: String?,
)
