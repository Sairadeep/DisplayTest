package com.turbotech.displaytest.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.collection.mutableIntListOf
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.CardImage
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.cardElevation
import com.turbotech.displaytest.components.topAppBarColorCombo
import com.turbotech.displaytest.model.DisplayEntities
import com.turbotech.displaytest.repository.ResultsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DisplayTestVM @Inject constructor(private val resultsRepo: ResultsRepo) : ViewModel() {

    private val _results = MutableStateFlow<List<DisplayEntities>>(emptyList())
    private val results = _results.asStateFlow()
    private val pageLoad = mutableStateOf(true)
    val swipeTestName: String = "Swipe_Screen_Test"
    val singleTouchTestName: String = "Single_Touch_Test"
    val multiTouchTestName: String = "Multi_Touch_Test"
    val pinchToZoomTestName: String = "Pinch_To_Zoom_Test"
    private val speakTestName: String = "Speaker_Test"
    private val vibrationTestName: String = "Vibration_Test"
    private val micTestName: String = "Microphone_Test"
    private val ringtoneTestName: String = "Ringtone_Test"
    private val alarmTestName: String = "Alarm_Test"
    private val notificationTestName: String = "Notification_Test"
    val boxState = mutableStateOf(false)
    val xPosition = mutableStateOf(0.dp)
    val yPosition = mutableStateOf(0.dp)
    val noOfClicks = mutableIntStateOf(0)
    val noC = mutableIntListOf()
    private val speakerImageId = mutableIntStateOf(0)
    private val displayImageId = mutableIntStateOf(0)
    private val cardText = mutableStateOf("Dummy")
    private val xId = mutableIntStateOf(99)
    private val heightOfIt = 250.dp
    private val borderColor = R.color.orange
    private val currentClicks = mutableIntStateOf(0)
    private val releaseState = mutableStateOf(false)
    private val btmSheetExpand = mutableStateOf(false)
    private lateinit var timer: CountDownTimer
    private val xText = mutableStateOf("Playing Music")
    private val yText = mutableStateOf("Did you hear the music with varied pitch?")
    private val toShowBtn = mutableStateOf(false)
    private val speakTestResult = mutableStateOf(false)
    private val ttsStatus = mutableStateOf(false)
    private val expandableState = mutableStateMapOf<Int, Boolean>()
    private val vibrationTestResults = mutableStateOf(false)
    private val ringtoneTestResults = mutableStateOf(false)
    private val alarmTestResults = mutableStateOf(false)
    private val notificationTestResults = mutableStateOf(false)

    init {
        viewModelScope.launch {
            resultsRepo.getResultsData().distinctUntilChanged().collect { resultDATA ->
                if (resultDATA.isNotEmpty()) {
                    _results.value = resultDATA
                    pageLoad.value = false
                    Log.d("resultsData", "${resultDATA.size}")
                } else {
                    Log.d("DisplayTestVM", "no results")
                    pageLoad.value = true
                }
            }
        }
    }

    private fun insertResult(result: DisplayEntities) =
        viewModelScope.launch {
            resultsRepo.insertResults(result)
        }

    private fun updateResult(result: DisplayEntities) =
        viewModelScope.launch {
            resultsRepo.updateResults(result)
        }

    @Composable
    private fun getResults() = results.collectAsState().value

    @Composable
    private fun specificTestId(): UUID {
        return when (getResults().size) {
            0 -> UUID.randomUUID()
            1 -> getResults()[0].id
            else -> getResults()[getResults().size - 1].id
        }
    }

    @Composable
    fun getAllTestResults() : Map<String, Boolean> {
       val testResult = mutableMapOf<String, Boolean>()
        getResults().forEach {de ->
            testResult[de.testName] = de.testResult
        }
        return testResult
    }

    @Composable
    fun zoomTransformableState(): Boolean {
        val scale = remember {
            mutableFloatStateOf(1f)
        }
        val rotation = remember {
            mutableFloatStateOf(1f)
        }
        var offset by remember {
            mutableStateOf(Offset.Zero)
        }
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
        }
        // Rotation > 330 degrees and zoom up to 4 times
        return rotation.floatValue > 180 && scale.floatValue > 4
    }

    @Composable
    fun UpdateResultAfterTest(context: Context, testName: String, testResult: Boolean) {
        Toast.makeText(context, "Test Completed", Toast.LENGTH_SHORT).show()
        updateResult(
            DisplayEntities(
                id = specificTestId(),
                testName = testName,
                isTestStarted = false,
                testResult = testResult
            )
        )
    }

    fun insertResultBeforeTest(testName: String) {
        insertResult(
            DisplayEntities(
                testName = testName,
                isTestStarted = true,
                testResult = false
            )
        )
    }

    @Composable
    fun ClickSelectionBox(navController: NavController) {
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
                        .background(Color.Green, shape = RoundedCornerShape(30))
                )
            }
        } else {
            UpdateResultAfterTest(
                context = LocalContext.current,
                testName = singleTouchTestName,
                testResult = true
            )
            navController.navigate("HomePage")
        }
    }

    @Composable
    fun TextBasedOnClicks() {
        if (noOfClicks.intValue <= 15) {
            val text = " Remaining Clicks : ${noOfClicks.intValue} / 15"
            TextFn(text = text, color = Color.Black, size = 18)
        } else {
            val text = "Test Completed"
            TextFn(text = text, color = Color.Black, size = 18)
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun DisplayTopAppBar(text: String, navController: NavController) {
        TopAppBar(
            title = { TextFn(text = text, color = Color.White, size = 22) },
            navigationIcon = {
                IconBtnFn(navController = navController)
            },
            colors = topAppBarColorCombo(),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }

    fun releaseStateOnMotionEvent(motionEvent: MotionEvent) {
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
    fun MultiTestResultDisplayText(navController: NavController) {
        if (releaseState.value) {
            TextFn(
                text = "Multi Touch has ${currentClicks.intValue} touches",
                color = Color.White,
                size = 18
            )
            Log.d("currentClicks", "${currentClicks.intValue}")
            if (currentClicks.intValue > 2) {
                UpdateResultAfterTest(
                    context = LocalContext.current,
                    testName = multiTouchTestName,
                    testResult = true
                )
                navController.navigate("HomePage")
            }
        }
    }

    private fun subPagesNavigation(
        index: Int,
        navController: NavHostController,
        context: Context,
        allTestResults: Map<String, Boolean>
    ) {
        when (index) {

            0 -> {
                navController.navigate(route = "SwipeScreenTest")
            }

            1 -> {
                if (allTestResults[swipeTestName] != null) {
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
                if (allTestResults[singleTouchTestName] != null) {
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
                if (allTestResults[multiTouchTestName] != null) {
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

    private fun cardColorForDT(
        index: Int,
        allTestResults: Map<String, Boolean>
    ) = when {
        index == 0 && allTestResults[swipeTestName] == true -> Color.Green
        index == 0 && allTestResults[swipeTestName] == false -> Color.Red
        index == 1 && allTestResults[singleTouchTestName] == true -> Color.Green
        index == 1 && allTestResults[singleTouchTestName] == false -> Color.Red
        index == 2 && allTestResults[multiTouchTestName] == true -> Color.Green
        index == 2 && allTestResults[multiTouchTestName] == false -> Color.Red
        index == 3 && allTestResults[pinchToZoomTestName] == true -> Color.Green
        index == 3 && allTestResults[pinchToZoomTestName] == false -> Color.Red
         else -> {Color.White}
    }

    private fun cardColorForST(
        index: Int,
        allTestResults: Map<String, Boolean>
    ) = when {
        index == 0 && allTestResults[speakTestName] == true -> Color.Green
        index == 0 && allTestResults[speakTestName] == false -> Color.Red
        index == 1 && allTestResults[vibrationTestName] == true -> Color.Green
        index == 1 && allTestResults[vibrationTestName] == false -> Color.Red
        index == 2 && allTestResults[micTestName] == true -> Color.Green
        index == 2 && allTestResults[micTestName] == false -> Color.Red
        index == 3 && allTestResults[ringtoneTestName] == true -> Color.Green
        index == 3 && allTestResults[ringtoneTestName] == false -> Color.Red
        index == 4 && allTestResults[alarmTestName] == true -> Color.Green
        index == 4 && allTestResults[alarmTestName] == false -> Color.Red
        index == 5 && allTestResults[notificationTestName] == true -> Color.Green
        index == 5 && allTestResults[notificationTestName] == false -> Color.Red
        else -> {
            Color.White
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun HomePageDesign(
        navController: NavHostController,
        allTestResults: Map<String, Boolean>,
    ) {
        val context = LocalContext.current
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            // Need to change this position to mic test
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE),
                    99
                )
            }
            items(count = 5) { index ->
                val rotateValue by animateFloatAsState(
                    targetValue = if (expandableState[index] == true) 180f else 0f,
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
                            when (index) {
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
                                        color = colorResource(borderColor),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick =
                                    {
                                        expandableState[index] = expandableState[index] != true
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
                        expandableState[index]?.let {
                            AnimatedVisibility(visible = it) {
                                Column(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .border(
                                            width = 1.dp,
                                            color = colorResource(id = borderColor),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                ) {
                                    when (index) {
                                        0 -> {
                                            DisplayTestOptions(
                                                navController = navController,
                                                allTestResults = allTestResults,
                                                height = heightOfIt
                                            )
                                        }

                                        1 -> SpeakerTestOptions(
                                            heightOfIt,
                                            allTestResults
                                        )

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
    fun DisplayTestOptions(
        navController: NavHostController,
        allTestResults: Map<String, Boolean>,
        height: Dp
    ) {
        val itemText = remember { mutableStateOf("Dummy") }
        val context = LocalContext.current
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
                            allTestResults
                        )
                    },
                    modifier = Modifier
                        .size(125.dp)
                        .padding(6.dp),
                    border = BorderStroke(1.5.dp, color = colorResource(borderColor)),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColorForDT(index, allTestResults),
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

                                3 -> {
                                    displayImageId.intValue = R.drawable.pinch
                                    itemText.value = "Pinch"
                                }
                            }
                            Row {
                                CardImage(id = displayImageId.intValue)
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

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun SpeakerTestOptions(height: Dp, allTestResults: Map<String, Boolean>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            Modifier.height(height)
        ) {
            items(count = 6) { index ->
                when (index) {
                    0 -> {
                        speakerImageId.intValue = R.drawable.speaker1
                        cardText.value = "Speaker"
                    }

                    1 -> {
                        speakerImageId.intValue = R.drawable.vibration
                        cardText.value = "Vibration"
                    }

                    2 -> {
                        speakerImageId.intValue = R.drawable.mic_24
                        cardText.value = "Microphone"
                    }

                    3 -> {
                        speakerImageId.intValue = R.drawable.ringtone
                        cardText.value = "Ringtone"
                    }

                    4 -> {
                        speakerImageId.intValue = R.drawable.alarm_on_24
                        cardText.value = "Alarm"
                    }

                    else -> {
                        speakerImageId.intValue = R.drawable.notifications_24
                        cardText.value = "Notification"
                    }

                }
                Card(
                    modifier = Modifier
                        .size(125.dp)
                        .padding(6.dp),
                    elevation = cardElevation(),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColorForST(index, allTestResults),
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.5.dp, color = colorResource(borderColor))
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
                                        btmSheetExpand.value = true
                                        xId.intValue = index
                                    }
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row {
                                CardImage(speakerImageId.intValue)
                            }

                            Spacer(modifier = Modifier.height(7.dp))

                            Row {
                                TextFn(text = cardText.value, color = Color.Black, size = 16)
                            }
                        }
                    }
                }
            }
        }
        SpeakerBottomSheet()
    }

    @Composable
    @RequiresApi(Build.VERSION_CODES.S)
    @Suppress("DEPRECATION")
    @OptIn(ExperimentalMaterial3Api::class)
    private fun SpeakerBottomSheet() {
        val context = LocalContext.current
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (btmSheetExpand.value) {
            ModalBottomSheet(
                onDismissRequest = { btmSheetExpand.value = false },
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
                containerColor = colorResource(id = borderColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /**
                     * Need to verify that the device volume isn't set to minimum or muted.
                     */

                    when (xId.intValue) {
                        0 -> {
                            MediaPlayerCtrl()
                            TextToSpeakFn(LocalContext.current)
                            Spacer(modifier = Modifier.height(10.dp))
                            if (toShowBtn.value) {
                                TextFn(text = yText.value, color = Color.Black, size = 24)
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
                                            UpdateResultAfterTest(
                                                context = LocalContext.current,
                                                testName = speakTestName,
                                                testResult = true
                                            )
                                            btmSheetExpand.value = false
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
                                        btmSheetExpand.value = false
                                        ttsStatus.value = false
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
                            if (vibrationTestResults.value) {
                                UpdateResultAfterTest(
                                    context = LocalContext.current,
                                    testName = vibrationTestName,
                                    testResult = true
                                )
                                vibrationTestResults.value = false
                            }
                            TextFn(text = "Vibration Test", color = Color.Black, size = 24)
                            VibrationCtrl(vibrator)
                        }

                        2 -> {
                            Text(
                                text = "How are the things going? \n \n Speak out the above text",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            // Will handle this later
                            SpeechRecZ()
                        }

                        3 -> {
                            if (ringtoneTestResults.value) {
                                UpdateResultAfterTest(
                                    context = context,
                                    testName = ringtoneTestName,
                                    testResult = true
                                )
                                ringtoneTestResults.value = false
                            }
                            TextFn(
                                text = "Playing Ringtone...!",
                                color = Color.Black,
                                size = 22
                            )
                            RingtoneManager(context)
                        }

                        4 -> {
                            if (alarmTestResults.value) {
                                UpdateResultAfterTest(
                                    context = context,
                                    testName = alarmTestName,
                                    testResult = true
                                )
                                alarmTestResults.value = false
                            }
                            TextFn(
                                text = "Playing Alarm Tone...!",
                                color = Color.Black,
                                size = 22
                            )
                            RingtoneManager(context)
                        }

                        5 -> {
                            if (notificationTestResults.value) {
                                UpdateResultAfterTest(
                                    context = context,
                                    testName = notificationTestName,
                                    testResult = true
                                )
                                notificationTestResults.value = false
                            }

                            TextFn(
                                text = "Playing Notification Tone...!",
                                color = Color.Black,
                                size = 22
                            )
                            RingtoneManager(context)
                        }

                        else -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Dummy ${xId.intValue}",
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

    @Composable
    private fun RingtoneManager(context: Context) {

        val ringtoneType: Int = when (xId.intValue) {

            3 -> RingtoneManager.TYPE_RINGTONE
            4 -> RingtoneManager.TYPE_ALARM
            5 -> RingtoneManager.TYPE_NOTIFICATION
            else -> RingtoneManager.TYPE_ALL

        }

        val ringtoneUri =
            RingtoneManager.getDefaultUri(ringtoneType)
        val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)

        LaunchedEffect(Unit) {

            when (xId.intValue) {

                3 -> insertResultBeforeTest(ringtoneTestName)
                4 -> insertResultBeforeTest(alarmTestName)
                5 -> insertResultBeforeTest(notificationTestName)
                else -> {
                    Log.d("Current_Test", "No, insertion of data ${xId.intValue}")
                }

            }
            ringtone.play()
            delay(4000)
            when (xId.intValue) {

                3 -> ringtoneTestResults.value = true
                4 -> alarmTestResults.value = true
                5 -> notificationTestResults.value = true
                else -> {
                    Log.d("Current_Tone", "No, tone ${xId.intValue}")
                }

            }
            delay(250)
            btmSheetExpand.value = false
        }

        DisposableEffect(Unit) {
            onDispose {
                ringtone.stop()
                ringtoneTestResults.value = false
                alarmTestResults.value = false
                notificationTestResults.value = false
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    private fun VibrationCtrl(vibrator: Vibrator) {

        LaunchedEffect(Unit) {
            insertResultBeforeTest(vibrationTestName)
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    5000,
                    VibrationEffect.EFFECT_HEAVY_CLICK
                )
            )
            delay(5800)
            vibrationTestResults.value = true
            delay(250)
            btmSheetExpand.value = false
        }

        DisposableEffect(Unit) {
            onDispose {
                vibrator.cancel()
                vibrationTestResults.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun MediaPlayerCtrl() {
        val context = LocalContext.current
        val pitchValue = remember { mutableFloatStateOf(1f) }
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm1)
        val playbackParam = PlaybackParams()

        LaunchedEffect(Unit) {
            mediaPlayer.start()
            insertResultBeforeTest(speakTestName)
            timer.start()
        }

        DisposableEffect(Unit) {
            onDispose {
                mediaPlayer.pause()
                timer.cancel()
                pitchValue.floatValue = 1f
                toShowBtn.value = false
                ttsStatus.value = false
            }
        }

        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if ((millisUntilFinished / 1000) % 2 == 0L) {
                    pitchValue.floatValue += 1
                    playbackParam.speed = 1f
                    playbackParam.pitch = pitchValue.floatValue
                    mediaPlayer.playbackParams = playbackParam
                    Log.d("CurrentPitch", "${mediaPlayer.playbackParams.pitch}")
                    toShowBtn.value = false
                    ttsStatus.value = false
                }
            }

            override fun onFinish() {
                pitchValue.floatValue = 1f
                mediaPlayer.pause()
                ttsStatus.value = true
                toShowBtn.value = true
            }

        }
        mediaPlayer.setOnCompletionListener {
            Log.d("MediaPlayerSetUp", "Completed")
        }
        mediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            Log.d("MediaPlayerSetUp", "Buffering $mp. $percent")
        }
        mediaPlayer.setOnDrmConfigHelper { drm ->
            Log.d("MediaPlayerSetUp", "DrmConfig $drm")
        }
        mediaPlayer.setOnDrmPreparedListener { mp, status ->
            Log.d("MediaPlayerSetUp", "DrmPrepared $mp $status")
        }
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.d("MediaPlayerSetUp", "Error $mp $what $extra")
            mediaPlayer.release()
            false
        }
        mediaPlayer.setOnInfoListener { mp, what, extra ->
            Log.d("MediaPlayerSetUp", "Info $mp $what $extra")
            false
        }
        mediaPlayer.setOnSeekCompleteListener { scl ->
            Log.d("MediaPlayerSetUp", "SeekComplete $scl")
        }
        mediaPlayer.setOnMediaTimeDiscontinuityListener { mp, mts ->
            Log.d(
                "MediaPlayerSetUp",
                "MediaTimeDiscontinuity ${mp.isPlaying} ${mts.mediaClockRate}"
            )
        }
    }

    @Composable
    private fun TextToSpeakFn(context: Context) {
        lateinit var textToSpeech: TextToSpeech
        if (ttsStatus.value) {
            textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val speakResult =
                        textToSpeech.setLanguage(Locale.getDefault())
                    if (speakResult == TextToSpeech.LANG_MISSING_DATA || speakResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("textToSpeechStatus", "Language not supported..!")
                    }
                    textToSpeech.speak(
                        yText.value,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                }
            }
        }
    }

    @Composable
    fun SpeechRecZ() {
       val context = LocalContext.current
       val recognizerIntent1 = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
       val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(
           context
       )
       recognizerIntent1.putExtra(
           RecognizerIntent.EXTRA_LANGUAGE_MODEL,
           RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
       )
       recognizerIntent1.putExtra(
           RecognizerIntent.EXTRA_LANGUAGE,
           Locale.getDefault()
       )

       speechRecognizer.setRecognitionListener(object :
           RecognitionListener {

           override fun onReadyForSpeech(params: Bundle?) {
               Log.d("OnReadyForSpeech", "User Ready for speaking.")
           }

           override fun onBeginningOfSpeech() {
               Log.d(
                   "onBeginningOfSpeech",
                   "The user has started to speak."
               )
           }

           override fun onRmsChanged(rmsdB: Float) {
               //   Log.d("OnRmsChanged", "Change in the level of sound")
           }

           override fun onBufferReceived(buffer: ByteArray?) {
               Log.d(
                   "OnBufferReceived",
                   "More sound has been received."
               )
           }

           override fun onEndOfSpeech() {
               Log.d(
                   "onEndOfSpeech",
                   "Called after the user stops speaking."
               )
               speechRecognizer.stopListening()
           }

           override fun onError(error: Int) {
               Log.d(
                   "OnError",
                   "An network or recognition error occurred."
               )
           }

           override fun onResults(results: Bundle?) {
               Log.d("Results", "Results")
               val speechResults =
                   results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
               if (!speechResults.isNullOrEmpty()) {
                   speechRecognizer.stopListening()
                   Log.d(
                       "Recognized_Text",
                       "Current Value : ${speechResults[0]}"
                   )
               }
           }

           override fun onPartialResults(partialResults: Bundle?) {
               Log.d(
                   "OnPartialResults",
                   "Called when partial recognition results are available."
               )
           }

           override fun onEvent(eventType: Int, params: Bundle?) {
               Log.d(
                   "OnEvent",
                   "Reserved for adding future events $eventType"
               )
           }
       })
       speechRecognizer.startListening(recognizerIntent1)
    }

}
