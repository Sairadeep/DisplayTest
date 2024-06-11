package com.turbotech.displaytest.screens

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MultiTouches(navController: NavController) {

    val context = LocalContext.current
    val currentClicks = remember { mutableIntStateOf(0) }
    val releaseState = remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { TextFn(text = "Multi Touch Test Page", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate("HomePage")
                        }) {
                            IconBtnFn()
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInteropFilter { motionEvent ->

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
                        true
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                if (releaseState.value) {
                    TextFn(
                        text = "Multi Touch has ${currentClicks.intValue} touches",
                        color = Color.White
                    )
                    Log.d("currentClicks", "${currentClicks.intValue}")
                    if (currentClicks.intValue > 2) {
                        Toast.makeText(context, "Test completed...!", Toast.LENGTH_SHORT).show()
                        LaunchedEffect(Unit) {
                            delay(1200)
                            navController.navigate("HomePage")
                        }
//                        DB ni petali and also card design and animation
                    }
                }
            }
        }
    }
}