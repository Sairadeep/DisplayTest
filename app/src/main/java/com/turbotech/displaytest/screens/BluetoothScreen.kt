package com.turbotech.displaytest.screens

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.turbotech.displaytest.viewModel.HRViewModel


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BluetoothScreen(navController: NavController, hrViewModel: HRViewModel) {
    /**
     * To implement the actual behaviour
     */
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        context.startActivity(intent)
    }
    BackHandler(
        onBack = {
            navController.navigate("HomePage")
        }
    )
    /*if (!hrViewModel.textToDisplayState.value) {
        SplashScreen(
            displayText = stringResource(id = R.string.bluetooth_detail),
            hrViewModel
        )
    } else {
     Surface {
         Column(
             modifier = Modifier.fillMaxSize(),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
         ) {
             val state = hrViewModel.state.collectAsState()
             val scannedDevices: List<BluetoothDevice> = state.value.scannedDevices
             val pairedDevices: List<BluetoothDevice> = state.value.pairedDevices
             Log.d("BluetoothScreenResults", "paired Devices: $pairedDevices")
             Log.d("BluetoothScreenResults", "scanned Devices: $scannedDevices")
             hrViewModel.startScan()
             if (pairedDevices.isNotEmpty()) {
                 LazyColumn {
                     items(pairedDevices) { sd ->
                         Text(sd.name, fontSize = 20.sp, color = Color.White)
                     }
                 }
             } else {
                 Text(text = "To Do")
             }
         }
     }
     DisposableEffect(Unit) {
         onDispose {
             Log.d("BluetoothScreenResults", "Stopping scan")
             hrViewModel.stopScan()
         }
     } }  */
}
