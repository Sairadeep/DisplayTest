package com.turbotech.displaytest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.turbotech.displaytest.components.IconBtnFn
import com.turbotech.displaytest.components.TextFn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTouch(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title =
                { TextFn(text = "Multi Touch Test Page", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("HomePage")}) {
                        IconBtnFn()
                    }
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
                .fillMaxSize()
                .padding(it)
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextFn(text = "Multi Touch Test Page", color = Color.White)
        }
    }
}