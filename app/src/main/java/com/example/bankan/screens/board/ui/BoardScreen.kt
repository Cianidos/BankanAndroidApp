package com.example.bankan.screens.board.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.*
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import org.koin.androidx.compose.viewModel

@Preview
@Composable
fun BoardScreenContentPreview(modifier: Modifier = Modifier) {
    val listData = ListData(
        ListInfo(name = "List list"),
        listOf(
            CardInfo(
                name = "SomeName1",
                description = "Some Long Description bla bla bla, ha ha ha"
            ),
            CardInfo(name = "", description = "Some Long Description"),
            CardInfo(name = "SomeName3", description = "Some Long Description")
        )
    )
    val bd = BoardData(
        BoardInfo(name = "My Board"), listOf(
            listData,
            listData.copy(content = listData.content.toMutableList().apply { removeAt(1) }),
            listData.copy(info = ListInfo(name = "To List or Not To List")),
            listData.copy(content = listData.content + listData.content + listData.content)
        )
    )
    BoardScreenContent(
        data = bd, onAddNewCard = {}, onAddNewList = {})
}

@Composable
fun BoardScreen(modifier: Modifier = Modifier, boardId: Int) {
    val vm: BoardScreenViewModel by viewModel()
    vm.setCurrentBoard(boardId)
    val data by vm.data.collectAsState()

    BoardScreenContent(
        modifier = modifier,
        data = data,
        onAddNewList = { vm.addNewList() },
        onAddNewCard = { vm.addNewCard(it) })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardScreenContent(
    modifier: Modifier = Modifier,
    data: BoardData,
    onAddNewList: () -> Unit,
    onAddNewCard: (Int) -> Unit
) {
    BankanTheme {
        LazyColumn {
            stickyHeader {
                androidx.compose.material.Card {
                    Text(text = data.info.name)
                }
            }
            item {
                LazyRow(modifier = modifier) {
                    eachAndBetween(data = data.content) {
                        List1(data = it, onAddNewCard = onAddNewCard)
                    }
                    item {
                        AddNewList(onAddNewList = onAddNewList)
                    }
                }
            }
        }
    }
}


