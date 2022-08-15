package com.example.bankan.screens.settings

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.bankan.common.ui.components.ContentWithBottomNavBar
import com.example.bankan.data.repository.ProfileRepository
import com.example.bankan.screens.destinations.AuthenticationDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
    ContentWithBottomNavBar(modifier = modifier, nav = nav) { SettingsScreenContent(it, nav) }
}

class SettingsViewModel : KoinComponent, ViewModel() {
    private val profileRepository: ProfileRepository by inject()
    val profileInfo = profileRepository.run {
        combine(userName, isGuest, isAuthorized, userId, ::ProfileInfo)
    }

    data class ProfileInfo(
        val nickname: String = "",
        val isGuest: Boolean = false,
        val isAuthorized: Boolean = false,
        val userId: Int = -1
    )

    fun logout(nav: NavController) {
        viewModelScope.launch {
            profileRepository.logout()
        }
        nav.navigate(AuthenticationDestination)
    }
}

@Composable
private fun SettingsScreenContent(modifier: Modifier = Modifier, nav: NavController) {
    val vm: SettingsViewModel = getViewModel()
    val profile by vm.profileInfo.collectAsState(initial = SettingsViewModel.ProfileInfo())
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = profile.nickname)
            Text(text = "Authorized: ${profile.isAuthorized}")
            Text(text = "Guest: ${profile.isGuest}")
            OutlinedButton(onClick = { vm.logout(nav) }) {
                Text(text = "Logout")
            }
        }
    }
}
