package com.turbotech.displaytest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.turbotech.displaytest.viewModel.HRViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(displayText: String, hrViewModel: HRViewModel) {
    Surface {

        var textToDisplay by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(Unit) {
                for (i in 1..displayText.length) {
                    // Typewriter effect
                    textToDisplay = displayText.substring(0, i)
                    delay(100)
                }
            }
            Text(
                text = textToDisplay,
                fontSize = 30.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                lineHeight = 45.sp,
                textAlign = TextAlign.Center
            )
        }
        if (displayText.length == textToDisplay.length) {
            LaunchedEffect(Unit) {
                delay(2000)
                hrViewModel.textToDisplayState.value = true
            }
        } else {
            hrViewModel.textToDisplayState.value = false
        }
    }
}