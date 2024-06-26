package com.turbotech.displaytest

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turbotech.displaytest.components.Permission
import com.turbotech.displaytest.screens.HomePage
import com.turbotech.displaytest.screens.MultiTouches
import com.turbotech.displaytest.screens.PinchToZoom
import com.turbotech.displaytest.screens.SingleTouch
import com.turbotech.displaytest.screens.SwipeScreenTest
import com.turbotech.displaytest.screens.WifiScreen
import com.turbotech.displaytest.ui.theme.DisplayTestTheme
import com.turbotech.displaytest.viewModel.DisplayTestVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisplayTestTheme {
                Surface {
                    Display_Test()
                }
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                199
            )
        }
    }

    @Suppress("Deprecation")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 199 && permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "Granted")
            Permission.setPermission()
        } else {
            Log.d("Permission", "Denied")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Display_Test() {
    val navController = rememberNavController()
    val viewModel = viewModel<DisplayTestVM>()

    NavHost(navController = navController, startDestination = "HomePage") {

        composable("HomePage") {
            HomePage(navController, viewModel)
        }
        composable(route = "SwipeScreenTest") {
            SwipeScreenTest(navController,viewModel)
        }
        composable("SingleTouch") {
            SingleTouch(navController,viewModel)
        }
        composable(route = "MultiTouch") {
            MultiTouches(navController,viewModel)
        }
        composable(route = "PinchToZoom") {
            PinchToZoom(navController,viewModel)
        }
        composable(route = "WifiScreen") {
            WifiScreen(navController, viewModel)
        }
    }
}


