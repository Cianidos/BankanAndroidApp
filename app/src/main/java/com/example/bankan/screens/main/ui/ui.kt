package com.example.bankan.screens.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.DragExample
import com.example.bankan.common.ui.SwipeableSample
import com.example.bankan.common.ui.TransformableSample
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.board.data.BoardInfo
import com.example.bankan.screens.main.viewmodel.AnimationViewModel
import com.example.bankan.screens.main.viewmodel.MainMenuStates
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import org.koin.androidx.compose.viewModel
import kotlin.math.roundToInt


@Composable
fun MainMenu(modifier: Modifier = Modifier, onBoardChosen: (boardId: Int) -> Unit) {
    val vm: MainMenuViewModel by viewModel()
    val boardInfoList by vm.boardInfoList.collectAsState()
    MainMenuContent(
        onBoardChosen = onBoardChosen,
        boardList = boardInfoList
    )
}

@Composable
fun MainMenuContent(
    modifier: Modifier = Modifier,
    onBoardChosen: (boardId: Int) -> Unit,
    boardList: List<BoardInfo>
) {
    BankanTheme {
        BoardList(
            modifier = modifier,
            list = boardList,
            onBoardChosen = onBoardChosen
        )
    }
}


@Preview
@Composable
fun MultitouchExamplesPreview(modifier: Modifier = Modifier) {
    val viewModel by viewModel<AnimationViewModel>()
    val list by viewModel.list.collectAsState()

    Column(modifier = modifier) {
        SwipeableBoardList(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            list = list,
            onDelete = { viewModel.deleteAtIndex(it) }
        )
        Box(Modifier.weight(1f)) {
            SwipeableSample()
        }
        TransformableSample(Modifier.weight(1f))
        DragExample(Modifier.weight(1f))
    }
}

@Composable
fun SwipeableBoardList(
    modifier: Modifier = Modifier,
    list: List<String>,
    onDelete: (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        eachAndBetween(data = list.withIndex()) {
            SwipeableBoardCard(text = it.value, onDelete = { onDelete(it.index) })
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableBoardCard(modifier: Modifier = Modifier, text: String, onDelete: () -> Unit) {
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
            .background(Color.Green)
    ) {
        AnimatedDashedOutlineBoardCard(modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .background(Color.DarkGray)
            .onGloballyPositioned {
                contentSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
            }, boardName = text
        )
    }
}


@Composable
fun BoardList(
    modifier: Modifier = Modifier,
    list: List<BoardInfo>,
    onBoardChosen: (boardId: Int) -> Unit,
) {
    val vm: MainMenuViewModel by viewModel()

    LazyColumn(modifier = modifier) {
        eachAndBetween(data = list.withIndex().toList()) {
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
            CreateNewBoard()
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
        Card(modifier = modifier.onGloballyPositioned {
            contentSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
        }, onClick = onBoardChosen) {
            Text(text = boardInfo.name)
        }
    }
}

@Composable
fun CreateNewBoard(modifier: Modifier = Modifier) {
    val vm: MainMenuViewModel by viewModel()
    val state by vm.state.collectAsState()
    val newBoardName by vm.newBoardName.collectAsState()

    val tfFr = remember { FocusRequester() }

    DashOutline(modifier = modifier) {
        when (state) {
            MainMenuStates.View -> {
                IconButton(onClick = { vm.createNewBoard() }) {
                    Icon(Icons.Outlined.Add, contentDescription = "Create New Board")
                }
            }
            MainMenuStates.Loading -> {}
            MainMenuStates.EnteringName -> {
                TextField(
                    modifier = Modifier.focusRequester(tfFr),
                    value = newBoardName,
                    onValueChange = { vm.changeNewBoardName(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { vm.submitNewBoard() })
                )
                tfFr.requestFocus()
            }
        }
    }
}


@Composable
fun AnimatedDashedOutlineCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val vm by viewModel<AnimationViewModel>()
    val time by vm.times.collectAsState()

    fun Int.breakOn(pointOfBreak: Int): Int {
        return if (this <= pointOfBreak) this else pointOfBreak * 2 - this
    }

    val intervals = floatArrayOf(
        time.mod(101).plus(20).breakOn(70).toFloat(),
        70.minus(time.mod(101).plus(20).breakOn(70)).toFloat(),
    )
    BankanTheme {
        DashOutline(
            modifier = modifier,
            strokeColor = Color.Cyan,
            intervals = intervals,
            phase = -time.toFloat()
        ) {
            content()
        }
    }
}

@Composable
fun AnimatedDashedOutlineBoardCard(modifier: Modifier = Modifier, boardName: String) {
    AnimatedDashedOutlineCard(modifier = modifier) {
        Text(modifier = Modifier.padding(10.dp), text = boardName)
    }
}
