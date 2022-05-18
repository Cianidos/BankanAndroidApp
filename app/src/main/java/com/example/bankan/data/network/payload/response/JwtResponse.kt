package com.example.bankan.data.network.payload.response

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(
    var accessToken: String,
    var id: Int,
    var login: String,
    val roles: List<String>,
    val tokenType: String = "Bearer"
)