package com.turbotech.displaytest.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarFn(text: String, navController: NavController) {
    TopAppBar(
        title = { TextFn(text = text, color = Color.White, size = 22) },
        navigationIcon = {
            IconBtnFn(navController = navController)
        },
        colors = topAppBarColorCombo(),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}