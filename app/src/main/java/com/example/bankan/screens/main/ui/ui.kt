package com.example.bankan.screens.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankan.common.ui.theme.BankanTheme


@Preview
@Composable
fun MainMenu() {
    BankanTheme {
    }
}


@Preview
@Composable
fun BoardListPreview() {
    BoardList(list = listOf("first board", "newxt", "1", "", "board next door"))
}

@Composable
fun BoardList(list: List<String>) {
    Column {
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

