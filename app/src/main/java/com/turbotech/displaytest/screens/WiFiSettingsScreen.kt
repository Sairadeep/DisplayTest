package com.turbotech.displaytest.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.turbotech.displaytest.R
import com.turbotech.displaytest.components.TextFn
import com.turbotech.displaytest.components.TopAppBarFn
import com.turbotech.displaytest.viewModel.HRViewModel

@SuppressLint("MissingPermission")
@Suppress( "Deprecation")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun WifiScreen(navController: NavController, hrViewModel: HRViewModel) {
    val context = LocalContext.current
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ssid = wifiManager.scanResults.map { map ->
        map.SSID
    }.filter { filter ->
        filter.isNotEmpty()
    }.distinct()
    val state = remember { mutableStateOf(false) }
    val passwordInput = remember {
        mutableStateOf("")
    }
    val passwordShowState = remember { mutableStateOf(true) }
    val displayName = remember { mutableStateOf("") }
    val showIcon = remember {
        mutableIntStateOf(R.drawable.eye_24)
    }
    val wifiState = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (!hrViewModel.textToDisplayState.value) {
        SplashScreen(
            displayText = stringResource(id = R.string.wifi_Detail),
            hrViewModel
        )
    } else {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Scaffold(
            topBar = {
                TopAppBarFn(
                    text = "Wifi Settings",
                    navController = navController
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyColumn {
                    items(count = ssid.size) { item ->
                        val wifiName = ssid[item]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .padding(3.dp),
                            border = BorderStroke(
                                width = 1.2.dp,
                                color = Color.Black
                            )
                        ) {
                            if (state.value) {
                                ModalBottomSheet(
                                    onDismissRequest = { state.value = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(225.dp)
                                        .padding(10.dp)
                                        .offset(0.dp, (-300).dp),
                                    scrimColor = Color.Transparent,
                                    shape = RoundedCornerShape(30.dp),
                                    containerColor = Color.Red
                                )
                                {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            TextFn(
                                                text = displayName.value,
                                                color = Color.White,
                                                size = 18
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            TextField(
                                                value = passwordInput.value,
                                                onValueChange = { oVC ->
                                                    passwordInput.value = oVC
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    autoCorrect = false
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        keyboardController?.hide()
                                                    }
                                                ),
                                                placeholder = {
                                                    Text(
                                                        text = "Wi-Fi Password",
                                                        fontSize = 15.sp,
                                                        color = Color.DarkGray
                                                    )
                                                },
                                                trailingIcon = {
                                                    IconButton(onClick = {
                                                        passwordShowState.value =
                                                            !passwordShowState.value
                                                    }) {
                                                        Icon(
                                                            painter = painterResource(id = showIcon.intValue),
                                                            contentDescription = "To show password"
                                                        )
                                                    }
                                                },
                                                visualTransformation = if (passwordShowState.value) {
                                                    VisualTransformation.None
                                                } else {
                                                    PasswordVisualTransformation()
                                                },
                                                modifier = Modifier
                                                    .padding(
                                                        5.dp
                                                    )
                                                    .clip(
                                                        RoundedCornerShape(
                                                            12.dp
                                                        )
                                                    )
                                            )
                                            if (passwordShowState.value) showIcon.intValue =
                                                R.drawable.eye_24 else showIcon.intValue =
                                                R.drawable.eye_off
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 15.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Button(onClick = {
                                                wifiState.value = true
                                            }) {
                                                Text(
                                                    text = "Connect",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (wifiState.value && displayName.value.isNotEmpty() && passwordInput.value.isNotEmpty()) {
                                Wifictrl(
                                    wifiManager = wifiManager,
                                    displayName.value,
                                    passwordInput.value
                                )
                            } else {
                                Log.d("wifi_State", "Not connected")
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 9.dp, bottom = 5.dp, start = 6.dp)
                                    .clickable(
                                        onClick = {
                                            state.value = true
                                            Toast
                                                .makeText(
                                                    context,
                                                    "$item $wifiName",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            displayName.value = wifiName
                                        }
                                    )
                            ) {
                                TextFn(
                                    text = wifiName,
                                    color = Color.Black,
                                    size = 22
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun Wifictrl(wifiManager: WifiManager, ssid: String, password: String) {
    // Need to implement more, as of now it works on click of it.
    val networkSuggestions = listOf(
        WifiNetworkSuggestion.Builder()
//          .setSsid("TP-Link_Guest_D3A0_5G")
//          .setWpa2Passphrase("02255880")
            .setSsid(ssid).setWpa2Passphrase(password)
            .build()
    )
    wifiManager.addNetworkSuggestions(
        networkSuggestions
    )
}