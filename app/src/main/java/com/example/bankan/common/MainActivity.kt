package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeveloperBoard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankan.common.BankanScreen.*
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.autheneication.ui.Authentication
import com.example.bankan.screens.board.ui.BoardScreenContentPreview
import com.example.bankan.screens.board.ui.CardExample
import com.example.bankan.screens.board.ui.ListPreview
import com.example.bankan.screens.main.ui.BoardListPreview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankanApp()
        }
    }
}

// TODO clean project structure
// TODO make main screens templates
// TODO set animations on navigation


@Composable
fun BankanApp() {
    BankanTheme {
        val navController = rememberNavController()
        val backstackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = BankanScreen.fromRoute(backstackEntry?.destination?.route)

        BankanNavHost(navController, currentScreen)
    }
}

@Composable
fun ContentWithBottomNavBar(
    modifier: Modifier = Modifier,
    onBoardsListClicked: () -> Unit,
    onBoardClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    centerContent: @Composable () -> Unit
) {
    Scaffold(modifier = modifier, bottomBar = {
        BottomAppBar {
            IconButton(onClick = onBoardsListClicked) {
                Icon(Icons.Outlined.SpaceDashboard, contentDescription = "List of Boards")
            }
            IconButton(onClick = onBoardClicked) {
                Icon(Icons.Outlined.DeveloperBoard, contentDescription = "Board")
            }
            IconButton(onClick = onSettingsClicked) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
            }
        }
    }) {
        centerContent()
    }
}

@Composable
fun BankanNavHost(
    navController: NavHostController,
    startDestination: BankanScreen,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.name,
        modifier = modifier
    ) {
        @Composable
        fun ContentWithBottomNavBar_(
            modifier: Modifier = Modifier,
            content: @Composable () -> Unit
        ) {
            ContentWithBottomNavBar(
                onBoardsListClicked = { navController.navigate(route = BoardsList.name) },
                onBoardClicked = { navController.navigate(route = Board.name) },
                onSettingsClicked = { navController.navigate(route = Settings.name) },
                centerContent = content
            )
        }
        composable(Authentication.name) {
            Authentication(onAppEnter = {
                navController.navigate(route = BoardsList.name)
            })
        }

        composable(Board.name) {
            ContentWithBottomNavBar_ {
                BoardScreenContentPreview()
            }
        }

        composable(BoardsList.name) {
            ContentWithBottomNavBar_ {
                BoardListPreview()
            }
        }

        composable(Settings.name) {
            ContentWithBottomNavBar_ {
                Screen()
            }
        }
    }
}


@Preview
@Composable
private fun Screen() {
    BankanTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                CardExample()
                ListPreview()
            }
        }
    }
}
