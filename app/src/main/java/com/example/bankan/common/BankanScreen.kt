package com.example.bankan.common

enum class BankanScreen {
    Authentication, Boards, Settings;

    companion object {
        private val association = values().associateBy { it.name }

        fun fromRoute(route: String?): BankanScreen =
            route?.substringBefore("/")?.let {
                association[it] ?: throw IllegalArgumentException("Route $route is not recognized.")
            } ?: Authentication
    }
}