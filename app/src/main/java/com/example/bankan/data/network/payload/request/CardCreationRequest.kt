package com.example.bankan.data.network.payload.request

import com.example.bankan.data.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate



@Serializable
data class CardCreationRequest(
    var name: String,
    var color: Int? = 0,
    @Serializable(LocalDateSerializer::class)
    var deadline: LocalDate? = null,
    var cardContent: String = "{}"
)

