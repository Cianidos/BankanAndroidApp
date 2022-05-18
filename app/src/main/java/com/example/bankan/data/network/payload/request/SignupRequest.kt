package com.example.bankan.data.network.payload.request

import kotlinx.serialization.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class SignupRequest(
    @NotBlank
    @Size(min = 3, max = 20)
    var username: String,

    @NotBlank
    @Size(max = 50)
    @Email
    var login: String,

    @NotBlank
    @Size(min = 6, max = 40)
    var password: String,

    var role: Set<String> = emptySet(),
)