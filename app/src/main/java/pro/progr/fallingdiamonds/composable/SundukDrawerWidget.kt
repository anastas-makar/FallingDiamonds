package pro.progr.fallingdiamonds.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pro.progr.fallingdiamonds.R

@Composable
fun SundukDrawerWidget(diamondsTotal: State<Int>,
                       navFun : () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable {
        navFun()
    }) {

        Image(
            painter = painterResource(id = R.drawable.sunduk_closed),
            contentDescription = "Сундук",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(modifier = Modifier.align(Alignment.CenterHorizontally))  {
            Text(text = "Открыть сундук с ${diamondsTotal.value}", style = MaterialTheme.typography.h6)
            Icon(
                painter = painterResource(id = R.drawable.ic_diamond),
                contentDescription = "Иконка бриллианта",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )
        }

    }
}