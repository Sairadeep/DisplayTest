package com.turbotech.displaytest.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.viewModel.DisplayTestVM
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController, displayTestVM: DisplayTestVM) {
    val context = LocalContext.current
    val itemText = remember { mutableStateOf("Dummy") }
    val testName = remember { mutableIntStateOf(108) }
    val testResult = remember {
        mutableStateOf(true)
    }
    val testStartState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(1000)
    }
    val allTestResults = displayTestVM.getAllTestResults()
    Log.d("allTestResults", "$allTestResults")

    Surface {
        Scaffold(
            topBar = {
                displayTestVM.DisplayTopAppBar(text = "Display Test", navController)
            }
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.purple_200))
                    .fillMaxSize()
                    .padding(it), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                testName.intValue = displayTestVM.getSpecificTestName()
                testResult.value = displayTestVM.specificTestResult()
                testStartState.value = displayTestVM.isSpecificTestStarted()
                Log.d("PageLoader", "${testName.intValue} ${testResult.value}")
                LazyVerticalGrid(columns = GridCells.Adaptive(225.dp)) {
                    items(count = 4) { index ->
                        Card(
                            onClick = {
                                when (index) {

                                    0 -> {
                                        navController.navigate(route = "SwipeScreenTest")
                                    }

                                    1 -> {
                                        if (allTestResults[displayTestVM.swipeTestName] != null) {
                                            navController.navigate(route = "SingleTouch")
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please complete swipe screen test first",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    2 -> {
                                        if (allTestResults[displayTestVM.singleTouchTestName] != null) {
                                            navController.navigate(route = "MultiTouch")
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please complete single touch test first",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    3 -> {
                                        if (allTestResults[displayTestVM.multiTouchTestName] != null) {
                                            navController.navigate(route = "PinchToZoom")
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please complete multi touch test first",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(45.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                when {
                                    index == 0 && allTestResults[displayTestVM.swipeTestName] == true -> Color.Green
                                    index == 0 && allTestResults[displayTestVM.swipeTestName] == false -> Color.Red
                                    index == 1 && allTestResults[displayTestVM.singleTouchTestName] == true -> Color.Green
                                    index == 1 && allTestResults[displayTestVM.singleTouchTestName] == false -> Color.Red
                                    index == 2 && allTestResults[displayTestVM.multiTouchTestName] == true -> Color.Green
                                    index == 2 && allTestResults[displayTestVM.multiTouchTestName] == false -> Color.Red
                                    index == 3 && allTestResults[displayTestVM.pinchToZoomTestName] == true -> Color.Green
                                    index == 3 && allTestResults[displayTestVM.pinchToZoomTestName] == false -> Color.Red
                                    else -> Color.LightGray
                                },
                                disabledContentColor = Color.Magenta
                            ),
                            enabled = true,
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 4.dp,
                                focusedElevation = 4.dp,
                                hoveredElevation = 4.dp
                            )
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                when (index) {

                                    0 -> {
                                        itemText.value = "Swipe Screen"
                                    }

                                    1 -> {
                                        itemText.value = "Single Touch"
                                    }

                                    2 -> {
                                        itemText.value = "Multi Touch"
                                    }

                                    3 -> {
                                        itemText.value = "Pinch To Zoom"
                                    }

                                    else -> {
                                        itemText.value = "Dummy"
                                    }
                                }
                                TextFn(text = itemText.value, color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }
    }
}




