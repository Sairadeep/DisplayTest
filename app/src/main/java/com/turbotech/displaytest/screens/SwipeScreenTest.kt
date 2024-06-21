package com.turbotech.displaytest.screens

import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.turbotech.displaytest.viewModel.DisplayTestVM

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwipeScreenTest(navController: NavController, displayTestVM: DisplayTestVM) {

    val context = LocalContext.current
    val timerState = remember { mutableStateOf(false) }
    lateinit var timer: CountDownTimer
    val drawWidth = remember { mutableFloatStateOf(0f) }
    val drawHeight = remember { mutableFloatStateOf(0f) }
    BackHandler (
        enabled = false,
        onBack = {
            if (timerState.value) {
                Toast.makeText(context, "Back Navigation restricted", Toast.LENGTH_SHORT).show()
            } else {
                navController.navigate("HomePage")
            }
        }
    )
    val lastTouchInX = remember {
        mutableFloatStateOf(0f)
    }
    val lastTouchInY = remember {
        mutableFloatStateOf(0f)
    }
    // Path
    val path = remember { mutableStateOf<Path?>(Path()) }
    val source = remember { MutableInteractionSource() }
    val sizeRemainedInWidth = remember { mutableFloatStateOf(0f) }
    val sizeRemainedInHeight = remember { mutableFloatStateOf(0f) }
    val lastInteractionTime = remember {
        mutableLongStateOf(0L)
    }
    val swipeResult = remember { mutableStateOf(false) }
    val drawTimeStatus = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        displayTestVM.insertResultBeforeTest(displayTestVM.swipeTestName)
    }

    Column(
    modifier = Modifier
    .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("TimerX", "yet to end ${millisUntilFinished / 1000}")
                drawTimeStatus.value = false
            }

            override fun onFinish() {
                drawTimeStatus.value = true
                Toast.makeText(context, "Test Completed..!", Toast.LENGTH_SHORT).show()
                navController.navigate("HomePage")
            }

        }

        LaunchedEffect(Unit) {
            timer.start()
        }
        
        Box {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = source,
                        indication = rememberRipple(bounded = true, color = Color.Magenta),
                        onClick = { }, enabled = true
                    )
                    .pointerInteropFilter { motionEvent ->

                        when (motionEvent.action) {

                            MotionEvent.ACTION_DOWN -> {
                                path.value?.moveTo(motionEvent.x, motionEvent.y)
                                lastTouchInX.floatValue = motionEvent.x
                                lastTouchInY.floatValue = motionEvent.y
                                lastInteractionTime.longValue = System.currentTimeMillis()
                            }

                            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                                val historySize = motionEvent.historySize
                                for (i in 0 until historySize) {
                                    val x = motionEvent.getHistoricalX(i)
                                    val y = motionEvent.getHistoricalY(i)
//                                  Draw a line segment with custom canvas or moveTo() or cubicTo() etc
                                    path.value?.lineTo(x, y)
                                    lastInteractionTime.longValue = System.currentTimeMillis()
                                }
                                path.value?.lineTo(motionEvent.x, motionEvent.y)
                                lastInteractionTime.longValue = System.currentTimeMillis()
                                lastTouchInX.floatValue = motionEvent.x
                                lastTouchInY.floatValue = motionEvent.y
                            }
                        }
                        lastTouchInX.floatValue = motionEvent.x
                        lastTouchInY.floatValue = motionEvent.y

//                      for refresh
                        val tempPath = path.value
                        path.value = null
                        path.value = tempPath

                        true
                    }
            ) {
                drawWidth.floatValue = this.drawContext.size.width
                drawHeight.floatValue = this.drawContext.size.height
//              Draw path
                Log.d("Draw_Size", "${this.drawContext.size}")
                path.value?.let {
                    drawPath(
                        path = it,
                        color = Color.Green,
                        style = Stroke(
                            width = 100f
                        )
                    )
                }
                Log.d("Draw_Size","I'm ${this.size.width} ${path.value?.getBounds()?.width} ${path.value?.getBounds()?.height}")
                sizeRemainedInWidth.floatValue = path.value?.getBounds()?.width!!
                sizeRemainedInHeight.floatValue = path.value?.getBounds()?.height!!
            }
           if(drawTimeStatus.value) {
                val x = drawWidth.floatValue - sizeRemainedInWidth.floatValue
                val y = drawHeight.floatValue - sizeRemainedInHeight.floatValue
                if (x > 30 || y > 30) {
                    Log.d("TimerX", "Display has an issue $x, $y")
                    swipeResult.value = false
                    timer.cancel()
                } else {
                    Log.d("TimerX", "Display is fine $x, $y")
                    swipeResult.value = true
                    timer.cancel()
                }
                timer.cancel()
            }
            if (swipeResult.value) {
                displayTestVM.UpdateResultAfterTest(
                    context = context,
                    testName = displayTestVM.swipeTestName,
                    testResult = true
                )
                navController.navigate("HomePage")
            }
        }
    }
}
