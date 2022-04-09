package com.example.bankan.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bankan.NavGraphs
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.repository.ProfileRepository
import com.example.bankan.destinations.*
import com.example.bankan.navDestination
import com.example.bankan.screens.board.ui.BoardScreen
import com.example.bankan.screens.main.ui.MainMenu
import com.example.bankan.screens.main.ui.MultitouchExamplesPreview
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.spec.Direction
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel


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

// TODO finish MainMenu
// TODO make board screen usable
// TODO make room/delight database
// TODO make repositories (maybe fake firstly)
// TODO choose dependencies strategy hoisting vs inject viewModel everywhere


@OptIn(ExperimentalAnimationApi::class,
    com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi::class
)
@Composable
fun BankanApp() {
    BankanTheme {
        val vm: NavigationViewModel by viewModel()
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

@Destination
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    BankanTheme {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalAnimationApi::class)
object BoardListAnimationStyle : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? =
        slideIntoContainer(
            towards = AnimatedContentScope.SlideDirection.Right,
            animationSpec = tween(700)
        )

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? =
        slideOutOfContainer(
            towards = AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
}


@Destination(style = BoardListAnimationStyle::class)
@Composable
fun BoardListScreenWithNavBar(modifier: Modifier = Modifier, nav: NavController) {
    val profileRepository: ProfileRepository by inject()
    ContentWithBottomNavBar(nav = nav) {
        MainMenu(it) { boardId -> nav.navigateTo(BoardScreenWithNavBarDestination) }
    }
}

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
) {
    ContentWithBottomNavBar(nav = nav) { BoardScreen(it) }
}

@OptIn(ExperimentalAnimationApi::class)
object SettingsAnimationStyle : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? =
        slideIntoContainer(
            towards = AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(700)
        )

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? =
        slideOutOfContainer(
            towards = AnimatedContentScope.SlideDirection.Right,
            animationSpec = tween(700)
        )
}

@Destination(style = SettingsAnimationStyle::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier, nav: NavController) {
    ContentWithBottomNavBar(nav = nav) { Screen(it) }
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
            MultitouchExamplesPreview()
        }
    }
}

@Composable
fun ContentWithBottomNavBar(
    modifier: Modifier = Modifier,
    nav: NavController,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    Scaffold(modifier = modifier,
        bottomBar = { BottomBar(navController = nav) }) {
        content(modifier = Modifier.padding(it))
    }
}

enum class BottomBarDestination(
    val direction: Direction,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    BoardList(
        BoardListScreenWithNavBarDestination,
        Icons.Outlined.SpaceDashboard,
        com.example.bankan.R.string.list_of_boards
    ),
    Board(
        BoardScreenWithNavBarDestination,
        Icons.Outlined.SpaceDashboard,
        com.example.bankan.R.string.board
    ),

    Settings(
        SettingsScreenDestination,
        Icons.Outlined.Settings,
        com.example.bankan.R.string.settings
    ),
}

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.navDestination

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            val stringRes = stringResource(id = destination.label)
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigateTo(destination.direction) { launchSingleTop = true }
                    //Log.d("AAAAAAAAA", "${currentDestination}, ${destination.direction} \n ${currentDestination?.route} ${destination.direction.route}")
                },
                icon = { Icon(destination.icon, contentDescription = stringRes) },
                label = { Text(stringRes) },
            )
        }
    }
}