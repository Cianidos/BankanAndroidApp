package com.example.bankan.screens.board.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.components.SwipeableElement
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.ListData
import com.example.bankan.data.models.ListInfo
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import org.koin.androidx.compose.viewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun List1(data: ListData) {
    val vm: BoardScreenViewModel by viewModel()
    BankanTheme {
        LazyColumn(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    text = data.info.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                )
            }
            items(data.content) { card ->
                Spacer(modifier = Modifier.height(10.dp))
                SwipeableElement(onSwipe = { vm.deleteCard(card.localId) }) {
                    val modifier = it.animateItemPlacement(tween(500))
                    if (card.name.isNotEmpty())
                        Card(modifier = modifier, name = card.name, description = card.description)
                    else
                        NameLessCard(modifier = modifier, card.description)
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AddNewCard(listInfo = data.info)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun AddNewList(modifier: Modifier = Modifier) {
    val vm: BoardScreenViewModel by viewModel()
    val boardInfo by vm.boardInfo().collectAsState(initial = BoardInfo(""))
    val isEntering by vm.isEnteringNewListName.collectAsState()
    val newListName by vm.newListName.collectAsState()

    BankanTheme {
        CreateNewButton(
            isEntering = isEntering,
            name = newListName,
            onCreateNew = { vm.isEnteringNewListName.value = true },
            onNameChanged = { vm.newListName.value = it },
            onSubmit = {
                vm.addNewList(
                    ListInfo(name = newListName, boardId = boardInfo.localId)
                )
                vm.newListName.value = ""
                vm.isEnteringNewListName.value = false
            }
        )
    }
}

