package com.turbotech.displaytest.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun TextFn(text: String, color: Color, size: Int) {
    Text(
        text = text,
        fontSize = size.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center
    )
}