package com.example.bankan.common.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    DashOutline(modifier = modifier) {
        AnimatedContent(
            targetState = isEntering,
            contentAlignment = Alignment.Center,
            transitionSpec = { fadeIn(tween(500)) with fadeOut(tween(500)) })
        { targetState ->
            if (targetState) TextField(
                modifier = Modifier
                    .scrollToOnFocus()
                    .focusOnEntry(),
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

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.scrollToOnFocus() = composed {
    val scope = rememberCoroutineScope()
    val requester = remember { BringIntoViewRequester() }

    bringIntoViewRequester(requester)
        .onFocusEvent {
            if (it.isFocused) {
                scope.launch {
                    delay(250)
                    requester.bringIntoView()
                }
            }
        }
}

fun Modifier.focusOnEntry() = composed {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    focusRequester(focusRequester = focusRequester)
}
