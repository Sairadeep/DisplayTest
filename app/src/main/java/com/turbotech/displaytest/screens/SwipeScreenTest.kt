package com.turbotech.displaytest.screens

import android.util.Log
import android.view.MotionEvent
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
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwipeScreenTest(navController: NavController) {
    BackHandler (
        enabled = true,
        onBack = {
            navController.navigate("HomePage")
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

    Column(
    modifier = Modifier
    .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
                if((((this.drawContext.size.width - sizeRemainedInWidth.floatValue) > 10) || ((this.drawContext.size.height - sizeRemainedInHeight.floatValue) > 10))
                    && ((System.currentTimeMillis() - lastInteractionTime.longValue) < 10)){
                    Log.d("Draw_Size","Display has an issue")
                }else{
                    Log.d("Draw_Size","Display is fine")
                }
            }
        }
    }
}