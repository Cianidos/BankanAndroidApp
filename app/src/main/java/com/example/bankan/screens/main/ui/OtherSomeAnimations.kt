package com.example.bankan.screens.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.DragExample
import com.example.bankan.common.ui.EachAndBetween
import com.example.bankan.common.ui.SwipeableSample
import com.example.bankan.common.ui.TransformableSample
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.main.viewmodel.AnimationViewModel
import org.koin.androidx.compose.viewModel
import kotlin.math.roundToInt


@Preview
@Composable
fun MultitouchExamplesPreview(modifier: Modifier = Modifier) {
    val viewModel by viewModel<AnimationViewModel>()
    val list by viewModel.list.collectAsState()

    Column(modifier = modifier) {
        SwipeableBoardList(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            list = list,
            onDelete = { viewModel.deleteAtIndex(it) }
        )
        Box(Modifier.weight(1f)) {
            SwipeableSample()
        }
        TransformableSample(Modifier.weight(1f))
        DragExample(Modifier.weight(1f))
    }
}

@Composable
fun SwipeableBoardList(
    modifier: Modifier = Modifier,
    list: List<String>,
    onDelete: (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        EachAndBetween(data = list.withIndex()) {
            SwipeableBoardCard(text = it.value, onDelete = { onDelete(it.index) })
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableBoardCard(modifier: Modifier = Modifier, text: String, onDelete: () -> Unit) {
    var contentSize: Size by remember { mutableStateOf(Size(48f, 48f)) }

    val swipeableState = rememberSwipeableState(0) {
        if (it == 1) {
            onDelete()
            false
        } else true
    }

    val anchors = mapOf(0f to 0, contentSize.width to 1) // Maps anchor points (in px) to states

    Box(
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Green)
    ) {
        AnimatedDashedOutlineBoardCard(modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .background(Color.DarkGray)
            .onGloballyPositioned {
                contentSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
            }, boardName = text
        )
    }
}


@Composable
fun AnimatedDashedOutlineCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val vm by viewModel<AnimationViewModel>()
    val time by vm.times.collectAsState()

    fun Int.breakOn(pointOfBreak: Int): Int {
        return if (this <= pointOfBreak) this else pointOfBreak * 2 - this
    }

    val intervals = floatArrayOf(
        time.mod(101).plus(20).breakOn(70).toFloat(),
        70.minus(time.mod(101).plus(20).breakOn(70)).toFloat(),
    )
    BankanTheme {
        DashOutline(
            modifier = modifier,
            strokeColor = Color.Cyan,
            intervals = intervals,
            phase = -time.toFloat()
        ) {
            content()
        }
    }
}

@Composable
fun AnimatedDashedOutlineBoardCard(modifier: Modifier = Modifier, boardName: String) {
    AnimatedDashedOutlineCard(modifier = modifier) {
        Text(modifier = Modifier.padding(10.dp), text = boardName)
    }
}
