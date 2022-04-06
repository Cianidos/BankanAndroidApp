package com.example.bankan.common

sealed class BankanScreen {
    val name: String = this::class.simpleName!!
    val route: String =
        this.javaClass.canonicalName!!.removePrefix("com.example.bankan.common.BankanScreen.")
            .replace('.', '/')

    object Authentication : BankanScreen()
    object Loading : BankanScreen()
    sealed class Main : BankanScreen() {
        object BoardsList : Main()
        object Board : Main()
        object Settings : Main()
    }
}