package pro.progr.fallingdiamonds.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import pro.progr.diamondapi.AccumulateInterface
import pro.progr.fallingdiamonds.Diamonds

@Composable
fun SundukScreen(backFun : () -> Unit,
                 diamondViewModel : AccumulateInterface) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                SundukBar(
                    backFun,
                    diamondsTotal = diamondViewModel.getDiamondsCount().collectAsState(
                        initial = 0
                    )
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxHeight()
            ) {
                Diamonds(diamondViewModel)

                LaunchedEffect(Unit) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Вы сделали что-то хорошее? Бросьте бриллианты в сундук!")
                    }
                }
            }
        }
    )
}