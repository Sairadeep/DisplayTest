package com.turbotech.displaytest.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DisplayCardImages(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier
            .size(70.dp)
            .padding(top = 3.dp)
            .border(0.75.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(CircleShape),
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun ConnectivityCardImages(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier
            .size(70.dp)
            .padding(top = 3.dp)
            .border(0.75.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(CircleShape),
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun SpeakerCardImages(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier
            .size(70.dp)
            .padding(top = 3.dp)
            .border(0.75.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(CircleShape),
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit
    )
}


@Composable
fun SensorCardImages(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier
            .size(70.dp)
            .padding(top = 3.dp)
            .border(0.75.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(CircleShape),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun CamCardImages(id: Int) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = Modifier
            .size(70.dp)
            .padding(top = 3.dp)
            .border(0.75.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(CircleShape),
        alignment = Alignment.Center,
        contentScale = ContentScale.FillWidth
    )
}