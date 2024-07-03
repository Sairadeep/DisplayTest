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
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.HRViewModel

@Composable
fun PinchToZoom(navController: NavController, HRViewModel: HRViewModel) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        HRViewModel.insertResultBeforeTest(HRViewModel.pinchToZoomTestName)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBarFn(text = HRViewModel.pinchToZoomTestName, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (HRViewModel.zoomTransformableState()) {
                    HRViewModel.UpdateResultAfterTest(
                        context = context,
                        testName = HRViewModel.pinchToZoomTestName,
                        testResult = true
                    )
                    navController.navigate("HomePage")
                }
            }
        }
    }
}