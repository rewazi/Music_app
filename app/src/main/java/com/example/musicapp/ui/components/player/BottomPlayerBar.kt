package com.example.musicapp.ui.components.player

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.ui.theme.AccentOrange
import com.example.musicapp.ui.theme.BgMedium

@Composable
fun BottomPlayerBar(
    currentSong: Song? = null,
    isPlaying: Boolean = false,
    isShuffleMode: Boolean = false,
    repeatMode: Int = 0, // 0: off, 1: one, 2: all
    onTogglePlay: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onToggleShuffle: () -> Unit = {},
    onToggleRepeat: () -> Unit = {},
    onShowSongInfo: () -> Unit = {}
) {
    var skipPrevFlash by remember { mutableStateOf(false) }
    var skipNextFlash by remember { mutableStateOf(false) }

    LaunchedEffect(skipPrevFlash) {
        if (skipPrevFlash) {
            delay(200)
            skipPrevFlash = false
        }
    }

    LaunchedEffect(skipNextFlash) {
        if (skipNextFlash) {
            delay(200)
            skipNextFlash = false
        }
    }

    Surface(
        color = BgMedium,
        modifier = Modifier.fillMaxWidth().height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group 1: Play, Repeat, Prev (Left side)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onTogglePlay, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = AccentOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = onToggleRepeat, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.material_symbols_repeat),
                        contentDescription = null,
                        tint = if (repeatMode != 0) Color.White else AccentOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(onClick = onPrevious, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next),
                        contentDescription = null,
                        tint = if (skipPrevFlash) Color.White else AccentOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Center: Song Title (Fixed center position)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentSong != null) "${currentSong.singerName} - ${currentSong.title}" else "Select a song",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }

            // Group 2: Next, Shuffle, Up (Right side)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onNext, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next1),
                        contentDescription = null,
                        tint = if (skipNextFlash) Color.White else AccentOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(onClick = onToggleShuffle, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = null,
                        tint = if (isShuffleMode) Color.White else AccentOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(onClick = onShowSongInfo, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = AccentOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
