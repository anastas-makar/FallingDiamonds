package pro.progr.fallingdiamonds

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class Grid {


    val gridWidth = mutableStateOf(0f)
    val gridHeight = mutableStateOf(0f)
    val diamondHeight = mutableStateOf(0f)

    var slots = mutableStateListOf<DiamondPath?>(null, null, null, null, null)

    fun getDiamondPath(slotNum : Int) : DiamondPath {
        for (i in slots.size - 1 downTo 0) {
            if(slots[i] == null) {

                val diamondPath = getDiamondPath(slotNum, i)

                slots[i] = diamondPath

                return diamondPath
            }
        }

        slots.forEachIndexed { index, diamondPath ->
            diamondPath?.let {
                diamondPath.dropDown.value = true
            }
        }

        clearSlots()

        val diamondPath = getDiamondPath(slotNum, 4)

        slots[4] = diamondPath

        return diamondPath

    }

    private fun getDiamondPath(slotFrom : Int, slotTo : Int) : DiamondPath {

        return DiamondPath(
            firstYOffset = if (slotFrom <= slotTo) gridHeight.value else gridHeight.value - diamondHeight.value * 4 / 5,
            fallDownYOffset = gridHeight.value + diamondHeight.value,
            xOffset = if (slotFrom == slotTo) null else gridWidth.value / 5 * (slotTo - slotFrom),
            secondYOffset = if (slotFrom > slotTo) gridHeight.value else null
        )
    }

    fun clearSlots() {
        slots = mutableStateListOf<DiamondPath?>(null, null, null, null, null)
    }
}