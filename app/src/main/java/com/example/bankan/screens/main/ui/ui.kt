package com.example.bankan.screens.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bankan.common.ui.theme.BankanTheme
import kotlin.math.abs
import kotlin.math.sin


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
            BoardCard(it)
        }
    }
}



@Composable
fun BoardCard(boardName: String) {
    val vm = viewModel<AnimationViewModel>()
    val time by vm.times.collectAsState()
    val space = abs(sin(time.toFloat() / 66f)) * 20f

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(26f, space), 0f)
    val stroke = Stroke(
        width = 3f,
        pathEffect = pathEffect
    )
    BankanTheme {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Cyan,
                            style = stroke,
                            cornerRadius = CornerRadius(10f),
                            topLeft = Offset(2f, 2f),
                            size = size.copy(width = size.width - 4f, height = size.height - 4f)
                        )
                    },
            ) {
                Text(modifier = Modifier.padding(10.dp), text = boardName)
            }
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

