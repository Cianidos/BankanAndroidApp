package com.example.bankan.common.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun CreateNewButton(
    modifier: Modifier = Modifier,
    isEntering: Boolean,
    name: String,
    onCreateNew: () -> Unit,
    onNameChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val tfFr = remember { FocusRequester() }
    DashOutline(modifier = modifier) {
        AnimatedContent(
            targetState = isEntering,
            contentAlignment = Alignment.Center,
            transitionSpec = { fadeIn(tween(500)) with fadeOut(tween(500)) })
        { targetState ->
            if (targetState) TextField(
                modifier = Modifier
                    .focusRequester(tfFr)
                    .onPlaced { tfFr.requestFocus() },
                value = name,
                onValueChange = { onNameChanged(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { onSubmit() })
            )
            else IconButton(modifier = Modifier, onClick = { onCreateNew() }) {
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
        }
    }
}
