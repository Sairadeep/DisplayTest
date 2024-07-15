package com.turbotech.displaytest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.HRViewModel

@Composable
fun SingleTouch(navController: NavController, hRViewModel: HRViewModel) {
    LaunchedEffect(Unit) {
        hRViewModel.insertResultBeforeTest(hRViewModel.singleTouchTestName)
    }
    if (!hRViewModel.textToDisplayState.value) {
        SplashScreen(displayText = stringResource(id = R.string.singleTouch_Detail), hRViewModel)
    } else {
    Scaffold(
        topBar = {
            TopAppBarFn(hRViewModel.singleTouchTestName, navController)
        },
        bottomBar = {
            SingleTouchBottomBar(hRViewModel)
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .pointerInput(Unit) {
//                  convert it to dp to avoid conversion of pixels or xdp or xxx dp
                    detectTapGestures { offset ->
                        hRViewModel.xPosition.value = offset.x.toDp()
                        hRViewModel.yPosition.value = offset.y.toDp()
                        hRViewModel.boxState.value = true
                        hRViewModel.noOfClicks.intValue += 1
                        hRViewModel.noC.add(hRViewModel.noOfClicks.intValue)
                    }
                }
                .fillMaxSize()
                .background(Color.Red)
        ) {
            hRViewModel.ClickSelectionBox(navController = navController)
        }
      }
    }
    DisposableEffect(Unit) {
        onDispose {
            hRViewModel.textToDisplayState.value = false
        }
    }
}

@Composable
private fun SingleTouchBottomBar(hRViewModel: HRViewModel) {
    BottomAppBar(
        modifier = Modifier
            .height(55.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.LightGray, shape = RoundedCornerShape(30.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            hRViewModel.TextBasedOnClicks()
        }
    }
}
