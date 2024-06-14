package com.turbotech.displaytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turbotech.displaytest.screens.HomePage
import com.turbotech.displaytest.screens.MultiTouches
import com.turbotech.displaytest.screens.PinchToZoom
import com.turbotech.displaytest.screens.SingleTouch
import com.turbotech.displaytest.screens.SwipeScreenTest
import com.turbotech.displaytest.ui.theme.DisplayTestTheme
import com.turbotech.displaytest.viewModel.DisplayTestVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
    }
}

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
    }
}


