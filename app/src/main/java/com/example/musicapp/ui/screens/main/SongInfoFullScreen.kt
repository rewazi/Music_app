package com.example.musicapp.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.ui.components.player.InfoChip

@Composable
fun SongInfoFullScreen(onClose: () -> Unit) {
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offsetY.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2D0B20), Color(0xFF4A1535))
                )
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (offsetY > 120f) {
                            onClose()
                        }
                        offsetY = 0f
                    },
                    onDragCancel = { offsetY = 0f },
                    onVerticalDrag = { _, dragAmount ->
                        if (offsetY + dragAmount >= 0f) {
                            offsetY += dragAmount / density
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    "Now Playing",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFD95D39)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Song Title",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Singer Name",
                        color = Color(0xFFD95D39),
                        fontSize = 16.sp
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color(0xFFE8622A),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))


            var progress by remember { mutableStateOf(0.35f) }
            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFE8622A),
                    activeTrackColor = Color(0xFFE8622A),
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1:23", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                Text("3:45", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))


            var isPlaying by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.material_symbols_repeat),
                        contentDescription = "Repeat",
                        tint = Color(0xFFD95D39),
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(52.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next),
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE8622A))
                        .clickable { isPlaying = !isPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(52.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next1),
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Volume",
                        tint = Color(0xFFD95D39),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoChip(label = "Genre", value = "Pop")
                InfoChip(label = "Year", value = "2024")
                InfoChip(label = "Duration", value = "3:45")
                InfoChip(label = "Album", value = "Title")
            }
        }
    }
}
