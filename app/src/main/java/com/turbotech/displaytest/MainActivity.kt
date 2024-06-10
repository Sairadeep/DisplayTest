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
import com.turbotech.displaytest.screens.MultiTouch
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
    val viewModel = viewModel<DisplayTestVM>()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "HomePage") {
        composable("HomePage") {
            HomePage(navController, viewModel)
        }
        composable(route = "SwipeScreenTest") {
            SwipeScreenTest(navController)
        }
        composable("SingleTouch") {
            SingleTouch(navController, viewModel)
        }
        composable(route = "MultiTouch") {
            MultiTouch(navController)
        }
//        composable("StillToDecide"){
//            StillToDecide()
//        }
    }
}


