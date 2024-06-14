package com.turbotech.displaytest.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.model.DisplayEntities
import com.turbotech.displaytest.viewModel.DisplayTestVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinchToZoom(navController: NavController, displayTestVM: DisplayTestVM) {
    val scale = remember {
        mutableFloatStateOf(1f)
    }
    val context = LocalContext.current
    val rotation = remember {
        mutableFloatStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    LaunchedEffect(Unit) {
        displayTestVM.insertResult(
            DisplayEntities(
                testName = displayTestVM.pinchToZoomTestName,
                isTestStarted = true,
                testResult = false
            )
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { TextFn(text = "Zoom Test Page", color = Color.White) },
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
                    .fillMaxSize()
                    .padding(it)
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1280f / 959f)
                ) {
                    val state =
                        rememberTransformableState { zoomChange, panChange, rotationChange ->

                            scale.floatValue =
                                (scale.floatValue * zoomChange).coerceIn(1f, 5f) // max zoom -> scale 5f

                            // Extra space left and right to be remove., scale minus
                            val extraWidth = (scale.floatValue - 1) * constraints.maxWidth
                            val extraHeight = (scale.floatValue - 1) * constraints.maxHeight

                            val maxX = extraWidth / 2 // left nd right
                            val maxY = extraHeight / 2

                            rotation.floatValue += rotationChange

                            // pan it as zoomed out
                            offset = Offset(
                                x = (offset.x + scale.floatValue * panChange.x).coerceIn(-maxX, maxX),
                                y = (offset.y + scale.floatValue * panChange.y).coerceIn(-maxY, maxY)
                            )
                        }
                    Image(
                        painter = painterResource(id = R.drawable.kermit),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale.floatValue
                                scaleY = scale.floatValue
                                rotationZ = rotation.floatValue
                                translationX = offset.x
                                translationY = offset.y
                            }
                            .transformable(state = state)
                    )
                    Log.d(
                        "Current_SCALE_ROTATION",
                        "scale: ${scale.floatValue} offset: $offset rotation: ${rotation.floatValue}"
                    )
                    // Rotation > 330 degrees and zoom up to 4 times
                    if (rotation.floatValue > 180 && scale.floatValue > 4) {
                        Toast.makeText(context, "Test Completed", Toast.LENGTH_SHORT).show()
                        displayTestVM.updateResult(
                            DisplayEntities(
                                id = displayTestVM.specificTestId(),
                                testName = displayTestVM.pinchToZoomTestName,
                                isTestStarted = false,
                                testResult = true
                            )
                        )
                        navController.navigate("HomePage")
                    }
                }
            }
        }
    }
}