package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoPatchRequest (
    val name : String? = null
)