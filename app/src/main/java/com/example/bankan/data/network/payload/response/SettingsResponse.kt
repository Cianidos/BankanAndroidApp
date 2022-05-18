package com.example.bankan.data.network.payload.response

import kotlinx.serialization.Serializable

@Serializable
class SettingsResponse(
    // TODO: I dont know how but it must be Any?
    val settings: String?
)


