package com.turbotech.displaytest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.navigation.NavController
import com.turbotech.displaytest.viewModel.DisplayTestVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MultiTouches(navController: NavController, displayTestVM: DisplayTestVM) {

    LaunchedEffect(Unit) {
        displayTestVM.insertResultBeforeTest(displayTestVM.multiTouchTestName)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                displayTestVM.DisplayTopAppBar(text = displayTestVM.multiTouchTestName, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInteropFilter { motionEvent ->
                        displayTestVM.releaseStateOnMotionEvent(motionEvent)
                        true
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                displayTestVM.MultiTestResultDisplayText(navController = navController)
            }
        }
    }
}

