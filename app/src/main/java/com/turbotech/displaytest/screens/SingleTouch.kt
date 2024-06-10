package com.turbotech.displaytest.screens

import android.util.Log
import android.widget.Toast
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.model.DisplayEntities
import com.turbotech.displaytest.viewModel.DisplayTestVM
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTouch(navController: NavController, displayTestVM: DisplayTestVM) {

    val context = LocalContext.current
    val boxState = remember { mutableStateOf(false) }
    val xPosition = remember { mutableStateOf(0.dp) }
    val yPosition = remember { mutableStateOf(0.dp) }
    val noOfClicks = remember { mutableIntStateOf(0) }
    val noC = remember {
        mutableIntListOf()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { TextFn(text = "Single Touch Test Page", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("HomePage") }) {
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
        bottomBar = {
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
                    if (noOfClicks.intValue <= 15) {
                        Text(
                            text = " Remaining Clicks : ${noOfClicks.intValue} / 15",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            text = "Test Completed",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    ) { it ->

        Column(
            modifier = Modifier
                .padding(it)
                .pointerInput(Unit) {
//                  convert it to dp to avoid conversion of pixels or xdp or xxxdp
                    detectTapGestures { offset ->
                        xPosition.value = offset.x.toDp()
                        yPosition.value = offset.y.toDp()
                        boxState.value = true
                        noOfClicks.intValue += 1
                        noC.add(noOfClicks.intValue)
                    }
                }
                .fillMaxSize()
                .background(Color.Red)
        ) {
//          How to make it stay on the UI
            if (noOfClicks.intValue <= 15) {
                if (xPosition.value >= 45.dp && yPosition.value >= 0.dp && boxState.value) {
                    Box(
                        modifier = Modifier
                            .padding(
//                          subtract the radius of the circle of interaction, so that the box stays at the center of interaction.
                                start = (xPosition.value - 45.dp),
                                top = (yPosition.value - 45.dp)
                            )
                            .size(90.dp)
                            .onGloballyPositioned { lc ->
                                Log.d(
                                    "noC_TC",
                                    "I'm ${lc.positionInParent()}, ${lc.positionInRoot().y}"
                                )
                            }
                            .background(Color.Green, shape = RoundedCornerShape(30))
                    )
                }
            } else {
                // provide animation or navigate to home screen with a delay of 2 seconds
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Test completed...!", Toast.LENGTH_SHORT).show()
                    delay(2000)
                    navController.navigate("HomePage")
                    displayTestVM.insertResult(
                        DisplayEntities(
                            swipeScreenTestResult = false,
                            singleTouchTestResult = true
                        )
                    )
//                    On navigating back send the size of entries, also.
                }
            }
        }
    }
}
