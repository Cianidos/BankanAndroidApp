package com.example.bankan.screens

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bankan.common.ui.theme.BankanTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    BankanTheme {
        CircularProgressIndicator()
    }
}
