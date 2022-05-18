package com.example.bankan.data.network.payload.request

import com.example.bankan.data.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CardPatchRequest(
    var name: String? = null,
    var changeColor: Boolean = false,
    var color: Int? = null,
    var changeDeadline: Boolean = false,
    @Serializable(LocalDateSerializer::class)
    var deadline: LocalDate? = null,
    var cardContent: String? = null
)
