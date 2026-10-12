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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            contentDescription = stringResource(R.string.falling_diamonds_chest),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier.padding(5.dp))  {
            Text(
                text = stringResource(R.string.falling_diamonds_drawer_open_chest),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)) {
                Text(text = stringResource(R.string.falling_diamonds_drawer_diamond_count, diamondsTotal.value))
                Icon(
                    painter = painterResource(id = R.drawable.ic_diamond),
                    contentDescription = stringResource(R.string.falling_diamonds_diamond_icon),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(12.dp)
                )
            }
        }

    }
}
