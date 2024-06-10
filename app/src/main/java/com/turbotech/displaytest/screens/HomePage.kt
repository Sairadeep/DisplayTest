package com.turbotech.displaytest.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.viewModel.DisplayTestVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController, displayTestVM: DisplayTestVM) {
    val context = LocalContext.current
    val itemText = remember { mutableStateOf("Dummy") }
    val navigationSelectedValue = remember { mutableIntStateOf(99) }
    val resultsNow = displayTestVM.results.collectAsState().value
    if (resultsNow.isNotEmpty()) {
        Log.d("DisplayTestVM", "XYZ: ${resultsNow[0].singleTouchTestResult}")
    }

    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                       TextFn(text = "Display Test", color = Color.White)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.purple_200))
                    .fillMaxSize()
                    .padding(it), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(columns = GridCells.Adaptive(225.dp)) {
                    items(count = 4) { index ->
                        Card(
                            onClick = {
                                Toast.makeText(context, "Clicked $index", Toast.LENGTH_SHORT).show()
                                navigationSelectedValue.intValue = index
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(5.dp),
                            shape = RoundedCornerShape(45.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black,
                                disabledContentColor = Color.Magenta
                            ),
                            enabled = true,
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 5.dp,
                                pressedElevation = 4.dp,
                                focusedElevation = 4.dp,
                                hoveredElevation = 4.dp
                            )
                        ) {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                when (index) {

                                    0 -> {
                                        itemText.value = "Swipe Screen Test"
                                    }

                                    1 -> {
                                        itemText.value = "Single Touch"
                                    }

                                    2 -> {
                                        itemText.value = "Multi Touch"
                                    }

                                    3 -> {
                                        itemText.value = "Still to Decide"
                                    }

                                    else -> {
                                        itemText.value = "Dummy"
                                    }
                                }

                                TextFn(text = itemText.value, color = Color.Black)

                                when (navigationSelectedValue.intValue) {
                                    0 -> {
                                        navController.navigate(route = "SwipeScreenTest")
                                    }

                                    1 -> {
                                        navController.navigate(route = "SingleTouch")
                                    }

                                    2 -> {
                                        navController.navigate(route = "MultiTouch")
                                    }

//                                    3 -> {
//                                        navController.navigate(route = "StillToDecide")
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
