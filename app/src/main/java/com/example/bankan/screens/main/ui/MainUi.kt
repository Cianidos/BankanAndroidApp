package com.example.bankan.screens.main.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.bankan.common.ui.components.ContentWithBottomNavBar
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.components.SwipeableElement
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.screens.destinations.BoardScreenWithNavBarDestination
import com.example.bankan.screens.main.viewmodel.MainMenuUiModel
import com.example.bankan.screens.main.viewmodel.MainMenuUiStates
import com.example.bankan.screens.main.viewmodel.MainMenuViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.androidx.compose.viewModel

@OptIn(ExperimentalAnimationApi::class)
object BoardListAnimationStyle : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition =
        slideIntoContainer(
            towards = AnimatedContentScope.SlideDirection.Right,
            animationSpec = tween(700)
        )

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition =
        slideOutOfContainer(
            towards = AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
}


@Destination(style = BoardListAnimationStyle::class)
@Composable
fun BoardListScreenWithNavBar(modifier: Modifier = Modifier, nav: NavController) {
//    val profileRepository: ProfileRepository by inject()
    ContentWithBottomNavBar(nav = nav) {
        MainMenu(it) { boardId -> nav.navigate(BoardScreenWithNavBarDestination) }
    }
}

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
    LazyColumn(modifier = modifier.padding(5.dp)) {
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
                    .height(5.dp)
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
        Card(
            onClick = onBoardChosen,
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
            ) {
                Text(text = boardInfo.name, modifier = Modifier.shadow(0.dp))
            }
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

