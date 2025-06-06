package pro.progr.fallingdiamonds

import androidx.compose.runtime.mutableStateOf

class DiamondPath(
    val firstYOffset : Float,
    val fallDownYOffset : Float,
    val xOffset : Float? = null,
    val secondYOffset : Float? = null
) {

    val dropDown = mutableStateOf(false)
}