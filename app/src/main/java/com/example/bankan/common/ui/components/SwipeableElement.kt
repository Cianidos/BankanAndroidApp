package com.example.bankan.common.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableElement(
    modifier: Modifier = Modifier,
    onSwipe: () -> Unit,
    content: @Composable () -> Unit
) {
    val callbackState by rememberUpdatedState(newValue = onSwipe)
    val state = rememberDismissState {
        when (it) {
            DismissValue.Default -> Unit
            DismissValue.DismissedToEnd -> callbackState()
            DismissValue.DismissedToStart -> Unit
        }
        false
    }
    SwipeToDismiss(
        state = state,
        modifier = modifier,
        directions = setOf(DismissDirection.StartToEnd),
        dismissThresholds = { FractionalThreshold(0.3f) },
        background = {},
        dismissContent = { content() })

}