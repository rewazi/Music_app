package com.example.musicapp.ui.screens.main

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicapp.data.model.Album
import com.example.musicapp.data.network.RetrofitClient
import com.example.musicapp.ui.components.items.AlbumItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainContent(
    padding: PaddingValues,
    onAlbumClick: (Album) -> Unit = {}
) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            Log.d("MusicApp", "Fetching albums...")
            albums = RetrofitClient.instance.getAlbums()
            Log.d("MusicApp", "Success: ${albums.size} albums found")
        } catch (e: Exception) {
            error = e.message
            Log.e("MusicApp", "Error fetching albums: ${e.message}", e)
        }
    }

    if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error", color = Color.White, modifier = Modifier.padding(16.dp))
        }
    } else if (albums.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFF18805))
        }
    } else {
        val scrollState = rememberScrollState()
        
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                val pagerState = rememberPagerState(pageCount = { albums.size })

                LaunchedEffect(pagerState.settledPage) {
                    while (true) {
                        delay(5000)
                        if (!pagerState.isScrollInProgress) {
                            val nextPage = (pagerState.currentPage + 1) % albums.size
                            pagerState.animateScrollToPage(
                                page = nextPage,
                                animationSpec = tween(600)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(Color(0xFF2D0B20))) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        beyondViewportPageCount = 1
                    ) { page ->
                        val album = albums[page]
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF18805))
                            .clickable { onAlbumClick(album) }
                        ) {
                            AsyncImage(
                                model = album.bannerUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(32.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    album.title,
                                    color = Color(0xFF4A1535),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.background(Color.White.copy(alpha = 0.4f)).padding(horizontal = 4.dp)
                                )
                                Text(
                                    album.singerName,
                                    color = Color(0xFF4A1535),
                                    fontSize = 18.sp,
                                    modifier = Modifier.background(Color.White.copy(alpha = 0.4f)).padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                    Row(
                        Modifier.height(40.dp).fillMaxWidth().align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(albums.size) { iteration ->
                            Canvas(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(12.dp)
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(iteration)
                                        }
                                    }
                            ) {
                                drawCircle(
                                    color = Color(0xFF4A1535),
                                    radius = size.minDimension / 2,
                                    style = if (pagerState.currentPage == iteration) Fill else Stroke(2f)
                                )
                            }
                        }
                    }
                }

                Text(
                    "Album Title",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.heightIn(max = 2000.dp) 
                ) {
                    items(albums) { album ->
                        AlbumItem(album, onClick = { onAlbumClick(album) })
                    }
                }
                
                // Extra spacer to make sure player doesn't hide content
                Spacer(modifier = Modifier.height(80.dp))
            }

            // Scroll indicator (Right side)
            if (scrollState.maxValue > 0) {
                val alpha by animateFloatAsState(targetValue = if (scrollState.isScrollInProgress) 1f else 0.5f)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .fillMaxHeight(0.3f) // Scale indicator height
                        .width(4.dp)
                        .alpha(alpha)
                        .background(Color(0xFFF18805), shape = RoundedCornerShape(2.dp))
                ) {
                    // This is a simple static indicator for now, 
                    // for dynamic movement we'd need to calculate thumb position
                    val scrollPercent = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f)
                            .offset(y = (scrollPercent * 100).dp) // approximation
                            .background(Color.White, shape = RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}
