package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.example.bankan.screens.board.ui.CardExample
import com.example.bankan.screens.board.ui.ListPreview
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.autheneication.ui.Authentication
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
        composable(Authentication.name) {
            Authentication(onAppEnter = {
                navController.navigate(route = Boards.name)
            })
        }
        composable(Boards.name) {
            BoardListPreview()
        }
        composable(Settings.name) {
            Screen()
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
