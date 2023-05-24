package com.finflio.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

class DiamondShape(fabSize: Float) : Shape {
    private val fabRadius = fabSize / 2f
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                moveTo(size.width / 2 - fabRadius, size.height / 2)
                relativeLineTo(fabRadius, fabRadius)
                relativeLineTo(fabRadius, -fabRadius)
                relativeLineTo(-fabRadius, -fabRadius)
            }
        )
    }
}
