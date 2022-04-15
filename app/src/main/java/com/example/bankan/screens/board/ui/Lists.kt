package com.example.bankan.screens.board.ui

import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.EachAndBetween
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
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = data.info.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .background(Color.Gray, RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            )
            EachAndBetween(data = data.content) { card ->
                SwipeableElement(onSwipe = { vm.deleteCard(card.localId) }) {
                    val anim = remember {
                        TargetBasedAnimation(
                            animationSpec = tween(500),
                            typeConverter = Float.VectorConverter,
                            initialValue = 0f,
                            targetValue = 1f
                        )
                    }
                    var playTime by remember { mutableStateOf(0L) }
                    var factor by remember { mutableStateOf(0f) }

                    LaunchedEffect(anim) {
                        val startTime = withFrameNanos { it }
                        do {
                            playTime = withFrameNanos { it } - startTime
                            factor = anim.getValueFromNanos(playTime)
                        } while (factor < 1f)
                    }

                    val modifier = Modifier.scale(scaleY = factor, scaleX = 1f)

                    if (card.name.isNotEmpty())
                        Card(modifier = modifier, name = card.name, description = card.description)
                    else
                        NameLessCard(modifier = modifier, card.description)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            AddNewCard(listInfo = data.info)
            Spacer(modifier = Modifier.height(10.dp))
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

