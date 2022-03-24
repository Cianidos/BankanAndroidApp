package com.example.bankan.screens.board.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.board.data.*

@Preview
@Composable
fun BoardScreenContentPreview(modifier: Modifier = Modifier) {
    val listData = ListData(
        ListInfo("List list"),
        listOf(
            CardInfo("SomeName1", "Some Long Description bla bla bla, ha ha ha"),
            CardInfo("", "Some Long Description"),
            CardInfo("SomeName3", "Some Long Description")
        )
    )
    BoardScreenContent(
        data = BoardData(
            BoardInfo(name = "My Board"), listOf(
                listData,
                listData.copy(second = listData.second.toMutableList().apply { removeAt(1) }),
                listData.copy(first = ListInfo(name = "To List or Not To List")),
                listData.copy(second = listData.second + listData.second + listData.second)
            )
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardScreenContent(modifier: Modifier = Modifier, data: BoardData) {
    BankanTheme {
        LazyColumn {
            stickyHeader {
                androidx.compose.material.Card {
                    Text(text = data.first.name)
                }
            }
            item {
                LazyRow(modifier = modifier) {
                    eachAndBetween(data = data.second) {
                        List1(data = it)
                    }
                }
            }
        }
    }
}


