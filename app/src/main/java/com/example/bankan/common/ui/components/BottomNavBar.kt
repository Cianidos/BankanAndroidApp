package com.example.bankan.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SpaceDashboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bankan.screens.appDestination
import com.example.bankan.screens.destinations.BoardListScreenWithNavBarDestination
import com.example.bankan.screens.destinations.BoardScreenWithNavBarDestination
import com.example.bankan.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.Direction

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
    val currentDestination = navController.currentBackStackEntryAsState().value?.appDestination()

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            val stringRes = stringResource(id = destination.label)
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction, fun NavOptionsBuilder.() {
                        launchSingleTop = true
                    })
                },
                icon = { Icon(destination.icon, contentDescription = stringRes) },
                label = { Text(stringRes) },
            )
        }
    }
}
