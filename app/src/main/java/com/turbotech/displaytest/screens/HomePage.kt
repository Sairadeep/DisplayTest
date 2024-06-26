package com.turbotech.displaytest.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.ConnectivityCardImages
import com.turbotech.displaytest.components.DisplayCardImages
import com.turbotech.displaytest.components.Permission
import com.turbotech.displaytest.components.SpeakerCardImages
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.cardElevation
import com.turbotech.displaytest.components.topAppBarColorCombo
import com.turbotech.displaytest.viewModel.DisplayTestVM
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomePage(navController: NavHostController, displayTestVM: DisplayTestVM) {
    val allTestResults = displayTestVM.getAllTestResults()

    LaunchedEffect(Unit) {
        delay(1000)
    }

    Surface {
        Scaffold(
            topBar = {
                HomeTopBar()
            }
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.purple_200))
                    .fillMaxSize()
                    .padding(it), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomePageDesign(
                    navController = navController,
                    allTestResults = allTestResults,
                    displayTestVM = displayTestVM
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomePageDesign(
    navController: NavHostController,
    allTestResults: Map<String, Boolean>,
    displayTestVM: DisplayTestVM
) {
    val expandableState = remember { mutableStateMapOf<Int, Boolean>() }
    val heightOfIt = 250.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomStart = 28.dp))
            .background(Color.Cyan)
    ) {
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(5.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                items(count = 3) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(
                                width = 1.5.dp,
                                color = colorResource(id = displayTestVM.borderColor),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.size(75.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextFn(text = "Hello", color = Color.Magenta, size = 16)
                        }
                    }
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 7.dp)
    ) {
        items(count = 5) { hpIndex ->
            val rotateValue by animateFloatAsState(
                targetValue = if (expandableState[hpIndex] == true) 180f else 0f,
                label = "Rotate Arrow"
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(1.5.dp, Color.Black, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when (hpIndex) {
                            0 -> {
                                Text(
                                    text = "Display Check",
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(6f)
                                )
                            }

                            1 -> {
                                Text(
                                    text = "Speaker Check",
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(6f)
                                )
                            }

                            2 -> {
                                Text(
                                    text = "Connectivity Test",
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(6f)
                                )
                            }

                            3 -> {
                                Text(
                                    text = "Sensor Test",
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(6f)
                                )
                            }

                            else -> {
                                Text(
                                    text = "Camera Test",
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(6f)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .rotate(rotateValue)
                                .background(color = Color.LightGray, shape = CircleShape)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(displayTestVM.borderColor),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick =
                                {
                                    expandableState[hpIndex] = expandableState[hpIndex] != true
                                }
                            ) {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                    expandableState[hpIndex]?.let {
                        AnimatedVisibility(visible = it) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = displayTestVM.borderColor),
                                        shape = RoundedCornerShape(12.dp),
                                    )
                            ) {
                                when (hpIndex) {
                                    0 -> {
                                        DisplayTestOptions(
                                            navController = navController,
                                            allTestResults = allTestResults,
                                            height = heightOfIt,
                                            displayTestVM = displayTestVM
                                        )
                                    }

                                    1 -> SpeakerTestOptions(
                                        heightOfIt,
                                        allTestResults,
                                        displayTestVM
                                    )

                                    2 -> {
                                        ConnectivityOptions(
                                            navController,
                                            allTestResults,
                                            displayTestVM
                                        )
                                    }

                                    else -> {
                                        TextFn(
                                            text = "No cards available",
                                            color = Color.Black,
                                            size = 16
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopBar() {
    TopAppBar(
        title = { TextFn(text = "Display Test", color = Color.White, size = 22) },
        colors = topAppBarColorCombo(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DisplayTestOptions(
    navController: NavHostController,
    allTestResults: Map<String, Boolean>,
    height: Dp,
    displayTestVM: DisplayTestVM
) {
    val itemText = remember { mutableStateOf("Dummy") }
    val context = LocalContext.current
    val displayImageId = remember { mutableIntStateOf(0) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .height(height)
    ) {
        items(count = 4) { index ->
            Card(
                onClick = {
                    subPagesNavigation(
                        index,
                        navController,
                        context,
                        allTestResults,
                        displayTestVM
                    )
                },
                modifier = Modifier
                    .size(125.dp)
                    .padding(6.dp),
                border = BorderStroke(1.5.dp, color = colorResource(displayTestVM.borderColor)),
                colors = CardDefaults.cardColors(
                    containerColor = cardColorForDT(index, allTestResults, displayTestVM),
                    contentColor = Color.Black
                ),
                enabled = true,
                elevation = cardElevation()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (index) {
                            0 -> {
                                displayImageId.intValue = R.drawable.swipe
                                itemText.value = "Swipe"
                            }

                            1 -> {
                                displayImageId.intValue = R.drawable.singletouch
                                itemText.value = "Single Touch"
                            }

                            2 -> {
                                displayImageId.intValue = R.drawable.multitouch
                                itemText.value = "Multi Touch"
                            }

                            else -> {
                                displayImageId.intValue = R.drawable.pinch
                                itemText.value = "Pinch"
                            }
                        }
                        Row {
                            DisplayCardImages(id = displayImageId.intValue)
                        }
                        Spacer(modifier = Modifier.height(7.dp))

                        Row {
                            TextFn(text = itemText.value, color = Color.Black, size = 16)
                        }
                    }
                }
            }

        }
    }
}

private fun cardColorForDT(
    index: Int,
    allTestResults: Map<String, Boolean>,
    displayTestVM: DisplayTestVM
) = when {
    index == 0 && allTestResults[displayTestVM.swipeTestName] == true -> Color.Green
    index == 0 && allTestResults[displayTestVM.swipeTestName] == false -> Color.Red
    index == 1 && allTestResults[displayTestVM.singleTouchTestName] == true -> Color.Green
    index == 1 && allTestResults[displayTestVM.singleTouchTestName] == false -> Color.Red
    index == 2 && allTestResults[displayTestVM.multiTouchTestName] == true -> Color.Green
    index == 2 && allTestResults[displayTestVM.multiTouchTestName] == false -> Color.Red
    index == 3 && allTestResults[displayTestVM.pinchToZoomTestName] == true -> Color.Green
    index == 3 && allTestResults[displayTestVM.pinchToZoomTestName] == false -> Color.Red
    else -> {
        Color.White
    }
}

private fun subPagesNavigation(
    index: Int,
    navController: NavHostController,
    context: Context,
    allTestResults: Map<String, Boolean>,
    displayTestVM: DisplayTestVM
) {
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
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SpeakerTestOptions(
    height: Dp,
    allTestResults: Map<String, Boolean>,
    displayTestVM: DisplayTestVM
) {
    val speakerImageId = remember { mutableIntStateOf(0) }
    val speakerCardText = remember { mutableStateOf("Dummy") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        Modifier.height(height)
    ) {
        items(count = 6) { spoIndex ->
            when (spoIndex) {
                0 -> {
                    speakerImageId.intValue = R.drawable.speaker1
                    speakerCardText.value = "Speaker"
                }

                1 -> {
                    speakerImageId.intValue = R.drawable.vibration
                    speakerCardText.value = "Vibration"
                }

                2 -> {
                    speakerImageId.intValue = R.drawable.mic_24
                    speakerCardText.value = "Microphone"
                }

                3 -> {
                    speakerImageId.intValue = R.drawable.ringtone
                    speakerCardText.value = "Ringtone"
                }

                4 -> {
                    speakerImageId.intValue = R.drawable.alarm_on_24
                    speakerCardText.value = "Alarm"
                }

                5 -> {
                    speakerImageId.intValue = R.drawable.notifications_24
                    speakerCardText.value = "Notification"
                }

            }
            Card(
                modifier = Modifier
                    .size(125.dp)
                    .padding(6.dp),
                elevation = cardElevation(),
                colors = CardDefaults.cardColors(
                    containerColor = displayTestVM.cardColorForST(spoIndex, allTestResults),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.5.dp, color = colorResource(displayTestVM.borderColor))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                enabled = true,
                                onClick = {
                                    displayTestVM.btmSheetExpand.value = true
                                    displayTestVM.xId.intValue = spoIndex
                                }
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row {
                            SpeakerCardImages(speakerImageId.intValue)
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        Row {
                            TextFn(text = speakerCardText.value, color = Color.Black, size = 16)
                        }
                    }
                }
            }
        }
    }
    SpeakerBottomSheet(displayTestVM)
}

@Composable
@RequiresApi(Build.VERSION_CODES.S)
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
fun SpeakerBottomSheet(
    displayTestVM: DisplayTestVM
) {
    val context = LocalContext.current
    val speakTestResult = remember { mutableStateOf(false) }
    val xText = remember { mutableStateOf("Playing Music") }
    val vibrator =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (displayTestVM.btmSheetExpand.value) {
        ModalBottomSheet(
            onDismissRequest = { displayTestVM.btmSheetExpand.value = false },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(187.dp)
                .padding(18.dp),
            scrimColor = Color.Transparent,
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                skipHiddenState = false
            ),
            contentColor = Color.Green,
            containerColor = colorResource(id = displayTestVM.borderColor)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                 * Need to verify that the device volume isn't set to minimum or muted.
                 */
                when (displayTestVM.xId.intValue) {
                    0 -> {
                        displayTestVM.MediaPlayerCtrl()
                        displayTestVM.TextToSpeakFn(context)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (displayTestVM.toShowBtn.value) {
                            TextFn(text = displayTestVM.yText.value, color = Color.Black, size = 24)
                            Row {
                                Button(onClick = {
                                    speakTestResult.value = true
                                }) {
                                    Text(
                                        text = "Yes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (speakTestResult.value) {
                                        displayTestVM.UpdateResultAfterTest(
                                            context = LocalContext.current,
                                            testName = displayTestVM.speakTestName,
                                            testResult = true
                                        )
                                        displayTestVM.btmSheetExpand.value = false
                                        speakTestResult.value = false
                                    } else {
                                        Log.d(
                                            "speakTestResult",
                                            "speakTestResult.value is empty"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Button(onClick = {
                                    displayTestVM.btmSheetExpand.value = false
                                    displayTestVM.ttsStatus.value = false
                                }) {
                                    Text(
                                        text = "No",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            TextFn(text = xText.value, color = Color.Black, size = 24)
                        }
                    }

                    1 -> {
                        if (displayTestVM.vibrationTestResults.value) {
                            displayTestVM.UpdateResultAfterTest(
                                context = LocalContext.current,
                                testName = displayTestVM.vibrationTestName,
                                testResult = true
                            )
                            displayTestVM.vibrationTestResults.value = false
                        }
                        TextFn(text = "Vibration Test", color = Color.Black, size = 24)
                        displayTestVM.VibrationCtrl(vibrator)
                    }

                    2 -> {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.CALL_PHONE
                                ),
                                99
                            )
                        }
                        Text(
                            text = "How are the things going? \n \n Speak out the above text",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        /**
                         * Need to implement this 😒😒😒😒
                         */
                        displayTestVM.SpeechRecZ()
                    }

                    3 -> {
                        if (displayTestVM.ringtoneTestResults.value) {
                            displayTestVM.UpdateResultAfterTest(
                                context = context,
                                testName = displayTestVM.ringtoneTestName,
                                testResult = true
                            )
                            displayTestVM.ringtoneTestResults.value = false
                        }
                        TextFn(
                            text = "Playing Ringtone...!",
                            color = Color.Black,
                            size = 22
                        )
                        displayTestVM.RingtoneManager(context)
                    }

                    4 -> {
                        if (displayTestVM.alarmTestResults.value) {
                            displayTestVM.UpdateResultAfterTest(
                                context = context,
                                testName = displayTestVM.alarmTestName,
                                testResult = true
                            )
                            displayTestVM.alarmTestResults.value = false
                        }
                        TextFn(
                            text = "Playing Alarm Tone...!",
                            color = Color.Black,
                            size = 22
                        )
                        displayTestVM.RingtoneManager(context)
                    }

                    5 -> {
                        if (displayTestVM.notificationTestResults.value) {
                            displayTestVM.UpdateResultAfterTest(
                                context = context,
                                testName = displayTestVM.notificationTestName,
                                testResult = true
                            )
                            displayTestVM.notificationTestResults.value = false
                        }

                        TextFn(
                            text = "Playing Notification Tone...!",
                            color = Color.Black,
                            size = 22
                        )
                        displayTestVM.RingtoneManager(context)
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Dummy ${displayTestVM.xId.intValue}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ConnectivityOptions(
    navController: NavController,
    allTestResults: Map<String, Boolean>,
    displayTestVM: DisplayTestVM
) {

    val context = LocalContext.current
    val connectivityImageId = remember { mutableIntStateOf(0) }
    val connectivityCardText = remember { mutableStateOf("Dummy") }
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(125.dp)) {
        items(count = 3) { coi ->
            when (coi) {
                0 -> {
                    connectivityImageId.intValue = R.drawable.wifi
                    connectivityCardText.value = "Wi-Fi"
                }

                1 -> {
                    connectivityImageId.intValue = R.drawable.bluetooth
                    connectivityCardText.value = "Bluetooth"
                }

                else -> {
                    connectivityImageId.intValue = R.drawable.gps
                    connectivityCardText.value = "Location"
                }
            }
            Card(
                modifier = Modifier
                    .size(125.dp)
                    .padding(6.dp),
                elevation = cardElevation(),
                colors = CardDefaults.cardColors(
                    containerColor = displayTestVM.connectivityCardColors(coi, allTestResults),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.5.dp, color = colorResource(displayTestVM.borderColor))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                enabled = true,
                                onClick = {
                                    when (coi) {

                                        0 -> {
                                            if (Permission.getPermission()) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Navigating to Wifi Screen",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                navController.navigate("WifiScreen")
                                            }
                                        }

                                        1 -> {
                                            val bluetoothIntent =
                                                Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                                            context.startActivity(bluetoothIntent)
                                        }

                                        2 -> {
                                            val gpsIntent =
                                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                            context.startActivity(gpsIntent)
                                        }

                                        else -> {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "None of the above",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                }
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            ConnectivityCardImages(connectivityImageId.intValue)
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        Row {
                            TextFn(
                                text = connectivityCardText.value,
                                color = Color.Black,
                                size = 16
                            )
                        }
                    }
                }
            }
        }
    }
}