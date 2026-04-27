package com.example.musicapp.ui.components.player

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomPlayerBar(onShowSongInfo: () -> Unit = {}) {
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isWorking by remember { mutableStateOf(false) }
    var ChangeWhite by remember { mutableStateOf(false) }
    var prevWhite by remember { mutableStateOf(false) }
    var nextWhite by remember { mutableStateOf(false) }
    var volWhite by remember { mutableStateOf(false) }
    var upWhite by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun flashWhite(setter: (Boolean) -> Unit) {
        setter(true)
        scope.launch {
            delay(1000)
            setter(false)
        }
    }

    Surface(color = Color(0xFF4A1535), modifier = Modifier.fillMaxWidth().height(64.dp)) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { isPlaying = !isPlaying }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isPlaying) Color.White else Color(0xFFD95D39)
                )
            }

            IconButton(onClick = { isWorking = !isWorking; flashWhite { ChangeWhite = it } }) {
                Icon(
                    painter = painterResource(id = R.drawable.material_symbols_repeat),
                    contentDescription = null,
                    tint = if (isWorking || ChangeWhite) Color.White else Color(0xFFD95D39)
                )
            }

            IconButton(onClick = { flashWhite { prevWhite = it } }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_next),
                    contentDescription = null,
                    tint = if (prevWhite) Color.White else Color(0xFFD95D39)
                )
            }

            Text(
                "Singer - Song Title",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { flashWhite { nextWhite = it } }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_next1),
                    contentDescription = null,
                    tint = if (nextWhite) Color.White else Color(0xFFD95D39)
                )
            }

            IconButton(onClick = { isMuted = !isMuted; flashWhite { volWhite = it } }) {
                Icon(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = null,
                    tint = if (isMuted || volWhite) Color.White else Color(0xFFD95D39),
                    modifier = Modifier.offset(y = 4.dp)
                )
            }

            IconButton(onClick = {
                flashWhite { upWhite = it }
                onShowSongInfo()
            }) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "Song info",
                    tint = if (upWhite) Color.White else Color(0xFFD95D39)
                )
            }
        }
    }
}
