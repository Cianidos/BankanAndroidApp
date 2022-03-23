package com.example.bankan.screens.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankan.common.ui.theme.BankanTheme
import kotlin.math.roundToInt


@Preview
@Composable
fun MainMenu() {
    BankanTheme {
    }
}


@Preview
@Composable
fun BoardListPreview() {
    val viewModel = viewModel<AnimationViewModel>()
    val list by viewModel.list.collectAsState()

    Column {
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
        BoardCard(modifier = Modifier
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .background(Color.DarkGray)
            .onGloballyPositioned {
                contentSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
            }, boardName = text
        )
    }
}

@Composable
inline fun Float.asDp(): Dp {
    return with(LocalDensity.current) { this@asDp.toDp() }
}

@Composable
inline fun Int.asDp(): Dp {
    return with(LocalDensity.current) { this@asDp.toDp() }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableSample(modifier: Modifier = Modifier) {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}

@Composable
fun DragExample(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}

@Composable
fun TransformableSample(modifier: Modifier = Modifier) {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(modifier = modifier) {
        Box(
            Modifier
                .fillMaxSize()
                // apply other transformations like rotation and zoom
                // on the pizza slice emoji
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                // add transformable to listen to multitouch transformation events
                // after offset
                .transformable(state = state)
                .background(Color.Red)
        )
    }
}

@Composable
fun BoardList(modifier: Modifier = Modifier, list: List<String>) {
    Column(modifier = modifier) {
        EachAndBetween(data = list) {
            BoardCard(boardName = it)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}


@Composable
fun BoardCard(modifier: Modifier = Modifier, boardName: String) {
    val vm = viewModel<AnimationViewModel>()
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
            Text(modifier = Modifier.padding(10.dp), text = boardName)
        }
    }
}

@Composable
fun DashOutline(
    modifier: Modifier = Modifier,
    cornersSize: CornerSize = MaterialTheme.shapes.medium.topStart,
    strokeColor: Color = MaterialTheme.colors.secondary,
    strokeWidth: Float = 6f,
    intervals: FloatArray = floatArrayOf(strokeWidth * 6f, strokeWidth * 3f),
    phase: Float = 0f,
    content: @Composable () -> Unit
) {
    val pathEffect = PathEffect.dashPathEffect(intervals, phase)
    val stroke = Stroke(
        width = strokeWidth,
        pathEffect = pathEffect,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
    )
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .drawBehind {
                val density = Density(density, fontScale)
                drawRoundRect(
                    color = strokeColor,
                    style = stroke,
                    cornerRadius = CornerRadius(cornersSize.toPx(size, density)),
                    topLeft = Offset(stroke.width / 2f, stroke.width / 2f),
                    size = size.copy(
                        width = size.width - stroke.width,
                        height = size.height - stroke.width
                    )
                )
            },
    ) {
        content()
    }
}

@Composable
fun <T> EachAndBetween(
    data: Iterable<T>,
    spacerHeight: Dp = 10.dp,
    content: @Composable (T) -> Unit
) {
    EachAndBetween(data = data, between = {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacerHeight)
        )
    }, content = content)
}

@Composable
fun <T> EachAndBetween(
    data: Iterable<T>,
    between: @Composable () -> Unit,
    content: @Composable (T) -> Unit
) {
    val iterator: Iterator<T> = data.iterator()
    while (iterator.hasNext()) {
        content(iterator.next())
        if (iterator.hasNext())
            between()
    }
}

