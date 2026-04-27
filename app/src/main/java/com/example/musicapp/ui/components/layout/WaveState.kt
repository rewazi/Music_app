package com.example.musicapp.ui.components.layout

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

data class WaveAnimationState(
    val topOffset: State<Float>,
    val topHorizontal: State<Float>,
    val bottomOffset: State<Float>,
    val bottomHorizontal: State<Float>
)

@Composable
fun rememberWaveAnimationState(): WaveAnimationState {
    val infiniteTransition = rememberInfiniteTransition(label = "sharedWaveTransition")
    
    val topOffset = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "waveTopOffset"
    )
    val topHorizontal = infiniteTransition.animateFloat(
        initialValue = -60f, targetValue = 60f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "waveTopHorizontal"
    )
    
    val bottomOffset = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "waveBottomOffset"
    )
    val bottomHorizontal = infiniteTransition.animateFloat(
        initialValue = 60f, targetValue = -60f,
        animationSpec = infiniteRepeatable(animation = tween(3500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "waveBottomHorizontal"
    )

    return remember(topOffset, topHorizontal, bottomOffset, bottomHorizontal) {
        WaveAnimationState(topOffset, topHorizontal, bottomOffset, bottomHorizontal)
    }
}
