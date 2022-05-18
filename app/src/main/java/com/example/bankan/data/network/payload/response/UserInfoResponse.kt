package com.example.bankan.data.network.payload.response

import com.example.bankan.data.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserInfoResponse(
    val id: Int? = null,
    val name: String? = null,
    @Serializable(LocalDateSerializer::class)
    val registrationDate: LocalDate? = null
)

