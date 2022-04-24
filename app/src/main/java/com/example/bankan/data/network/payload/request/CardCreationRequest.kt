package com.example.bankan.data.network.payload.request

import java.time.LocalDate

data class CardCreationRequest(
    var name: String,
    var color: Int? = 0,
    var deadline: LocalDate? = null,
    var cardContent: String = "{}"
)

