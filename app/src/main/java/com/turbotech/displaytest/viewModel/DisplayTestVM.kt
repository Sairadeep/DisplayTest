package com.turbotech.displaytest.viewModel

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.navigation.NavHostController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.cardElevation
import com.turbotech.displaytest.components.topAppBarColorCombo
import com.turbotech.displaytest.model.DisplayEntities
import com.turbotech.displaytest.repository.ResultsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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
    val boxState = mutableStateOf(false)
    val xPosition = mutableStateOf(0.dp)
    val yPosition = mutableStateOf(0.dp)
    val noOfClicks = mutableIntStateOf(0)
    val noC = mutableIntListOf()
    private val currentClicks = mutableIntStateOf(0)
    private val releaseState = mutableStateOf(false)

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
            // Rotation > 330 degrees and zoom up to 4 times
        }
        return rotation.floatValue > 180 && scale.floatValue > 4
    }

    @Composable
    fun UpdateResultAfterTest(context: Context, testName: String, navController: NavController) {
        Toast.makeText(context, "Test Completed", Toast.LENGTH_SHORT).show()
        updateResult(
            DisplayEntities(
                id = specificTestId(),
                testName = testName,
                isTestStarted = false,
                testResult = true
            )
        )
        navController.navigate("HomePage")
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
                navController = navController
            )
        }
    }

    @Composable
    fun TextBasedOnClicks() {
        if (noOfClicks.intValue <= 15) {
            val text = " Remaining Clicks : ${noOfClicks.intValue} / 15"
            TextFn(text = text, color = Color.Black)
        } else {
            val text = "Test Completed"
            TextFn(text = text, color = Color.Black)
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun DisplayTopAppBar(text: String, navController: NavController) {
        TopAppBar(
            title = { TextFn(text = text, color = Color.White) },
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
                color = Color.White
            )
            Log.d("currentClicks", "${currentClicks.intValue}")
            if (currentClicks.intValue > 2) {
                UpdateResultAfterTest(
                    context = LocalContext.current,
                    testName = multiTouchTestName,
                    navController = navController
                )
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

     private fun cardColor(
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
        else -> Color.LightGray
    }

    private fun cardText(index : Int, itemText : MutableState<String>){
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
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun LazyVerticalGridFn(
        navController: NavHostController,
        allTestResults: Map<String, Boolean>
    ) {
        val itemText = remember { mutableStateOf("Dummy") }
        val context = LocalContext.current

        LazyVerticalGrid(columns = GridCells.Adaptive(225.dp)) {
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
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(45.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        cardColor(index, allTestResults),
                        disabledContentColor = Color.Magenta
                    ),
                    enabled = true,
                    elevation = cardElevation()
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        cardText(index, itemText)
                        TextFn(text = itemText.value, color = Color.Black)
                    }
                }
            }
        }
    }
    
}