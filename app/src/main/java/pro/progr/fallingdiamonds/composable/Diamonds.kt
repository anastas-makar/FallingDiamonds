package pro.progr.fallingdiamonds

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun Diamonds(viewModel: DiamondViewModelInterface) {
    val rowWidth = remember { mutableStateOf(0) }

    val boxHeight = remember { mutableStateOf(0) }
    val screenWidth = remember { mutableStateOf(0) }
    val sundukHeight = remember { mutableStateOf(0) }
    val diamondCapHeight = remember { mutableStateOf(0) }
    val diamondHeight = remember { mutableStateOf(0) }

    val finalOffset =  remember { mutableStateOf(0) }

    finalOffset.value = boxHeight.value - sundukHeight.value - diamondCapHeight.value

    val diamonds = listOf(
        R.drawable.diamond_green,
        R.drawable.diamond_navi,
        R.drawable.diamond_red,
        R.drawable.diamond_lilac,
        R.drawable.diamond_violet)

    // ---- 2. Модель искры ----
    data class Sparkle(
        val id: Int,
        var offset: Offset,
        var alpha: Float,
        val velocity: Offset,
        val birthTime: Long,
        val lifetime: Long = 1000L,
        val color: Color
    )

    // ---- 1. Список искр над сундуком ----
    val chestSparkles = remember { mutableStateListOf<Sparkle>() }

    // ---- 3. Фоновая генерация искр ----
    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            chestSparkles.removeAll { now - it.birthTime > it.lifetime }

            // Обновляем
            chestSparkles.forEach {
                it.offset += it.velocity
                it.alpha *= 0.95f
            }

            // С шансом ~1 раз в 700 мс — спавним искру
            if (Random.nextFloat() < 0.03f) {
                val chestLeftOffset = (screenWidth.value - rowWidth.value) / 2f
                val origin = Offset(
                    Random.nextFloat() * rowWidth.value + chestLeftOffset,
                    (boxHeight.value - sundukHeight.value - 20).toFloat() - Random.nextFloat() * finalOffset.value
                )

                val sparkCount = (8..12).random()

                repeat(sparkCount) {
                    val angle = Random.nextFloat() * 2 * Math.PI
                    val speed = Random.nextFloat() * 2f + 1f
                    val velocity = Offset(
                        x = (speed * kotlin.math.cos(angle)).toFloat(),
                        y = (speed * kotlin.math.sin(angle)).toFloat()
                    )

                    val color = Color(listOf(
                        0xFF006064,
                        0xFF1A237E,
                        0xFFB71C1C,
                        0xFF880E4F,
                        0xFF4A148C
                    ).random())

                    chestSparkles.add(
                        Sparkle(
                            id = chestSparkles.size,
                            offset = origin,
                            alpha = 0.5f,
                            velocity = velocity,
                            birthTime = now,
                            color = color
                        )
                    )
                }
            }


            delay(16)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .onGloballyPositioned { coordinates ->
            boxHeight.value = coordinates.size.height
            screenWidth.value = coordinates.size.width
        }
    ) {

        Image(
            painter = painterResource(id = R.drawable.sunduk),
            contentDescription = "Сундук",
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        Box(modifier = Modifier
            .align(alignment = Alignment.TopCenter)
            .width(with(LocalDensity.current) { rowWidth.value.toDp() })
            .wrapContentHeight()
            .onGloballyPositioned { coordinates ->
                diamondCapHeight.value = coordinates.size.height / 4
                diamondHeight.value = coordinates.size.height
            }) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    diamonds.forEachIndexed { index, drawableId ->
                        Box(modifier = Modifier.weight(0.1f)) {
                            val draggableDiamonds = remember { mutableStateListOf(0L) }

                            draggableDiamonds.forEach {diamondId ->
                                    DraggableDiamond(
                                        drawableId = drawableId,
                                        grid = grid,
                                        slotNum = index,
                                        onDragStarted = {
                                            draggableDiamonds.add(draggableDiamonds.size.toLong())
                                        },
                                        onAnimationFinished = {
                                        },
                                        viewModel = viewModel
                                    )
                            }
                        }
                    }
                }
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.BottomCenter)
            .padding(bottom = sundukHeight.value.dp)) {

            val time = System.currentTimeMillis()

            chestSparkles.forEach {
                val age = time - it.birthTime
                val pulse = 1f + 0.3f * kotlin.math.sin(age / 100f)

                drawCircle(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            it.color.copy(alpha = it.alpha),
                            Color.Transparent
                        ),
                        center = it.offset,
                        radius = 30f * pulse
                    ),
                    radius = 30f * pulse,
                    center = it.offset
                )
            }
        }

        grid.gridWidth.value = rowWidth.value.toFloat()
        grid.gridHeight.value = finalOffset.value.toFloat()
        grid.diamondHeight.value = diamondHeight.value.toFloat()

        Image(
            painter = painterResource(id = R.drawable.polsunduka),
            contentDescription = "Сундук",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { coordinates ->
                    rowWidth.value = coordinates.size.width
                    sundukHeight.value = coordinates.size.height
                }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            grid.clearSlots()
        }
    }
}