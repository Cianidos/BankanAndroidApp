package com.example.bankan.data.network.payload.response

import com.example.bankan.data.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CardResponse(
    var id: Int? = null,
    var name: String? = null,
    var color: Int? = null,
    @Serializable(LocalDateSerializer::class)
    var creationData: LocalDate? = null,
    @Serializable(LocalDateSerializer::class)
    var deadline: LocalDate? = null,
    var creatorId: Int? = null,
    var cardContent: String? = null
)

