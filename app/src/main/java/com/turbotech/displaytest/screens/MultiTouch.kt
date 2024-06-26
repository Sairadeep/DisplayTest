package com.turbotech.displaytest.screens

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.DisplayTestVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MultiTouches(navController: NavController, displayTestVM: DisplayTestVM) {
    val currentClicks = remember { mutableIntStateOf(0) }
    val releaseState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        displayTestVM.insertResultBeforeTest(displayTestVM.multiTouchTestName)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBarFn(text = displayTestVM.multiTouchTestName, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInteropFilter { motionEvent ->
                        releaseStateOnMotionEvent(motionEvent, currentClicks, releaseState)
                        true
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                MultiTestResultDisplayText(
                    navController,
                    currentClicks,
                    releaseState,
                    displayTestVM
                )
            }
        }
    }
}

fun releaseStateOnMotionEvent(
    motionEvent: MotionEvent,
    currentClicks: MutableIntState,
    releaseState: MutableState<Boolean>
) {

    // Allows up to 5 Touches
    currentClicks.intValue = motionEvent.pointerCount

    when (motionEvent.action) {

        MotionEvent.ACTION_DOWN -> {
            releaseState.value = true
        }

        MotionEvent.ACTION_POINTER_DOWN -> {
            releaseState.value = true
        }

        MotionEvent.ACTION_UP -> {
            currentClicks.intValue -= 1
        }

        MotionEvent.ACTION_POINTER_UP -> {
            currentClicks.intValue -= 1
        }

    }
}

@Composable
fun MultiTestResultDisplayText(
    navController: NavController,
    currentClicks: MutableIntState,
    releaseState: MutableState<Boolean>,
    displayTestVM: DisplayTestVM
) {
    if (releaseState.value) {
        TextFn(
            text = "Multi Touch has ${currentClicks.intValue} touches",
            color = Color.White,
            size = 18
        )
        Log.d("currentClicks", "${currentClicks.intValue}")
        if (currentClicks.intValue > 2) {
            displayTestVM.UpdateResultAfterTest(
                context = LocalContext.current,
                testName = displayTestVM.multiTouchTestName,
                testResult = true
            )
            navController.navigate("HomePage")
        }
    }
}


