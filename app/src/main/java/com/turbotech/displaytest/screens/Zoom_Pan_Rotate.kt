package com.turbotech.displaytest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.turbotech.displaytest.viewModel.DisplayTestVM


@Composable
fun PinchToZoom(navController: NavController, displayTestVM: DisplayTestVM) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        displayTestVM.insertResultBeforeTest(displayTestVM.pinchToZoomTestName)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                displayTestVM.DisplayTopAppBar(text = displayTestVM.pinchToZoomTestName, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (displayTestVM.zoomTransformableState()) {
                    displayTestVM.UpdateResultAfterTest(
                        context = context,
                        testName = displayTestVM.pinchToZoomTestName,
                        testResult = true
                    )
                    navController.navigate("HomePage")
                }
            }
        }
    }
}


