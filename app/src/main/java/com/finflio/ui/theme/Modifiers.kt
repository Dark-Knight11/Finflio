package com.finflio.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.*

fun Modifier.glow(isGlow: Boolean, color: Color): Modifier {
    return if (isGlow) {
        this.then(
            Modifier.drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        0.0f to color.copy(0.5f), 1.0f to Color.Transparent, radius = 100f
                    ), blendMode = BlendMode.Lighten, radius = size.width
                )
            })
    } else this
}

fun Modifier.gradientBackground(
    colorStops: Array<Pair<Float, Color>> = emptyArray(),
    colors: List<Color> = emptyList(),
    angle: Float,
    extraX: Float = 0f,
    extraY: Float = 0f
) = this.then(
    Modifier.drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() //Fractional x
        val y = sin(angleRad).toFloat() //Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = min(offset.x.coerceAtLeast(0f), size.width),
            y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
        )

        if (colors.isNotEmpty()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(size.width + extraX, size.height + extraY) - exactOffset,
                    end = exactOffset
                ), size = size
            )
        } else {
            drawRect(
                brush = Brush.linearGradient(
                    colorStops = colorStops,
                    start = Offset(size.width + extraX, size.height + extraY) - exactOffset,
                    end = exactOffset
                ), size = size
            )
        }
    }
)

fun Modifier.gesturesDisabled() = pointerInput(Unit) {
    awaitPointerEventScope {
        // we should wait for all new pointer events
        while (true) {
            awaitPointerEvent(pass = PointerEventPass.Initial)
                .changes
                .forEach(PointerInputChange::consume)
        }
    }
}