package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeveloperBoard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.bankan.common.BankanScreen.*
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.autheneication.ui.Authentication
import com.example.bankan.screens.board.ui.BoardScreenContentPreview
import com.example.bankan.screens.board.ui.CardExample
import com.example.bankan.screens.board.ui.ListPreview
import com.example.bankan.screens.main.ui.MainMenu
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankanTheme {
                BankanApp()
//                ExperimentalAnimationNav()
            }
        }
    }
}

// TODO make data layer, extract data, repositories and etc to separated module
// TODO finish MainMenu
// TODO make board screen usable
// TODO make room/delight database
// TODO make repositories (maybe fake firstly)
// TODO choose dependencies strategy hoisting vs inject viewModel everywhere


@Composable
fun ContentWithBottomNavBarImpl(
    modifier: Modifier = Modifier,
    onBoardsListClicked: () -> Unit,
    onBoardClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    centerContent: @Composable (modifier: Modifier) -> Unit
) {
    Scaffold(modifier = modifier,
        bottomBar = {
            BottomAppBar {
                IconButton(modifier = Modifier.weight(1f), onClick = onBoardsListClicked) {
                    Icon(Icons.Outlined.SpaceDashboard, contentDescription = "List of Boards")
                }
                IconButton(modifier = Modifier.weight(1f), onClick = onBoardClicked) {
                    Icon(Icons.Outlined.DeveloperBoard, contentDescription = "Board")
                }
                IconButton(modifier = Modifier.weight(1f), onClick = onSettingsClicked) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                }
            }
        }) { centerContent(modifier = Modifier.padding(it)) }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BankanApp() {
    BankanTheme {
        val navController = rememberAnimatedNavController()
        val backstackEntry = navController.currentBackStackEntry
        val currentScreen = BankanScreen.fromRoute(backstackEntry?.destination?.route)

        NavHostContainer(navController = navController, currentScreen = currentScreen)
        val d = 1 + 1
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavHostContainer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentScreen: BankanScreen,
) {
    @Composable
    fun ContentWithBottomNavBar(
        modifier: Modifier = Modifier,
        content: @Composable (Modifier) -> Unit
    ) {
        ContentWithBottomNavBarImpl(
            onBoardsListClicked = { navController.navigate(route = BoardsList.name) },
            onBoardClicked = { navController.navigate(route = Board.name) },
            onSettingsClicked = { navController.navigate(route = Settings.name) },
            centerContent = content
        )
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = currentScreen.name,
    ) {
        composable(Authentication.name) {
            Authentication(modifier) {
                navController.navigate(route = "Main")
            }
        }

        navigation(BoardsList.name, "Main") {
            composable(BoardsList.name,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) { ContentWithBottomNavBar { MainMenu(it) {} } }

            composable(
                Board.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        BoardsList.name ->
                            slideIntoContainer(
                                towards = AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        Settings.name ->
                            slideIntoContainer(
                                towards = AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        else -> null
                    }

                },
                exitTransition = {
                    when (targetState.destination.route) {
                        BoardsList.name ->
                            slideOutOfContainer(
                                towards = AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        Settings.name ->
                            slideOutOfContainer(
                                towards = AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        else -> null
                    }
                }
            ) { ContentWithBottomNavBar { BoardScreenContentPreview(it) } }

            composable(
                Settings.name,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                }
            ) { ContentWithBottomNavBar { Screen(it) } }
        }
    }
}


@Preview
@Composable
private fun Screen(modifier: Modifier = Modifier) {
    BankanTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                CardExample()
                ListPreview()
            }
        }
    }
}
