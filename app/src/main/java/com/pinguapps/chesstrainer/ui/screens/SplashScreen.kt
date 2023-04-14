package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.ui.theme.ChessPurple
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeElapsed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChessPurple),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.splash), contentDescription = null)

        LaunchedEffect(Unit) {
            delay(1000)
            onTimeElapsed()
        }
    }
}