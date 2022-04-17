package com.example.bankan.screens.settings

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.bankan.common.ui.components.ContentWithBottomNavBar
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.main.ui.MultitouchExamplesPreview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object SettingsAnimationStyle : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition =
        slideIntoContainer(
            towards = AnimatedContentScope.SlideDirection.Left,
            animationSpec = tween(700)
        )

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition =
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
