package com.example.bankan.data.network.payload.request

import io.ktor.resources.*
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    var login: String,
    var password: String,
)