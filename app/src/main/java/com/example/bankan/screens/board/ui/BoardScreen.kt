package com.example.bankan.screens.board.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.bankan.common.ui.components.ContentWithBottomNavBar
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.BoardData
import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.models.ListData
import com.example.bankan.destinations.BoardListScreenWithNavBarDestination
import com.example.bankan.destinations.CardEditorScreenDestination
import com.example.bankan.destinations.SettingsScreenDestination
import com.example.bankan.navDestination
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.koin.androidx.compose.viewModel


@OptIn(ExperimentalAnimationApi::class)
object BoardAnimationStyle : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? =
        when (initialState.navDestination) {
            BoardListScreenWithNavBarDestination ->
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            SettingsScreenDestination ->
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            else -> null
        }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? =
        when (targetState.navDestination) {
            BoardListScreenWithNavBarDestination ->
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            SettingsScreenDestination ->
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            else -> null
        }
}

@Destination(style = BoardAnimationStyle::class)
@Composable
fun BoardScreenWithNavBar(
    modifier: Modifier = Modifier,
    nav: NavController,
    navD: DestinationsNavigator,
    r: ResultRecipient<CardEditorScreenDestination, String>
) {
    ContentWithBottomNavBar(nav = nav) { BoardScreen(it, nav = navD, r = r) }
}

@Composable
fun BoardScreen(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator,
    r: ResultRecipient<CardEditorScreenDestination, String>
) {
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
        data = data, nav = nav, r = r
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardScreenContent(
    modifier: Modifier = Modifier,
    data: BoardData,
    nav: DestinationsNavigator,
    r: ResultRecipient<CardEditorScreenDestination, String>,
) {
    val vm: BoardScreenViewModel by viewModel()
    BankanTheme {
        Surface(modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { vm.discardEnterings() }
        ) {
            LazyColumn {
                stickyHeader {
                    androidx.compose.material.Card {
                        Text(text = data.info.name)
                    }
                }
                item {
                    LazyRow(modifier = modifier) {
                        eachAndBetween(data = data.content) {
                            List1(data = it, nav = nav,r=r)
                        }
                        item {
                            AddNewList()
                        }
                    }
                }
            }
        }
    }
}


