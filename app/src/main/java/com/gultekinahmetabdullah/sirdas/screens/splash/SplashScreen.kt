package com.gultekinahmetabdullah.sirdas.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gultekinahmetabdullah.sirdas.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashScreenFinished: () -> Unit
) {

    val runningRate = 2000L
    val alpha = remember { Animatable(1f) }
    val velocity by remember { mutableFloatStateOf(0.01f) }
    var alphaValue by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(runningRate) {
        delay(runningRate)
        while (alphaValue > 0f) {
            alphaValue -= velocity
            alpha.animateTo(alphaValue)
        }
        onSplashScreenFinished()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .alpha(
                alpha.value
            ),
        color = Color.White
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sirdas),
                contentDescription = "Logo",
                modifier = Modifier.size(256.dp)
            )
        }
    }
}