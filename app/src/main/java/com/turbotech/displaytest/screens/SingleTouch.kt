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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.HRViewModel

@Composable
fun SingleTouch(navController: NavController, HRViewModel: HRViewModel) {

    LaunchedEffect(Unit) {
        HRViewModel.insertResultBeforeTest(HRViewModel.singleTouchTestName)
    }

    Scaffold(
        topBar = {
            TopAppBarFn(HRViewModel.singleTouchTestName,navController)
        },
        bottomBar = {
            SingleTouchBottomBar(HRViewModel)
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .pointerInput(Unit) {
//                  convert it to dp to avoid conversion of pixels or xdp or xxx dp
                    detectTapGestures { offset ->
                        HRViewModel.xPosition.value = offset.x.toDp()
                        HRViewModel.yPosition.value = offset.y.toDp()
                        HRViewModel.boxState.value = true
                        HRViewModel.noOfClicks.intValue += 1
                        HRViewModel.noC.add(HRViewModel.noOfClicks.intValue)
                    }
                }
                .fillMaxSize()
                .background(Color.Red)
        ) {
            HRViewModel.ClickSelectionBox(navController = navController)
        }
    }
}

@Composable
private fun SingleTouchBottomBar(HRViewModel: HRViewModel) {
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
            HRViewModel.TextBasedOnClicks()
        }
    }
}
