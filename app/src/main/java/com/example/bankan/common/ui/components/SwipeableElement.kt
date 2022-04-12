package com.example.bankan.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableElement(
    modifier: Modifier = Modifier,
    onSwipe: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
) {
    val contentSize: MutableState<Size> = remember { mutableStateOf(Size(48f, 48f)) }

    val swipeableState = rememberSwipeableState(0) {
        if (it == 1) {
            onSwipe()
            false
        } else true
    }

    val anchors = mapOf(0f to 0, contentSize.value.width to 1)

    Box(
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Transparent)
    ) {
        content(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .onGloballyPositioned {
                    contentSize.value = Size(it.size.width.toFloat(), it.size.height.toFloat())
                },
        )
    }
}