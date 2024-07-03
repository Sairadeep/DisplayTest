package com.turbotech.displaytest.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Vibrator
import android.provider.Settings
import android.util.Half.EPSILON
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
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.turbotech.displaytest.components.SensorCardImages
import com.turbotech.displaytest.components.SpeakerCardImages
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.cardElevation
import com.turbotech.displaytest.components.topAppBarColorCombo
import com.turbotech.displaytest.viewModel.HRViewModel
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomePage(navController: NavHostController, hRViewModel: HRViewModel) {
    val allTestResults = hRViewModel.getAllTestResults()

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
                    hRViewModel = hRViewModel
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
    hRViewModel: HRViewModel
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
                                color = colorResource(id = hRViewModel.borderColor),
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
                                    color = colorResource(hRViewModel.borderColor),
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
                                        color = colorResource(id = hRViewModel.borderColor),
                                        shape = RoundedCornerShape(12.dp),
                                    )
                            ) {
                                when (hpIndex) {
                                    0 -> {
                                        DisplayTestOptions(
                                            navController = navController,
                                            allTestResults = allTestResults,
                                            height = heightOfIt,
                                            hRViewModel = hRViewModel
                                        )
                                    }

                                    1 -> SpeakerTestOptions(
                                        heightOfIt,
                                        allTestResults,
                                        hRViewModel
                                    )

                                    2 -> {
                                        ConnectivityOptions(
                                            navController,
                                            allTestResults,
                                            hRViewModel
                                        )
                                    }

                                    3 -> {
                                        SensorOptions(
                                            allTestResults = allTestResults,
                                            hRViewModel = hRViewModel
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
    hRViewModel: HRViewModel
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
                        hRViewModel
                    )
                },
                modifier = Modifier
                    .size(125.dp)
                    .padding(6.dp),
                border = BorderStroke(1.5.dp, color = colorResource(hRViewModel.borderColor)),
                colors = CardDefaults.cardColors(
                    containerColor = hRViewModel.cardColorForDT(index, allTestResults, hRViewModel),
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

private fun subPagesNavigation(
    index: Int,
    navController: NavHostController,
    context: Context,
    allTestResults: Map<String, Boolean>,
    hRViewModel: HRViewModel
) {
    when (index) {

        0 -> {
            navController.navigate(route = "SwipeScreenTest")
        }

        1 -> {
            if (allTestResults[hRViewModel.swipeTestName] != null) {
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
            if (allTestResults[hRViewModel.singleTouchTestName] != null) {
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
            if (allTestResults[hRViewModel.multiTouchTestName] != null) {
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
    hrViewModel: HRViewModel
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
                    containerColor = hrViewModel.cardColorForST(spoIndex, allTestResults),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.5.dp, color = colorResource(hrViewModel.borderColor))
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
                                    hrViewModel.btmSheetExpand.value = true
                                    hrViewModel.xId.intValue = spoIndex
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
    SpeakerBottomSheet(hrViewModel)
}

@Composable
@RequiresApi(Build.VERSION_CODES.S)
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
fun SpeakerBottomSheet(
    hRViewModel: HRViewModel
) {
    val context = LocalContext.current
    val speakTestResult = remember { mutableStateOf(false) }
    val xText = remember { mutableStateOf("Playing Music") }
    val vibrator =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (hRViewModel.btmSheetExpand.value) {
        ModalBottomSheet(
            onDismissRequest = { hRViewModel.btmSheetExpand.value = false },
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
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**
                 * Need to verify that the device volume isn't set to minimum or muted.
                 */
                when (hRViewModel.xId.intValue) {
                    0 -> {
                        hRViewModel.MediaPlayerCtrl()
                        hRViewModel.TextToSpeakFn(context)
                        Spacer(modifier = Modifier.height(10.dp))
                        if (hRViewModel.toShowBtn.value) {
                            TextFn(text = hRViewModel.yText.value, color = Color.Black, size = 24)
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
                                        hRViewModel.UpdateResultAfterTest(
                                            context = LocalContext.current,
                                            testName = hRViewModel.speakTestName,
                                            testResult = true
                                        )
                                        hRViewModel.btmSheetExpand.value = false
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
                                    hRViewModel.btmSheetExpand.value = false
                                    hRViewModel.ttsStatus.value = false
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
                        if (hRViewModel.vibrationTestResults.value) {
                            hRViewModel.UpdateResultAfterTest(
                                context = LocalContext.current,
                                testName = hRViewModel.vibrationTestName,
                                testResult = true
                            )
                            hRViewModel.vibrationTestResults.value = false
                        }
                        TextFn(text = "Vibration Test", color = Color.Black, size = 24)
                        hRViewModel.VibrationCtrl(vibrator)
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
                        hRViewModel.SpeechRecZ()
                    }

                    3 -> {
                        if (hRViewModel.ringtoneTestResults.value) {
                            hRViewModel.UpdateResultAfterTest(
                                context = context,
                                testName = hRViewModel.ringtoneTestName,
                                testResult = true
                            )
                            hRViewModel.ringtoneTestResults.value = false
                        }
                        TextFn(
                            text = "Playing Ringtone...!",
                            color = Color.Black,
                            size = 22
                        )
                        hRViewModel.RingtoneManager(context)
                    }

                    4 -> {
                        if (hRViewModel.alarmTestResults.value) {
                            hRViewModel.UpdateResultAfterTest(
                                context = context,
                                testName = hRViewModel.alarmTestName,
                                testResult = true
                            )
                            hRViewModel.alarmTestResults.value = false
                        }
                        TextFn(
                            text = "Playing Alarm Tone...!",
                            color = Color.Black,
                            size = 22
                        )
                        hRViewModel.RingtoneManager(context)
                    }

                    5 -> {
                        if (hRViewModel.notificationTestResults.value) {
                            hRViewModel.UpdateResultAfterTest(
                                context = context,
                                testName = hRViewModel.notificationTestName,
                                testResult = true
                            )
                            hRViewModel.notificationTestResults.value = false
                        }

                        TextFn(
                            text = "Playing Notification Tone...!",
                            color = Color.Black,
                            size = 22
                        )
                        hRViewModel.RingtoneManager(context)
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Dummy ${hRViewModel.xId.intValue}",
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

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ConnectivityOptions(
    navController: NavController,
    allTestResults: Map<String, Boolean>,
    hRViewModel: HRViewModel
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
                    containerColor = hRViewModel.connectivityCardColors(coi, allTestResults),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.5.dp, color = colorResource(hRViewModel.borderColor))
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
                                            if (Permission.getLocPermission()) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Navigating to Wifi Screen",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                navController.navigate("WifiScreen")
                                            } else {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Please enable location permission",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        }

                                        1 -> {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Navigating to Bluetooth Setting Screen",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            navController.navigate("BluetoothScreen")
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

@Composable
fun SensorOptions(
    hRViewModel: HRViewModel,
    allTestResults: Map<String, Boolean>
) {
    val currentIndex = remember { mutableIntStateOf(99) }
    val btmSheetState = remember { mutableStateOf(false) }
    val sensorImageId = remember { mutableIntStateOf(0) }
    val sensorCardText = remember { mutableStateOf("Dummy") }
    if (btmSheetState.value) {
        sensorTestBottomSheet(
            state = btmSheetState,
            vM = hRViewModel,
            index = currentIndex.intValue
        )
    }
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(125.dp)) {
        items(count = 3) { index ->
            when (index) {
                0 -> {
                    sensorImageId.intValue = R.drawable.speed_24
                    sensorCardText.value = "Accelerometer"
                }

                1 -> {
                    sensorImageId.intValue = R.drawable.gyroscope
                    sensorCardText.value = "Gyroscope"
                }

                else -> {
                    sensorImageId.intValue = R.drawable.light_mode_24
                    sensorCardText.value = "Light Sensor"
                }

            }
            Card(
                modifier = Modifier
                    .size(125.dp)
                    .padding(6.dp),
                elevation = cardElevation(),
                colors = CardDefaults.cardColors(
                    containerColor = hRViewModel.cardColorForSensorTest(index, allTestResults),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.5.dp, color = colorResource(hRViewModel.borderColor))
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
                                    when (index) {

                                        0 -> {
                                            currentIndex.intValue = index
                                            btmSheetState.value = true
                                        }

                                        1 -> {
                                            currentIndex.intValue = index
                                            btmSheetState.value = true
                                        }

                                        else -> {
                                            currentIndex.intValue = index
                                            btmSheetState.value = true
                                        }
                                    }
                                }
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row {
                            SensorCardImages(id = sensorImageId.intValue)
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        Row {
                            TextFn(
                                text = sensorCardText.value,
                                color = Color.Black,
                                size = 14
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sensorTestBottomSheet(
    state: MutableState<Boolean>,
    vM: HRViewModel,
    index: Int
) {
    val context = LocalContext.current
    lateinit var timer: CountDownTimer
    val timerValue = remember { mutableLongStateOf(0L) }
    val lightResult = remember { mutableStateOf(false) }
    val gravity = FloatArray(3)
    val accelerationX = remember { mutableFloatStateOf(0f) }
    val accelerationY = remember { mutableFloatStateOf(0f) }
    val accelerationZ = remember { mutableFloatStateOf(0f) }
    val omegaMagnitude = remember { mutableFloatStateOf(0f) }
    val rotationX = remember { mutableFloatStateOf(0f) }
    val rotationY = remember { mutableFloatStateOf(0f) }
    val rotationZ = remember { mutableFloatStateOf(0f) }
    val lightIllumination = remember { mutableFloatStateOf(0f) }
    val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor) {
                gyroscope -> {
                    rotationX.value = event.values[0]
                    rotationY.value = event.values[1]
                    rotationZ.value = event.values[2]

                    omegaMagnitude.floatValue =
                        sqrt(rotationX.floatValue * rotationX.floatValue + rotationY.floatValue * rotationY.floatValue + rotationZ.floatValue * rotationZ.floatValue)

                    if (omegaMagnitude.floatValue > EPSILON) {
                        rotationX.floatValue /= omegaMagnitude.floatValue
                        rotationY.floatValue /= omegaMagnitude.floatValue
                        rotationZ.floatValue /= omegaMagnitude.floatValue
                    }
                    Log.d("Omega", "${omegaMagnitude.floatValue}")
                }

                accelerometer -> {
                    val alpha = 0.8f
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

                    accelerationX.floatValue = event.values[0] - gravity[0]
                    accelerationY.floatValue = event.values[1] - gravity[1]
                    accelerationZ.floatValue = event.values[2] - gravity[2]
                }

                lightSensor -> {
                    lightIllumination.floatValue = event.values[0]
                    Log.d("Light", "${lightIllumination.floatValue}")
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d("AccelerometerValues", "Accuracy: $accuracy")
        }
    }
    LaunchedEffect(Unit) {
        when (index) {
            0 -> {
                if (accelerometer != null) {
                    vM.insertResultBeforeTest(vM.accelerometerSensorTestName)
                    sensorManager.registerListener(
                        sensorEventListener,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                } else {
                    Log.d(
                        "LaunchedEffectRegistration",
                        "Can't register accelerometer as the device don't have it."
                    )
                }
            }

            1 -> {
                if (gyroscope != null) {
                    vM.insertResultBeforeTest(vM.gyroscopeSensorTestName)
                    sensorManager.registerListener(
                        sensorEventListener,
                        gyroscope,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                } else {
                    Log.d(
                        "LaunchedEffectRegistration",
                        "Can't register gyroscope as the device don't have it."
                    )
                }
            }

            else -> {
                if (lightSensor != null) {
                    vM.insertResultBeforeTest(vM.lightSensorTestName)
                    sensorManager.registerListener(
                        sensorEventListener,
                        lightSensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
            }
        }

    }
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
            state.value = false
        }
    }
    if (state.value) {
        ModalBottomSheet(
            onDismissRequest = { state.value = false },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
                .padding(18.dp),
            scrimColor = Color.Transparent,
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                skipHiddenState = false
            ),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (index) {
                    0 -> {
                        if (accelerometer != null) {
                            TextFn(
                                text = "Linear acceleration values",
                                color = Color.Black,
                                size = 20
                            )
                            Divider(
                                thickness = 2.dp,
                                color = Color.Black,
                                modifier = Modifier.width(250.dp)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "X: ${accelerationX.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "Y: ${accelerationY.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "Z: ${accelerationZ.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            if (accelerationX.floatValue > 20 || accelerationY.floatValue > 20 || accelerationZ.floatValue > 20) {
                                state.value = false
                                Log.d(
                                    "SpeakerBtmSheetStatus",
                                    "${state.value} X: ${accelerationX.floatValue} Y: ${accelerationY.floatValue} Z: ${accelerationZ.floatValue}"
                                )
                                vM.UpdateResultAfterTest(
                                    context = context,
                                    testName = vM.accelerometerSensorTestName,
                                    testResult = true
                                )
                            } else {
                                Log.d(
                                    "SpeakerBtmSheetStatus",
                                    "${state.value} X: ${accelerationX.floatValue} Y: ${accelerationY.floatValue} Z: ${accelerationZ.floatValue}"
                                )
                            }
                        } else {
                            TextFn(
                                text = "No Accelerometer sensor available",
                                color = Color.Black,
                                size = 24
                            )
                        }
                    }

                    1 -> {
                        if (gyroscope != null) {
                            TextFn(
                                text = "Gyroscope values",
                                color = Color.Black,
                                size = 20
                            )
                            Divider(
                                thickness = 2.dp,
                                color = Color.Black,
                                modifier = Modifier.width(200.dp)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "X: ${rotationX.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "Y: ${rotationY.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "Z: ${rotationZ.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            TextFn(
                                text = "Magnitude: ${omegaMagnitude.floatValue}",
                                color = Color.Black,
                                size = 18
                            )
                            if (omegaMagnitude.floatValue > 0.5) {
                                state.value = false
                                vM.UpdateResultAfterTest(
                                    context = context,
                                    testName = vM.gyroscopeSensorTestName,
                                    testResult = true
                                )
                            } else {
                                Log.d(
                                    "SpeakerBtmSheetStatus",
                                    "${state.value} X: ${accelerationX.floatValue} Y: ${accelerationY.floatValue} Z: ${accelerationZ.floatValue}"
                                )
                            }
                        } else {
                            TextFn(
                                text = "No Gyroscope sensor available",
                                color = Color.Black,
                                size = 24
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (lightSensor != null) {
                                timer = object : CountDownTimer(5000, 1000) {

                                    override fun onTick(millisUntilFinished: Long) {
                                        timerValue.longValue = (millisUntilFinished / 1000)
                                        Log.d("RemainingX", "${millisUntilFinished / 1000}")
                                    }


                                    override fun onFinish() {
                                        timer.cancel()
                                        if (lightIllumination.floatValue.toInt() in 1..10000 && lightIllumination.floatValue.toInt() > 10) {
                                            lightResult.value = true
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Test Failed..!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            lightResult.value = false
                                            state.value = false
                                        }
                                    }

                                }
                                LaunchedEffect(Unit) {
                                    timer.start()
                                }
                                TextFn(
                                    text = "Light Sensor Illumination",
                                    color = Color.Black,
                                    size = 20
                                )
                                Divider(
                                    thickness = 2.dp,
                                    color = Color.Black,
                                    modifier = Modifier.width(300.dp)
                                )
                                Spacer(modifier = Modifier.size(15.dp))
                                TextFn(
                                    text = "Light Illumination: ${lightIllumination.floatValue} lux",
                                    color = Color.Black,
                                    size = 18
                                )
                                if (lightResult.value) {
                                    vM.UpdateResultAfterTest(
                                        context = context,
                                        testName = vM.lightSensorTestName,
                                        testResult = true
                                    )
                                    lightResult.value = false
                                    state.value = false
                                }
                            } else {
                                TextFn(
                                    text = "No Light Sensor available",
                                    color = Color.Black,
                                    size = 24
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}