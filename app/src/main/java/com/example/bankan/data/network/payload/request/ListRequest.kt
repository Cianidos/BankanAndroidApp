package com.example.bankan.data.network.payload.request

// TODO: add exceptions on length of name and description
data class ListRequest(
    val name: String,
    val description: String
)

