package com.turbotech.displaytest.viewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun insertResult(result: DisplayEntities) =
        viewModelScope.launch {
            resultsRepo.insertResults(result)
        }

    fun updateResult(result: DisplayEntities) =
        viewModelScope.launch {
            resultsRepo.updateResults(result)
        }

    @Composable
    fun getResults() = results.collectAsState().value

    @Composable
    fun getSpecificTestName(): Int {
//         0 -> Swipe Screen Test
//         1 -> Single Touch Test
//         2 -> Multi Touch Test
//         3 -> Pinch To Zoom Test
//        99 -> No Test
        return when (getResults().size) {
            0 -> 99
            1 -> {
                when (getResults()[0].testName) {
                    swipeTestName -> return 0
                    singleTouchTestName -> return 1
                    multiTouchTestName -> return 2
                    pinchToZoomTestName -> return 3
                    else -> return 99
                }
            }

            else -> {
                when (getResults()[getResults().size - 1].testName) {
                    swipeTestName -> return 0
                    singleTouchTestName -> return 1
                    multiTouchTestName -> return 2
                    pinchToZoomTestName -> return 3
                    else -> return 99
                }
            }
        }
    }

    @Composable
    fun isSpecificTestStarted(): Boolean {
        return when (getResults().size) {
            0 -> false
            1 -> getResults()[0].isTestStarted
            else -> getResults()[getResults().size - 1].isTestStarted
        }
    }


    @Composable
    fun specificTestId(): UUID {
        return when (getResults().size) {
            0 -> UUID.randomUUID()
            1 -> getResults()[0].id
            else -> getResults()[getResults().size - 1].id
        }
    }

    @Composable
    fun specificTestResult(): Boolean {
        return when (getResults().size) {
            0 -> false
            1 -> getResults()[0].testResult
            else -> getResults()[getResults().size - 1].testResult
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

}