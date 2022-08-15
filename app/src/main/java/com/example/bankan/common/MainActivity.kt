package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.NavGraphs
import com.example.bankan.screens.destinations.AuthenticationDestination
import com.example.bankan.screens.destinations.BoardListScreenWithNavBarDestination
import com.example.bankan.screens.destinations.LoadingScreenDestination
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankanApp()
        }
    }
}


@OptIn(
    ExperimentalAnimationApi::class,
    com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi::class
)
@Composable
fun BankanApp() {
    val vm: NavigationViewModel = getViewModel()
    BankanTheme {
        CompositionLocalProvider(LocalContentColor provides Color.Black) {
            val state: MainState by vm.state.collectAsState()
            val navController = rememberAnimatedNavController()
            val engine = rememberAnimatedNavHostEngine()
            val currentScreen = when (state) {
                MainState.Authorized.Guest -> BoardListScreenWithNavBarDestination
                MainState.Authorized.LoggedIn -> BoardListScreenWithNavBarDestination
                MainState.Loading -> LoadingScreenDestination
                MainState.NotAuthorized -> AuthenticationDestination
            }
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                startRoute = currentScreen,
                navController = navController,
                engine = engine
            )
        }
    }
}

