package com.example.musicapp.ui.components.player

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun InfoChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label, 
            color = Color.White.copy(alpha = 0.5f), 
            fontSize = 11.sp
        )
        Text(
            text = value, 
            color = Color.White, 
            fontSize = 14.sp, 
            fontWeight = FontWeight.Bold
        )
    }
}
