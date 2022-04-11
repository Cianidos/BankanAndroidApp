package com.example.bankan.screens.board.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.BoardData
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.ListData
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import org.koin.androidx.compose.viewModel


@Composable
fun BoardScreen(modifier: Modifier = Modifier) {
    val vm: BoardScreenViewModel by viewModel()
    val boardInfo by vm.boardInfo().collectAsState(initial = BoardInfo(""))
    val listInfo by vm.listData().collectAsState(initial = emptyList())


    val cardInfo: List<ListData> = listInfo.map {
        val cardList by it.second.collectAsState(initial = emptyList())
        ListData(it.first, cardList)
    }

    val data = BoardData(boardInfo, cardInfo)

    BoardScreenContent(
        modifier = modifier,
        data = data,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardScreenContent(
    modifier: Modifier = Modifier,
    data: BoardData,
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
                        List1(data = it)
                    }
                    item {
                        AddNewList()
                    }
                }
            }
        }
    }
}


