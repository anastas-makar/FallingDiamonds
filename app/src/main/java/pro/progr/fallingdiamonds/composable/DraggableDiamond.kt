package pro.progr.fallingdiamonds

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.progr.diamondapi.AddDiamondsInterface
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun DraggableDiamond(
    @DrawableRes drawableId: Int,
    grid: Grid,
    slotNum: Int,
    onDragStarted: () -> Unit,
    onAnimationFinished: () -> Unit,
    viewModel: AddDiamondsInterface
) {
    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    val offsetX = remember { Animatable(0f) }
    val shouldFindSlot = remember { mutableStateOf(true) }
    val diamondPath = remember { mutableStateOf<DiamondPath?>(null) }
    val shouldRender = remember { mutableStateOf(true)}
    // Модель искры
    data class Sparkle(
        val id: Int,
        var offset: Offset,
        var alpha: Float,
        val velocity: Offset,
        val birthTime: Long,
        val lifetime: Long = 1000L
    )

    val sparkles = remember { mutableStateListOf<Sparkle>() }

    // Обновление искр
    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            sparkles.removeAll { now - it.birthTime > it.lifetime }
            sparkles.forEach {
                it.offset += it.velocity
                it.alpha *= 0.70f
            }
            delay(16L)
        }
    }
    if (shouldRender.value) {

        Canvas(modifier = Modifier.fillMaxWidth()) {
            sparkles.forEach {
                drawCircle(
                    color = Color.LightGray.copy(alpha = it.alpha),
                    radius = 10f,
                    center = it.offset
                )
            }
        }

        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "1 бриллиант",
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
                }
                .draggable(
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            offsetY.snapTo(offsetY.value + delta)
                        }
                    },
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        if (shouldFindSlot.value) {
                            withContext(Dispatchers.Main) {
                                diamondPath.value = grid.getDiamondPath(slotNum)
                            }
                        }
                    },
                    onDragStopped = {
                        onDragStarted()
                        if (diamondPath.value != null && shouldFindSlot.value) {
                            viewModel.add(1)
                            coroutineScope.launch {
                                offsetY.animateTo(
                                    targetValue = diamondPath.value!!.firstYOffset,
                                    animationSpec = tween(durationMillis = 1500),
                                    block = {
                                        val now = System.currentTimeMillis()
                                        if (diamondPath.value!!.firstYOffset > (offsetY.value + 10f)) {
                                            sparkles.add(
                                                Sparkle(
                                                    id = sparkles.size,
                                                    offset = Offset(
                                                        offsetX.value + grid.gridWidth.value / grid.slots.size / 2 + Random.nextFloat() * 20f - 10f,
                                                        value + Random.nextFloat() * 20f - 10f
                                                    ),
                                                    alpha = 0.7f,
                                                    velocity = Offset(
                                                        Random.nextFloat() * 1.5f - 0.75f,
                                                        Random.nextFloat() * -2f - 0.5f
                                                    ),
                                                    birthTime = now
                                                )
                                            )
                                        }
                                    }
                                )

                                diamondPath.value!!.xOffset?.let { xOffset ->
                                    offsetX.animateTo(
                                        targetValue = xOffset,
                                        animationSpec = tween(durationMillis = 800)
                                    )
                                }

                                diamondPath.value!!.secondYOffset?.let { secondYOffset ->
                                    offsetY.animateTo(
                                        targetValue = secondYOffset,
                                        animationSpec = tween(durationMillis = 200)
                                    )
                                }

                                shouldFindSlot.value = false
                            }

                        } else if (diamondPath.value != null && !shouldFindSlot.value) {
                            val target = diamondPath.value!!.secondYOffset
                                ?: diamondPath.value!!.firstYOffset
                            offsetY.animateTo(
                                targetValue = target,
                                animationSpec = tween(durationMillis = 1500)
                            )
                        }
                    }
                )
        )
    }

    if (diamondPath.value?.dropDown?.value == true) {
        diamondPath.value?.fallDownYOffset?.let { falldownOffset ->
            coroutineScope.launch {
                offsetY.animateTo(
                    targetValue = falldownOffset,
                    animationSpec = tween(durationMillis = 200)
                )
                shouldRender.value = false
                onAnimationFinished()
            }
        }
    }
}
