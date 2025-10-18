package pro.progr.fallingdiamonds.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
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
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
        .clickable {
            navFun()
        }) {

        Image(
            painter = painterResource(id = R.drawable.sunduk_closed),
            contentDescription = "Сундук",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier.align(Alignment.CenterHorizontally))  {
            Text(text = "Открыть сундук", modifier = Modifier.align(Alignment.CenterHorizontally))
            Row(modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)) {
                Text(text = "с ${diamondsTotal.value}")
                Icon(
                    painter = painterResource(id = R.drawable.ic_diamond),
                    contentDescription = "Иконка бриллианта",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(12.dp)
                )
            }
        }

    }
}