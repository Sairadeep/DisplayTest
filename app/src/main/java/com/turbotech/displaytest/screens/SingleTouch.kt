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
import com.turbotech.displaytest.viewModel.DisplayTestVM

@Composable
fun SingleTouch(navController: NavController, displayTestVM: DisplayTestVM) {

    LaunchedEffect(Unit) {
        displayTestVM.insertResultBeforeTest(displayTestVM.singleTouchTestName)
    }

    Scaffold(
        topBar = {
            TopAppBarFn(displayTestVM.singleTouchTestName,navController)
        },
        bottomBar = {
            SingleTouchBottomBar(displayTestVM)
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .pointerInput(Unit) {
//                  convert it to dp to avoid conversion of pixels or xdp or xxx dp
                    detectTapGestures { offset ->
                        displayTestVM.xPosition.value = offset.x.toDp()
                        displayTestVM.yPosition.value = offset.y.toDp()
                        displayTestVM.boxState.value = true
                        displayTestVM.noOfClicks.intValue += 1
                        displayTestVM.noC.add(displayTestVM.noOfClicks.intValue)
                    }
                }
                .fillMaxSize()
                .background(Color.Red)
        ) {
            displayTestVM.ClickSelectionBox(navController = navController)
        }
    }
}

@Composable
private fun SingleTouchBottomBar(displayTestVM: DisplayTestVM) {
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
            displayTestVM.TextBasedOnClicks()
        }
    }
}
