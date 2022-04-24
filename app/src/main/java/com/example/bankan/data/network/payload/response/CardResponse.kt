package com.example.bankan.data.network.payload.response

import java.time.LocalDate

data class CardResponse(
    var id: Int? = null,
    var name: String? = null,
    var color: Int? = null,
    var creationData: LocalDate? = null,
    var deadline: LocalDate? = null,
    var creatorId: Int? = null,
    var cardContent: String? = null
)

