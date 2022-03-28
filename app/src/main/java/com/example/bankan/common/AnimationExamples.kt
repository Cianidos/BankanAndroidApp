package com.example.bankan.common

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

fun Context.showSmallLengthToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

@ExperimentalAnimationApi
@Composable
fun ExperimentalAnimationNav() {
    val navController = rememberAnimatedNavController()

    val backstackEntry = navController.currentBackStackEntry

    AnimatedNavHost(navController, startDestination = backstackEntry?.destination?.route ?: "Blue") {
        composable("Blue",
            enterTransition = {
//                when (initialState.destination.route) {
//                    "Red" -> {
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
//                    }
//                    else -> null
//                }
            },
            exitTransition = {
//                when (targetState.destination.route) {
//                    "Red" -> {
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
//                    }
//                    else -> null
//                }
            },
//            popEnterTransition = {
//                when (initialState.destination.route) {
//                    "Red" -> {
//                        ctx.showSmallLengthToast("Blue, popEnterTr, Red")
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    }
//                    else -> null
//                }
//            },
//            popExitTransition = {
//                when (targetState.destination.route) {
//                    "Red" -> {
//                        ctx.showSmallLengthToast("Blue, popExitTr, Red")
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    }
//                    else -> null
//                }
//            }
        ) { BlueScreen(navController) }


        composable("Red",
            enterTransition = {
//                when (initialState.destination.route) {
//                    "Blue" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
//                    "Green" ->
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Up,
//                            animationSpec = tween(700)
//                        )
//                    else -> null
//                }
            },
            exitTransition = {
//                when (targetState.destination.route) {
//                    "Blue" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
//                    "Green" ->
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Up,
//                            animationSpec = tween(700)
//                        )
//                    else -> null
//                }
            },
//            popEnterTransition = {
//                when (initialState.destination.route) {
//                    "Blue" ->
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    "Green" ->
//                        slideIntoContainer(
//                            AnimatedContentScope.SlideDirection.Down,
//                            animationSpec = tween(700)
//                        )
//                    else -> null
//                }
//            },
//            popExitTransition = {
//                when (targetState.destination.route) {
//                    "Blue" ->
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Right,
//                            animationSpec = tween(700)
//                        )
//                    "Green" ->
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Down,
//                            animationSpec = tween(700)
//                        )
//                    else -> null
//                }
//            }
        ) { RedScreen(navController) }


        navigation(
            startDestination = "Green",
            route = "Inner",
            enterTransition = { expandIn(animationSpec = tween(700)) },
            exitTransition = { shrinkOut(animationSpec = tween(700)) }
        ) {
            composable(
                "Green",
                enterTransition = {
                    when (initialState.destination.route) {
                        "Red" ->
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700)
                            )
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "Red" ->
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700)
                            )
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "Red" ->
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700)
                            )
                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "Red" ->
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700)
                            )
                        else -> null
                    }
                }
            ) { GreenScreen(navController) }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.BlueScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

        Spacer(Modifier.height(Dp(25f)))

        NavigateButton(
            "Navigate Horizontal",
            Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
        ) { navController.navigate("Red") }

        Spacer(Modifier.height(Dp(25f)))

        NavigateButton(
            "Navigate Expand",
            Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
        ) { navController.navigate("Inner") }
        Text(
            "Blue",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateEnterExit(
                    enter = fadeIn(animationSpec = tween(250, delayMillis = 450)),
                    exit = ExitTransition.None
                ),
            color = Color.White, fontSize = 80.sp, textAlign = TextAlign.Center
        )
        NavigateBackButton(navController)
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.RedScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Spacer(Modifier.height(Dp(25f)))
        NavigateButton(
            "Navigate Horizontal",
            Modifier
                .wrapContentWidth()
                .then(Modifier.align(Alignment.CenterHorizontally))
        ) { navController.navigate("Blue") }
        Spacer(Modifier.height(Dp(25f)))
        NavigateButton(
            "Navigate Vertical",
            Modifier
                .wrapContentWidth()
                .then(Modifier.align(Alignment.CenterHorizontally))
        ) { navController.navigate("Green") }
        Text(
            "Red",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateEnterExit(
                    enter = fadeIn(animationSpec = tween(250, delayMillis = 450)),
                    exit = ExitTransition.None
                ),
            color = Color.White, fontSize = 80.sp, textAlign = TextAlign.Center
        )
        NavigateBackButton(navController)
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.GreenScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Spacer(Modifier.height(Dp(25f)))
        NavigateButton(
            "Navigate to Red",
            Modifier
                .wrapContentWidth()
                .then(Modifier.align(Alignment.CenterHorizontally))
        ) { navController.navigate("Red") }
        Text(
            "Green",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateEnterExit(
                    enter = fadeIn(animationSpec = tween(250, delayMillis = 450)),
                    exit = ExitTransition.None
                ),
            color = Color.White, fontSize = 80.sp, textAlign = TextAlign.Center
        )
        NavigateBackButton(navController)
    }
}

@Composable
fun NavigateButton(
    text: String,
    modifier: Modifier = Modifier,
    listener: () -> Unit = { }
) {
    Button(
        onClick = listener,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun NavigateBackButton(navController: NavController) {
    // Use LocalLifecycleOwner.current as a proxy for the NavBackStackEntry
    // associated with this Composable
    if (navController.currentBackStackEntry == LocalLifecycleOwner.current &&
        navController.previousBackStackEntry != null
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Previous screen")
        }
    }
}