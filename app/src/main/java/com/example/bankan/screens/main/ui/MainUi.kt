package com.example.bankan.screens.main.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.components.SwipeableElement
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.screens.main.viewmodel.MainMenuUiModel
import com.example.bankan.screens.main.viewmodel.MainMenuUiStates
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import org.koin.androidx.compose.viewModel

@Composable
fun MainMenu(modifier: Modifier = Modifier, onBoardChosen: (boardId: Int) -> Unit) {
    val vm: MainMenuViewModel by viewModel()
    val uiModel by vm.uiModel.collectAsState()

    MainMenuContent(
        uiModel = uiModel,
        onBoardChosen = { vm.chooseCurrentBoard(it); onBoardChosen(it) },
        onCreateNewBoard = { vm.createNewBoard() },
        onChangeNewBoardName = { vm.changeNewBoardName(it) },
        onDelete = { vm.deleteBoard(it) },
        onSubmitNewBoard = { vm.submitNewBoard() },
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
    onDelete: (boardId: Int) -> Unit,
) {
    val vm: MainMenuViewModel by viewModel()
    BankanTheme {
        Column(modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { vm.discardNewBoard() })
        {
            BoardList(
                modifier = modifier,
                uiModel = uiModel,
                onBoardChosen = onBoardChosen,
                onSubmitNewBoard = onSubmitNewBoard,
                onChangeNewBoardName = onChangeNewBoardName,
                onCreateNewBoard = onCreateNewBoard,
                onDelete = onDelete,
            )
        }
    }
}

@SuppressLint("ModifierFactoryExtensionFunction")
@OptIn(ExperimentalFoundationApi::class)
private fun LazyItemScope.animatePlacement(): Modifier = Modifier.animateItemPlacement(tween(500))

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardList(
    modifier: Modifier = Modifier,
    uiModel: MainMenuUiModel,
    onBoardChosen: (boardId: Int) -> Unit,
    onDelete: (boardId: Int) -> Unit,
    onCreateNewBoard: () -> Unit,
    onChangeNewBoardName: (String) -> Unit,
    onSubmitNewBoard: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        eachAndBetween(data = uiModel.boardInfoList.withIndex().toList()) {
            BoardCard(
                modifier = animatePlacement(),
                boardInfo = it.value,
                onBoardChosen = { onBoardChosen(it.value.localId) },
                onDelete = { onDelete(it.value.localId) })
        }
        item {
            Spacer(
                modifier = animatePlacement()
                    .height(10.dp)
                    .fillMaxWidth()
            )
        }
        item {
            CreateNewBoard(
                modifier = animatePlacement(),
                uiModel = uiModel,
                onCreateNewBoard = onCreateNewBoard,
                onChangeNewBoardName = onChangeNewBoardName,
                onSubmitNewBoard = onSubmitNewBoard
            )
        }
        item {
            Spacer(
                modifier = animatePlacement()
                    .height(50.dp)
                    .fillMaxWidth()
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
    SwipeableElement(modifier = modifier, onSwipe = onDelete) {
        Card(modifier = it, onClick = onBoardChosen) {
            Text(text = boardInfo.name)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun CreateNewBoard(
    modifier: Modifier = Modifier,
    uiModel: MainMenuUiModel,
    onCreateNewBoard: () -> Unit,
    onChangeNewBoardName: (String) -> Unit,
    onSubmitNewBoard: () -> Unit
) {
    CreateNewButton(
        modifier = modifier,
        isEntering = uiModel.state == MainMenuUiStates.EnteringName,
        name = uiModel.newBoardName,
        onCreateNew = onCreateNewBoard,
        onNameChanged = onChangeNewBoardName,
        onSubmit = onSubmitNewBoard
    )
}

