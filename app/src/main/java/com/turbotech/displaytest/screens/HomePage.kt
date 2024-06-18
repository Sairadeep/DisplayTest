package com.turbotech.displaytest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.topAppBarColorCombo
import com.turbotech.displaytest.viewModel.DisplayTestVM
import kotlinx.coroutines.delay

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
                displayTestVM.HomePageDesign(
                    navController = navController,
                    allTestResults = allTestResults
                )
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

