package com.turbotech.displaytest.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.turbotech.displaytest.data.domain.BluetoothDevice
import com.turbotech.displaytest.viewModel.HRViewModel


@Composable
fun BluetoothScreen(navController: NavController, hrViewModel: HRViewModel) {
    BackHandler(
        onBack = {
            navController.navigate("HomePage")
        }
    )
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
                Text(text = "Hi")
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.d("BluetoothScreenResults", "Stopping scan")
            hrViewModel.stopScan()
        }
    }
}
