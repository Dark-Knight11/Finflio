package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.mr_pine.zoomables.ZoomableState
import de.mr_pine.zoomables.rememberZoomableState
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OpenableImage(
    modifier: Modifier = Modifier,
    imageUrl: Any? = null
) {
    val dialogState = rememberMaterialDialogState()
    val scope = rememberCoroutineScope()
    val zoomableState = rememberZoomableState(rotationBehavior = ZoomableState.Rotation.DISABLED)
    var visibility by remember { mutableStateOf(true) }

    LaunchedEffect(dialogState.showing) {
        if (!dialogState.showing) {
            zoomableState.apply {
                scale.value = 1f
                offset.value = Offset.Zero
            }
        }
    }
    LaunchedEffect(key1 = visibility) {
        if (visibility) {
            delay(3500)
            visibility = false
        }
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = "attachment",
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { dialogState.show() },
        contentScale = ContentScale.Crop
    )

    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = RectangleShape,
        backgroundColor = Color.Black.copy(0.4f),
        elevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Zoomable(
                modifier = Modifier.fillMaxSize(),
                coroutineScope = scope,
                zoomableState = zoomableState,
                onTap = {
                    visibility = !visibility
                }
            ) {
                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun Zoomable(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    zoomableState: ZoomableState,
    dragGesturesEnabled: ZoomableState.() -> Boolean = { true },
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    minimumSwipeDistance: Int = 0,
    onDoubleTap: ((Offset) -> Unit)? = null,
    onTap: ((Offset) -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    onPointerMovement: ((Boolean) -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit)
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var composableCenter by remember { mutableStateOf(Offset.Zero) }
    var transformOffset by remember { mutableStateOf(Offset.Zero) }

    val doubleTapFunction = onDoubleTap ?: {
        if (zoomableState.scale.value != 1f) {
            coroutineScope.launch {
                zoomableState.animateBy(
                    zoomChange = 1 / zoomableState.scale.value,
                    panChange = -zoomableState.offset.value,
                    rotationChange = -zoomableState.rotation.value
                )
            }
            Unit
        } else {
            coroutineScope.launch {
                zoomableState.animateZoomToPosition(2f, position = it, composableCenter)
            }
            Unit
        }
    }

    fun onTransformGesture(
        centroid: Offset,
        pan: Offset,
        zoom: Float,
        transformRotation: Float
    ) {
        val rotationChange =
            if (zoomableState.rotationBehavior == ZoomableState.Rotation.DISABLED) 0f else transformRotation

        val tempOffset = zoomableState.offset.value + pan

        val x0 = centroid.x - composableCenter.x
        val y0 = centroid.y - composableCenter.y

        val hyp0 = sqrt(x0 * x0 + y0 * y0)
        val hyp1 = zoom * hyp0 * (
            if (x0 > 0) {
                1f
            } else {
                -1f
            }
            )

        val alpha0 = atan(y0 / x0)

        val alpha1 = alpha0 + (rotationChange * ((2 * PI) / 360))

        val x1 = cos(alpha1) * hyp1
        val y1 = sin(alpha1) * hyp1

        transformOffset =
            centroid - (composableCenter - tempOffset) - Offset(x1.toFloat(), y1.toFloat())

        coroutineScope.launch {
            zoomableState.transform {
                transformBy(
                    zoomChange = zoom,
                    panChange = transformOffset - zoomableState.offset.value,
                    rotationChange = rotationChange
                )
            }
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap,
                    onDoubleTap = doubleTapFunction,
                    onLongPress = {
                        onLongPress?.invoke()
                    }
                )
            }
            .pointerInput(Unit) {
                // TODO()
                forEachGesture {
                    awaitPointerEventScope {
                        var transformRotation = 0f
                        var zoom = 1f
                        var pan = Offset.Zero
                        var pastTouchSlop = false
                        val touchSlop = viewConfiguration.touchSlop
                        var lockedToPanZoom = false
                        var drag: PointerInputChange?
                        var overSlop = Offset.Zero

                        val down = awaitFirstDown(requireUnconsumed = false)

                        var transformEventCounter = 0
                        do {
                            val event = awaitPointerEvent()
                            val canceled = event.changes.fastAny { it.isConsumed }
                            var relevant = true
                            if (event.changes.size > 1) {
                                if (!canceled) {
                                    val zoomChange = event.calculateZoom()
                                    val rotationChange = event.calculateRotation()
                                    val panChange = event.calculatePan()

                                    if (!pastTouchSlop) {
                                        zoom *= zoomChange
                                        transformRotation += rotationChange
                                        pan += panChange

                                        val centroidSize =
                                            event.calculateCentroidSize(useCurrent = false)
                                        val zoomMotion = abs(1 - zoom) * centroidSize
                                        val rotationMotion =
                                            abs(
                                                transformRotation * PI.toFloat() * centroidSize / 180f
                                            )
                                        val panMotion = pan.getDistance()

                                        if (zoomMotion > touchSlop ||
                                            rotationMotion > touchSlop ||
                                            panMotion > touchSlop
                                        ) {
                                            pastTouchSlop = true
                                            lockedToPanZoom =
                                                zoomableState.rotationBehavior == ZoomableState.Rotation.LOCK_ROTATION_ON_ZOOM_PAN && rotationMotion < touchSlop
                                        }
                                    }

                                    if (pastTouchSlop) {
                                        val eventCentroid =
                                            event.calculateCentroid(useCurrent = false)
                                        val effectiveRotation =
                                            if (lockedToPanZoom) 0f else rotationChange
                                        if (effectiveRotation != 0f ||
                                            zoomChange != 1f ||
                                            panChange != Offset.Zero
                                        ) {
                                            onTransformGesture(
                                                eventCentroid,
                                                panChange,
                                                zoomChange,
                                                effectiveRotation
                                            )
                                        }
                                        event.changes.fastForEach {
                                            if (it.positionChanged()) {
                                                it.consume()
                                            }
                                        }
                                    }
                                }
                            } else if (transformEventCounter > 3) relevant = false
                            transformEventCounter++
                        } while (!canceled && event.changes.fastAny { it.pressed } && relevant)

                        if (zoomableState.dragGesturesEnabled()) {
                            do {
                                awaitPointerEvent()
                                drag = awaitTouchSlopOrCancellation(down.id) { change, over ->
                                    if (change.positionChange() != Offset.Zero) change.consume()
                                    overSlop = over
                                }
                                onPointerMovement?.invoke(true)
                            } while (drag != null && !drag.isConsumed)
                            if (drag != null) {
                                dragOffset = Offset.Zero
                                if (zoomableState.scale.value !in 0.92f..1.08f) {
                                    coroutineScope.launch {
                                        zoomableState.transform {
                                            transformBy(1f, overSlop, 0f)
                                        }
                                    }
                                } else {
                                    dragOffset += overSlop
                                }
                                if (drag(drag.id) {
                                    if (zoomableState.scale.value !in 0.92f..1.08f) {
                                        zoomableState.offset.value += it.positionChange()
                                    } else {
                                        dragOffset += it.positionChange()
                                    }
                                    if (it.positionChange() != Offset.Zero) it.consume()
                                }
                                ) {
                                    if (zoomableState.scale.value in 0.92f..1.08f) {
                                        val offsetX = dragOffset.x
                                        if (offsetX > minimumSwipeDistance) {
                                            onSwipeRight()
                                        } else if (offsetX < -minimumSwipeDistance) {
                                            onSwipeLeft()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RectangleShape)
                .offset {
                    IntOffset(
                        zoomableState.offset.value.x.roundToInt(),
                        zoomableState.offset.value.y.roundToInt()
                    )
                }
                .graphicsLayer(
                    scaleX = zoomableState.scale.value,
                    scaleY = zoomableState.scale.value,
                    rotationZ = zoomableState.rotation.value
                )
                .onGloballyPositioned { coordinates ->
                    val localOffset =
                        Offset(
                            coordinates.size.width.toFloat() / 2,
                            coordinates.size.height.toFloat() / 2
                        )
                    val windowOffset = coordinates.localToWindow(localOffset)
                    composableCenter =
                        coordinates.parentLayoutCoordinates?.windowToLocal(windowOffset)
                            ?: Offset.Zero
                },
            content = content,
            contentAlignment = Alignment.Center
        )
    }
}
