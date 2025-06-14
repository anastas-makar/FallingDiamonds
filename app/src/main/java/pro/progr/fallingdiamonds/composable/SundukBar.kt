package pro.progr.fallingdiamonds.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pro.progr.fallingdiamonds.R

@Composable
fun SundukBar(backFun: () -> Unit, diamondsTotal: State<Int>) {
    TopAppBar(
        title = {
            Text(text = "Сундук с ${diamondsTotal.value}")
            Icon(
                painter = painterResource(id = R.drawable.ic_diamond),
                contentDescription = "Todo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = { backFun() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}