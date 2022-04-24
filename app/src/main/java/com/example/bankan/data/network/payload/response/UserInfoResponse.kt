package com.example.bankan.data.network.payload.response

import java.time.LocalDate

data class UserInfoResponse(
    val id: Int? = null,
    val name: String? = null,
    val registrationDate: LocalDate? = null
)

