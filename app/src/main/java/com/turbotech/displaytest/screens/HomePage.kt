package com.turbotech.displaytest.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.CountDownTimer
import android.os.Vibrator
import android.provider.MediaStore
import android.provider.Settings
import android.util.Half.EPSILON
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.CamCardImages
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
    val context = LocalContext.current
    val vibrator by lazy {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    val indeText = remember { mutableStateOf("Test Results") }
    val resultText = remember {
        mutableStateOf("Dummy")
    }
    val passedCount = allTestResults.values.filter { it }.size
    val failedCount = allTestResults.values.filterNot { it }.size
    val totalTests = 18
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomStart = 28.dp))
            .background(Color.LightGray)
    ) {
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(5.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                items(count = 3) { inde ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, 4.dp)
                            .border(
                                width = 1.5.dp,
                                color = colorResource(id = hRViewModel.borderColor),
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .size(85.dp)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row {
                                when (inde) {
                                    0 -> {
                                        indeText.value = "Passed"
                                        resultText.value = passedCount.toString()
                                    }

                                    1 -> {
                                        indeText.value = "Failed"
                                        resultText.value = failedCount.toString()
                                    }

                                    2 -> {
                                        indeText.value = "Pending"
                                        resultText.value =
                                            (totalTests - (failedCount + passedCount)).toString()
                                    }

                                    else -> {
                                        Log.d("CardNumber","No number")
                                    }
                                }
                                TextFn(
                                    text = resultText.value,
                                    color = Color.Black,
                                    size = 28
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextFn(
                                    text = indeText.value,
                                    color = Color.Black,
                                    size = 18
                                )
                            }
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
                                    // Toggle the state of the clicked accordion
                                    val isCurrentlyExpanded = expandableState[hpIndex] ?: false
                                    expandableState.keys.forEach { key ->
                                        expandableState[key] = false
                                    }
                                    expandableState[hpIndex] = !isCurrentlyExpanded
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
                                        hRViewModel,
                                        vibrator
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
                                            hRViewModel = hRViewModel,
                                            vibrator = vibrator
                                        )
                                    }

                                    4 -> {
                                        CameraTestOptions(
                                            allTestResults,
                                            hRViewModel
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
                        navController
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
    navController: NavHostController
) {

    when (index) {

        0 -> {
            navController.navigate(route = "SwipeScreenTest")
        }

        1 -> {
                navController.navigate(route = "SingleTouch")
        }

        2 -> {
                navController.navigate(route = "MultiTouch")
        }

        3 -> {
                navController.navigate(route = "PinchToZoom")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SpeakerTestOptions(
    height: Dp,
    allTestResults: Map<String, Boolean>,
    hrViewModel: HRViewModel, vibrator: Vibrator
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
    SpeakerBottomSheet(hrViewModel, vibrator)
}

@Composable
@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
fun SpeakerBottomSheet(
    hRViewModel: HRViewModel,
    vibrator: Vibrator
) {
    val context = LocalContext.current
    val micTestResult = remember { mutableStateOf(false) }
    val speakTestResult = remember { mutableStateOf(false) }
    val xText = remember { mutableStateOf("Playing Music") }
    val speechRecStatus = remember {
        mutableStateOf(false)
    }
    val height = remember { mutableIntStateOf(0) }
    if (hRViewModel.btmSheetExpand.value) {
        ModalBottomSheet(
            onDismissRequest = {
                hRViewModel.textToDisplayState.value = false
                hRViewModel.btmSheetExpand.value = false
            },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(height.intValue.dp)
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
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Speaker_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
                            hRViewModel.MediaPlayerCtrl()
                            hRViewModel.TextToSpeakFn(context)
                            Spacer(modifier = Modifier.height(10.dp))
                            if (hRViewModel.toShowBtn.value) {
                                TextFn(
                                    text = hRViewModel.yText.value,
                                    color = Color.Black,
                                    size = 24
                                )
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
                                            hRViewModel.textToDisplayState.value = false
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
                    }

                    1 -> {
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.vibration_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
                        if (hRViewModel.vibrationTestResults.value) {
                            hRViewModel.UpdateResultAfterTest(
                                context = LocalContext.current,
                                testName = hRViewModel.vibrationTestName,
                                testResult = true
                            )
                            hRViewModel.textToDisplayState.value = false
                            hRViewModel.vibrationTestResults.value = false
                        }
                        TextFn(text = "Vibration Test", color = Color.Black, size = 24)
                            hRViewModel.VibrationCtrl(vibrator)
                        }
                    }

                    2 -> {
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Microphone_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
                            speechRecStatus.value = true
                            hRViewModel.MicTest(context, micTestResult, speechRecStatus)
                        }
                    }

                    3 -> {
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Ringtone_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
                            if (hRViewModel.ringtoneTestResults.value) {
                                hRViewModel.UpdateResultAfterTest(
                                    context = context,
                                    testName = hRViewModel.ringtoneTestName,
                                    testResult = true
                                )
                            }
                            TextFn(
                                text = "Playing Ringtone...!",
                                color = Color.Black,
                                size = 22
                            )
                            hRViewModel.RingtoneManager(context)
                        }
                    }

                    4 -> {
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Alarm_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
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
                    }

                    5 -> {
                        if (!hRViewModel.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Notification_Detail),
                                hRViewModel
                            )
                        } else {
                            height.intValue = 185
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
    allTestResults: Map<String, Boolean>,
    vibrator: Vibrator
) {
    val currentIndex = remember { mutableIntStateOf(99) }
    val btmSheetState = remember { mutableStateOf(false) }
    val sensorImageId = remember { mutableIntStateOf(0) }
    val sensorCardText = remember { mutableStateOf("Dummy") }
    if (btmSheetState.value) {
        SensorTestBottomSheet(
            state = btmSheetState,
            vM = hRViewModel,
            index = currentIndex.intValue,
            vibrator
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
                                            currentIndex.intValue = 0
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

@Composable
fun CameraTestOptions(
    allTestResults: Map<String, Boolean>,
    hRViewModel: HRViewModel
) {
    val context = LocalContext.current
    val camPermissionStatus = remember { mutableStateOf(false) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val imageReadyState = remember { mutableStateOf(false) }
    val camBtmSheetState = remember { mutableStateOf(false) }
    val camSelectIndex = remember { mutableIntStateOf(98) }
    val camPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted..!", Toast.LENGTH_SHORT).show()
                camPermissionStatus.value = true
            } else {
                Toast.makeText(context, "Permission Denied..!", Toast.LENGTH_SHORT).show()
                camPermissionStatus.value = false
            }
        }
    val camLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { ar ->
        if (ar.resultCode == Activity.RESULT_OK) {
            image.value = ar.data?.extras?.get("data") as Bitmap
            Log.d("BitCode", "Success: ${image.value}")
            imageReadyState.value = true
        } else {
            Log.d("BitCode", "Fail: ${ar.data}")
            imageReadyState.value = false
        }
    }
    val camTestImageId = remember { mutableIntStateOf(0) }
    val camTestCardText = remember { mutableStateOf("Dummy") }
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    LaunchedEffect(Unit) {
        camPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    DisposableEffect(Unit) {
        onDispose {
            camBtmSheetState.value = false
        }
    }
    if (imageReadyState.value) {
        camBtmSheetState.value = true
        CamBottomSheet(image, camBtmSheetState, hRViewModel, camSelectIndex.intValue)
    } else {
        Log.d("ImageReadyState", "Isn't ready yet")
    }
    if (!camPermissionStatus.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    enabled = true,
                    onClick = {
                        if (!camPermissionStatus.value) {
                            camPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Permission already granted..!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFn(text = "Please grant camera permission", color = Color.Black, size = 22)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(125.dp)
        )
        {
            items(count = 2) { index ->
                Card(
                    modifier = Modifier
                        .size(125.dp)
                        .padding(6.dp),
                    elevation = cardElevation(),
                    colors = CardDefaults.cardColors(
                        containerColor = hRViewModel.cardColorForCamTest(index, allTestResults),
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.5.dp, color = colorResource(hRViewModel.borderColor))
                ) {
                    when (index) {
                        0 -> {
                            camTestImageId.intValue = R.drawable.rear_camera
                            camTestCardText.value = "Back Camera"
                        }

                        else -> {
                            camTestImageId.intValue = R.drawable.front_camera
                            camTestCardText.value = "Front Camera"
                        }

                    }
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
                                                camLauncher.launch(intent)
                                                camSelectIndex.intValue = 0
                                                hRViewModel.insertResultBeforeTest(hRViewModel.rearCamTest)
                                            }

                                            else -> {
                                                camSelectIndex.intValue = 1
                                                hRViewModel.insertResultBeforeTest(hRViewModel.frontCamTest)
                                                intent.putExtra(
                                                    "android.intent.extras.CAMERA_FACING",
                                                    1
                                                )
                                                intent.putExtra(
                                                    "android.intent.extras.LENS_FACING_FRONT",
                                                    1
                                                )
                                                intent.putExtra(
                                                    "android.intent.extra.USE_FRONT_CAMERA",
                                                    true
                                                )
                                                camLauncher.launch(intent)
                                            }
                                        }
                                    }
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row {
                                CamCardImages(id = camTestImageId.intValue)
                            }

                            Spacer(modifier = Modifier.height(7.dp))

                            Row {
                                TextFn(
                                    text = camTestCardText.value,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamBottomSheet(
    image: MutableState<Bitmap?>,
    camBtmSheetState: MutableState<Boolean>,
    hRViewModel: HRViewModel,
    value: Int
) {
    val context = LocalContext.current
    val result = remember { mutableStateOf(false) }
    if (camBtmSheetState.value) {
        if (result.value) {
            when (value) {
                0 -> {
                    hRViewModel.UpdateResultAfterTest(
                        context = context,
                        testName = hRViewModel.rearCamTest,
                        testResult = true
                    )
                }

                1 -> {
                    hRViewModel.UpdateResultAfterTest(
                        context = context,
                        testName = hRViewModel.frontCamTest,
                        testResult = true
                    )
                }

                else -> {
                    Log.d("CamTestResultUpdateStatus", "No result..!")
                }
            }
            LaunchedEffect(Unit) {

                delay(100)
                camBtmSheetState.value = false
            }
            DisposableEffect(Unit) {
                onDispose {
                    result.value = false
                    hRViewModel.textToDisplayState.value = false
                }
            }
        }
        ModalBottomSheet(
            onDismissRequest = {
                hRViewModel.textToDisplayState.value = false
                camBtmSheetState.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(12.dp),
            scrimColor = Color.Transparent,
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                skipHiddenState = false
            ),
            containerColor = Color.White,
            content = {
                if (!hRViewModel.textToDisplayState.value) {
                    SplashScreen(displayText = stringResource(id = R.string.camera), hRViewModel)
                } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFn(
                        text = "Camera Test",
                        color = Color.Black,
                        size = 20
                    )
                    Divider(
                        modifier = Modifier.width(140.dp),
                        thickness = 1.5.dp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    image.value?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Captured Image..!",
                            modifier = Modifier.size(200.dp),
                            filterQuality = FilterQuality.High
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFn(
                        text = "Have you clicked the above image?",
                        color = Color.Black,
                        size = 18
                    )
                    Row {

                        Button(onClick = {
                            result.value = true
                        }) {
                            TextFn(text = "Yes", color = Color.White, size = 15)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = {
                            hRViewModel.textToDisplayState.value = false
                            camBtmSheetState.value = false
                        }) {
                            TextFn(text = "No", color = Color.White, size = 15)
                        }
                    }
                }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorTestBottomSheet(
    state: MutableState<Boolean>,
    vM: HRViewModel,
    index: Int,
    vibrator: Vibrator
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
    val lastUpdate = remember { mutableLongStateOf(0) }
    val updateDelay = remember { mutableLongStateOf(500) }
    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdate.longValue > updateDelay.longValue) {
                // Slowing down updation of sensor values.
                lastUpdate.longValue = currentTime
            when (event.sensor) {
                gyroscope -> {
                    rotationX.floatValue = event.values[0]
                    rotationY.floatValue = event.values[1]
                    rotationZ.floatValue = event.values[2]

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
            } else{
                Log.d("SensorEventDelay", "Delay ${currentTime - lastUpdate.longValue} ${updateDelay.longValue}")
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
        val height = remember {
            mutableIntStateOf(0)
        }
        ModalBottomSheet(
            onDismissRequest = {
                vM.textToDisplayState.value = false
                state.value = false
            },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(height.intValue.dp)
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
                        if (!vM.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Accelerometer_Detail),
                                vM
                            )
                        } else {
                            height.intValue = 205
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
                                vM.MinVib(vibrator)
                                state.value = false
                                Log.d(
                                    "SpeakerBtmSheetStatus",
                                    "${state.value} X: ${accelerationX.floatValue} Y: ${accelerationY.floatValue} Z: ${accelerationZ.floatValue}"
                                )
                                vM.textToDisplayState.value = false
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
                            LaunchedEffect(Unit) {
                                vM.textToDisplayState.value = false
                                delay(3000)
                                state.value = false
                            }
                        }
                        }
                    }

                    1 -> {
                        if (!vM.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Gyroscope_Detail),
                                vM
                            )
                        } else {
                            height.intValue = 205
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
                                vM.MinVib(vibrator)
                                state.value = false
                                vM.textToDisplayState.value = false
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
                            LaunchedEffect(Unit) {
                                delay(3000)
                                state.value = false
                                vM.textToDisplayState.value = false
                            }
                        }
                        }
                    }

                    else -> {
                        if (!vM.textToDisplayState.value) {
                            height.intValue = 500
                            SplashScreen(
                                displayText = stringResource(id = R.string.Light_Sensor),
                                vM
                            )
                        } else {
                            height.intValue = 205
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
                                   vM.MinVib(vibrator)
                                    vM.textToDisplayState.value = false
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
                                LaunchedEffect(Unit) {
                                    delay(3000)
                                    state.value = false
                                    vM.textToDisplayState.value = false
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