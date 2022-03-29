package com.example.bankan.screens.board.data

typealias BoardData = Pair<BoardInfo, List<ListData>>

data class BoardInfo(val name: String)

typealias ListData = Pair<ListInfo, List<CardInfo>>

data class CardInfo(val name: String, val description: String)

data class ListInfo(val name: String)