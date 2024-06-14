package com.turbotech.displaytest.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun IconBtnFn(navController: NavController) {
    IconButton(onClick = {
        navController.navigate("HomePage")
    }) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Go Back",
            tint = Color.White
        )
    }
}

