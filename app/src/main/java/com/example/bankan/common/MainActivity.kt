package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bankan.common.BankanScreen.*
import com.example.bankan.common.ui.components.CardExample
import com.example.bankan.common.ui.components.ListPreview
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

        RallyNavHost(navController, currentScreen)
    }
}

@Composable
fun RallyNavHost(
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

enum class BankanScreen {
    Authentication, Boards, Settings;

    companion object {
        private val association = values().associateBy { it.name }

        fun fromRoute(route: String?): BankanScreen =
            route?.substringBefore("/")?.let {
                association[it] ?: throw IllegalArgumentException("Route $route is not recognized.")
            } ?: Authentication
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


@Preview
@Composable
fun IconsTest() {
    BankanTheme {
        Column(
            modifier = Modifier
                .size(200.dp)
                .background(color = Color.Gray)
        ) {
            Icon(Icons.Default.Edit, "edit")
        }
    }
}

