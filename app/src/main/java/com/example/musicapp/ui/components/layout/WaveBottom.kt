package com.example.musicapp.ui.components.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.theme.AccentOrange
import com.example.musicapp.ui.theme.BgMedium

@Composable
fun WaveBottom(offset: Float, horizontalShift: Float) {
    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val w = size.width
        val h = size.height
        val shift = offset * 80f

        val backPath = Path().apply {
            moveTo(0f, h)
            lineTo(w, h)
            lineTo(w, h * 0.45f)
            quadraticTo(w * 0.75f + horizontalShift, h * -0.1f + shift, w * 0.5f, h * 0.35f)
            quadraticTo(w * 0.25f - horizontalShift, h * 0.8f - shift, 0f, h * 0.25f)
            close()
        }
        drawPath(backPath, BgMedium)

        val frontPath = Path().apply {
            moveTo(0f, h)
            lineTo(w, h)
            lineTo(w, h * 0.65f)
            quadraticTo(w * 0.75f - horizontalShift, h * 0.1f - shift, w * 0.5f, h * 0.55f)
            quadraticTo(w * 0.25f + horizontalShift, h * 0.95f + shift, 0f, h * 0.45f)
            close()
        }
        drawPath(frontPath, AccentOrange)
    }
}
