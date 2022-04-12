package com.example.bankan.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
inline fun Float.asDp(): Dp {
    return with(LocalDensity.current) { this@asDp.toDp() }
}

@Composable
inline fun Int.asDp(): Dp {
    return with(LocalDensity.current) { this@asDp.toDp() }
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


fun <T> LazyListScope.eachAndBetween(
    data: List<T>,
    spacerHeight: Dp = 10.dp,
    spacerWidth: Dp = spacerHeight,
    content: @Composable LazyItemScope.(T) -> Unit
) {
    eachAndBetween(data = data, between = {
        Spacer(
            modifier = Modifier
                .height(spacerHeight)
                .width(spacerWidth)
        )
    }, content = content)
}

fun <T> LazyListScope.eachAndBetween(
    data: List<T>,
    between: @Composable () -> Unit,
    content: @Composable LazyItemScope.(T) -> Unit
) {
    itemsIndexed(data) { idx, value ->
        content(value)
        if (idx != data.lastIndex)
            between()
    }
}
