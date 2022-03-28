package com.example.bankan.common

enum class BankanScreen {
    Authentication, BoardsList, Board, Settings;

    companion object {
        private val association = values().associateBy { it.name }

        // TODO fix nest in Main
        fun fromRoute(route: String?): BankanScreen =
            route?.substringBefore("/")?.let {
                association[it] ?: throw IllegalArgumentException("Route $route is not recognized.")
            } ?: Authentication
    }
}