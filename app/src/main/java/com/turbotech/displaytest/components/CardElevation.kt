package com.turbotech.displaytest.components

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun cardElevation() = CardDefaults.elevatedCardElevation(
    defaultElevation = 5.dp,
    pressedElevation = 4.dp,
    focusedElevation = 4.dp,
    hoveredElevation = 4.dp
)