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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.HRViewModel

@Composable
fun PinchToZoom(navController: NavController, hrViewModel: HRViewModel) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        hrViewModel.insertResultBeforeTest(hrViewModel.pinchToZoomTestName)
    }
    if (!hrViewModel.textToDisplayState.value) {
        SplashScreen(displayText = stringResource(id = R.string.Pin_Zoom_Detail), hrViewModel)
    } else {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBarFn(text = hrViewModel.pinchToZoomTestName, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (hrViewModel.zoomTransformableState()) {
                    hrViewModel.UpdateResultAfterTest(
                        context = context,
                        testName = hrViewModel.pinchToZoomTestName,
                        testResult = true
                    )
                    navController.navigate("HomePage")
                }
            }
        }
    }
    }
}