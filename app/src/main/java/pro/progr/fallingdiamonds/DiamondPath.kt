package pro.progr.fallingdiamonds

import androidx.compose.runtime.mutableStateOf

class DiamondPath(
    val firstYOffset : Float,
    val fallDownYOffset : Float,
    val xOffset : Float? = null,
    val secondYOffset : Float? = null
) {
    //todo: это поле использовать надо было
    val fallDown = mutableStateOf(false)

    val dropDown = mutableStateOf(false)
}