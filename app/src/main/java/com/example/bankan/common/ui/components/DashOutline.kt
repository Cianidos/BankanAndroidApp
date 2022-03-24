package com.example.bankan.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density

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