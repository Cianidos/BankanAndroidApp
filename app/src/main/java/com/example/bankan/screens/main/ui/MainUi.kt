package com.example.bankan.screens.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.board.data.BoardInfo
import com.example.bankan.screens.main.viewmodel.MainMenuUiModel
import com.example.bankan.screens.main.viewmodel.MainMenuUiStates
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import org.koin.androidx.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun MainMenu(modifier: Modifier = Modifier, onBoardChosen: (boardId: Int) -> Unit) {
    val vm: MainMenuViewModel by viewModel()
    val uiModel by vm.uiModel.collectAsState()
    MainMenuContent(
        onBoardChosen = onBoardChosen,
        uiModel = uiModel,
        onCreateNewBoard = { vm.createNewBoard() },
        onChangeNewBoardName = { vm.changeNewBoardName(it) },
        onSubmitNewBoard = { vm.submitNewBoard() }
    )
}

@Composable
fun MainMenuContent(
    modifier: Modifier = Modifier,
    uiModel: MainMenuUiModel,
    onBoardChosen: (boardId: Int) -> Unit,
    onCreateNewBoard: () -> Unit,
    onChangeNewBoardName: (String) -> Unit,
    onSubmitNewBoard: () -> Unit,
) {
    val vm: MainMenuViewModel by viewModel()
    BankanTheme {
        Column(modifier = modifier
            .fillMaxSize()
            .clickable { vm.discardNewBoard() }) {
            BoardList(
                modifier = modifier,
                uiModel = uiModel,
                onBoardChosen = onBoardChosen,
                onSubmitNewBoard = onSubmitNewBoard,
                onChangeNewBoardName = onChangeNewBoardName,
                onCreateNewBoard = onCreateNewBoard
            )
        }
    }
}

@Composable
fun BoardList(
    modifier: Modifier = Modifier,
    uiModel: MainMenuUiModel,
    onBoardChosen: (boardId: Int) -> Unit,
    onCreateNewBoard: () -> Unit,
    onChangeNewBoardName: (String) -> Unit,
    onSubmitNewBoard: () -> Unit,
) {
    val vm: MainMenuViewModel by viewModel()

    LazyColumn(modifier = modifier) {
        eachAndBetween(data = uiModel.boardInfoList.withIndex().toList()) {
            BoardCard(
                boardInfo = it.value,
                onBoardChosen = { onBoardChosen(it.index) },
                onDelete = { vm.deleteBoard(it.index) })
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
            )
        }
        item {
            CreateNewBoard(
                uiModel = uiModel,
                onCreateNewBoard = onCreateNewBoard,
                onChangeNewBoardName = onChangeNewBoardName,
                onSubmitNewBoard = onSubmitNewBoard
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoardCard(
    modifier: Modifier = Modifier,
    boardInfo: BoardInfo,
    onDelete: () -> Unit,
    onBoardChosen: () -> Unit
) {
    var contentSize: Size by remember { mutableStateOf(Size(48f, 48f)) }

    val swipeableState = rememberSwipeableState(0) {
        if (it == 1) {
            onDelete()
            false
        } else true
    }

    val anchors = mapOf(0f to 0, contentSize.width to 1) // Maps anchor points (in px) to states

    Box(
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Transparent)
    ) {
        Card(modifier = modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .onGloballyPositioned {
                contentSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
            }, onClick = onBoardChosen
        ) {
            Text(text = boardInfo.name)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateNewBoard(
    modifier: Modifier = Modifier,
    uiModel: MainMenuUiModel,
    onCreateNewBoard: () -> Unit,
    onChangeNewBoardName: (String) -> Unit,
    onSubmitNewBoard: () -> Unit
) {
    val tfFr = remember { FocusRequester() }

    DashOutline(modifier = modifier) {
        Column {
            AnimatedVisibility(visible = uiModel.state == MainMenuUiStates.View) {
                IconButton(onClick = { onCreateNewBoard() }) {
                    Icon(Icons.Outlined.Add, contentDescription = "Create New Board")
                }
            }
            AnimatedVisibility(visible = uiModel.state == MainMenuUiStates.EnteringName) {
                TextField(
                    modifier = Modifier
                        .focusRequester(tfFr)
                        .onPlaced { tfFr.requestFocus() },
                    value = uiModel.newBoardName,
                    onValueChange = { onChangeNewBoardName(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSubmitNewBoard() })
                )
            }
        }
    }
}