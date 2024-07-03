package com.turbotech.displaytest.viewModel

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.data.BluetoothUiState
import com.turbotech.displaytest.data.domain.BluetoothController
import com.turbotech.displaytest.model.DisplayEntities
import com.turbotech.displaytest.repository.ResultsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HRViewModel @Inject constructor(
    private val resultsRepo: ResultsRepo,
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private val _results = MutableStateFlow<List<DisplayEntities>>(emptyList())
    private val results = _results.asStateFlow()
    private val pageLoad = mutableStateOf(true)
    val swipeTestName: String = "Swipe_Screen_Test"
    val singleTouchTestName: String = "Single_Touch_Test"
    val multiTouchTestName: String = "Multi_Touch_Test"
    val pinchToZoomTestName: String = "Pinch_To_Zoom_Test"
    val speakTestName: String = "Speaker_Test"
    val vibrationTestName: String = "Vibration_Test"
    private val micTestName: String = "Microphone_Test"
    val ringtoneTestName: String = "Ringtone_Test"
    val alarmTestName: String = "Alarm_Test"
    val notificationTestName: String = "Notification_Test"
    private val wifiTestName: String = "Wifi_Test"
    private val bluetoothTestName: String = "Bluetooth_Test"
    private val locationTestName: String = "Location_Test"
    val accelerometerSensorTestName: String = "Accelerometer_Sensor_Test"
    val gyroscopeSensorTestName: String = "Gyroscope_Sensor_Test"
    val lightSensorTestName: String = "Light_Sensor_Test"
    val boxState = mutableStateOf(false)
    val xPosition = mutableStateOf(0.dp)
    val yPosition = mutableStateOf(0.dp)
    val xId = mutableIntStateOf(99)
    val noOfClicks = mutableIntStateOf(0)
    val noC = mutableIntListOf()
    val borderColor = R.color.orange
    val btmSheetExpand = mutableStateOf(false)
    val toShowBtn = mutableStateOf(false)
    val yText = mutableStateOf("Did you hear the music with varied pitch?")
    private lateinit var timer: CountDownTimer
    val ttsStatus = mutableStateOf(false)
    val vibrationTestResults = mutableStateOf(false)
    val ringtoneTestResults = mutableStateOf(false)
    val alarmTestResults = mutableStateOf(false)
    val notificationTestResults = mutableStateOf(false)
    private val _state = MutableStateFlow(BluetoothUiState())

    //    multiple  flow to a single flow and then to a stateflow
    val state = combine(
        bluetoothController.scannedList,
        bluetoothController.pairedList,
        _state
    ) { scannedList, pairedList, state ->
        state.copy(
            scannedDevices = scannedList,
            pairedDevices = pairedList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )


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

    fun startScan() {
        Log.d("HRViewModelStartScan","Start Scan")
        bluetoothController.startScan()
    }

    fun stopScan() {
        Log.d("HRViewModelStopScan","Stop Scan")
        bluetoothController.stopScan()
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
        // Rotation > 180 degrees and zoom up to 4 times
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

    fun connectivityCardColors(
        index: Int,
        allTestResults: Map<String, Boolean>,
    ) = when {
        index == 0 && allTestResults[wifiTestName] == true -> Color.Green
        index == 1 && allTestResults[bluetoothTestName] == true -> Color.Green
        index == 1 && allTestResults[bluetoothTestName] == false -> Color.Red
        index == 2 && allTestResults[locationTestName] == true -> Color.Green
        index == 2 && allTestResults[locationTestName] == false -> Color.Red
        index == 0 && allTestResults[wifiTestName] == false -> Color.Red
        else -> Color.White
    }

    fun cardColorForST(
        index: Int,
        allTestResults: Map<String, Boolean>,
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

    fun cardColorForSensorTest(
        index: Int,
        allTestResults: Map<String, Boolean>,
    ) = when {
        index == 0 && allTestResults[accelerometerSensorTestName] == true -> Color.Green
        index == 0 && allTestResults[accelerometerSensorTestName] == false -> Color.Red
        index == 1 && allTestResults[gyroscopeSensorTestName] == true -> Color.Green
        index == 1 && allTestResults[gyroscopeSensorTestName] == false -> Color.Red
        index == 2 && allTestResults[lightSensorTestName] == true -> Color.Green
        index == 2 && allTestResults[lightSensorTestName] == false -> Color.Red
        else -> {
            Color.White
        }
    }

     fun cardColorForDT(
        index: Int,
        allTestResults: Map<String, Boolean>,
        hRViewModel: HRViewModel
    ) = when {
        index == 0 && allTestResults[hRViewModel.swipeTestName] == true -> Color.Green
        index == 0 && allTestResults[hRViewModel.swipeTestName] == false -> Color.Red
        index == 1 && allTestResults[hRViewModel.singleTouchTestName] == true -> Color.Green
        index == 1 && allTestResults[hRViewModel.singleTouchTestName] == false -> Color.Red
        index == 2 && allTestResults[hRViewModel.multiTouchTestName] == true -> Color.Green
        index == 2 && allTestResults[hRViewModel.multiTouchTestName] == false -> Color.Red
        index == 3 && allTestResults[hRViewModel.pinchToZoomTestName] == true -> Color.Green
        index == 3 && allTestResults[hRViewModel.pinchToZoomTestName] == false -> Color.Red
        else -> {
            Color.White
        }
    }

    @Composable
    fun RingtoneManager(context: Context) {

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
    fun VibrationCtrl(vibrator: Vibrator) {

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
    fun TextToSpeakFn(context: Context) {
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
                   /*Log.d(
                       "Recognized_Text",
                       "Current Value : ${speechResults[0]}"
                   )*/
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