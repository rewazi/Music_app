package com.example.musicapp.ui.screens.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicapp.R
import com.example.musicapp.data.model.Album
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.network.RetrofitClient

@Composable
fun AlbumDetailsScreen(
    album: Album,
    padding: PaddingValues,
    onBack: () -> Unit,
    onSongClick: (Song) -> Unit = {}
) {
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    LaunchedEffect(album.id) {
        try {
            songs = RetrofitClient.instance.getSongs(album.id)
        } catch (e: Exception) {
            // handle error
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2D0B20))
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                // Large Album Art
                AsyncImage(
                    model = album.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFFF18805)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Album Info with Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /* Favorite */ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = album.title,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = album.singerName,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 18.sp
                        )
                    }

                    IconButton(onClick = { /* Download */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.vector),
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Divider or Section Header
                androidx.compose.material3.HorizontalDivider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White.copy(alpha = 0.1f)
                )

                // Songs List
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFF18805))
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        songs.forEach { song ->
                            SongListItem(song, onClick = { onSongClick(song) })
                        }
                    }
                }
                
                // Extra spacer for player
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Improved Scroll indicator
        if (scrollState.maxValue > 0) {
            val alpha by animateFloatAsState(targetValue = if (scrollState.isScrollInProgress) 1f else 0.5f)
            val trackHeight = 250.dp
            val thumbHeight = 40.dp

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .height(trackHeight)
                    .width(4.dp)
                    .alpha(alpha)
                    .background(Color(0xFFF18805).copy(alpha = 0.3f), shape = RoundedCornerShape(2.dp))
            ) {
                val scrollPercent = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
                val thumbOffset = (trackHeight - thumbHeight) * scrollPercent

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(thumbHeight)
                        .offset(y = thumbOffset)
                        .background(Color.White, shape = RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun SongListItem(song: Song, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF18805)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = song.singerName,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    }
}
