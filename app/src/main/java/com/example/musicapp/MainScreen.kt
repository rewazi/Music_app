package com.example.musicapp

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF4A1535),
                modifier = Modifier.width(300.dp)
            ) {
                DrawerContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text("Welcome", color = Color.White, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(R.drawable.qlementine_icons_user_16),
                                contentDescription = null,
                                tint = Color(0xFFF0A202),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(painter = painterResource(R.drawable.menu), contentDescription = null, tint = Color(0xFFF0A202))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF4A1535),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color(0xFFE8622A)
                    )
                )
            },
            bottomBar = { BottomPlayerBar() },
            containerColor = Color(0xFF2D0B20)
        ) { padding ->
            MainContent(padding)
        }
    }
}

@Composable
fun DrawerContent() {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFE8622A))
            Spacer(modifier = Modifier.width(12.dp))
            Text("MATVIKO", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(32.dp))
        
        DrawerItem("HOME", Icons.Default.Home, active = true)
        DrawerItem("SEARCH", Icons.Default.Search)
        DrawerItem("LIBRARY", Icons.AutoMirrored.Filled.List)
        
        Spacer(modifier = Modifier.height(32.dp))
        Text("PLAYLISTS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(color = Color.White.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
        
        repeat(3) {
            PlaylistItem("Your Playlist")
        }
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
            Text("...", color = Color(0xFFE8622A), fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Other playlists", color = Color.White)
        }
    }
}

@Composable
fun DrawerItem(label: String, icon: ImageVector, active: Boolean = false) {
    Surface(
        color = if (active) Color(0xFF2D0B20) else Color.Transparent,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFE8622A))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaylistItem(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE8622A)))
        Spacer(modifier = Modifier.width(16.dp))
        Text(name, color = Color.White)
    }
}

@Composable
fun MainContent(padding: PaddingValues) {
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
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Banner Carousel using albums' bannerUrl
            val pagerState = rememberPagerState(pageCount = { albums.size })
            
            // Auto-scroll logic improved
            LaunchedEffect(pagerState.settledPage) {
                while (true) {
                    delay(5000)
                    if (!pagerState.isScrollInProgress) {
                        val nextPage = (pagerState.currentPage + 1) % albums.size
                        pagerState.animateScrollToPage(
                            page = nextPage,
                            animationSpec = tween(durationMillis = 800)
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
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF18805))) {
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
                // Dots (indicators)
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
                modifier = Modifier.weight(1f)
            ) {
                items(albums) { album ->
                    AlbumItem(album)
                }
            }
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = album.imageUrl,
            contentDescription = null,
            modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF18805)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(album.title, color = Color.White, fontWeight = FontWeight.Bold)
        Text(album.singerName, color = Color(0xFFD95D39), fontSize = 12.sp)
    }
}

@Composable
fun BottomPlayerBar() {
    Surface(color = Color(0xFF4A1535), modifier = Modifier.fillMaxWidth().height(64.dp)) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFFD95D39))
            Icon(painter = painterResource(id = R.drawable.material_symbols_repeat), contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Icon(painter = painterResource(id = R.drawable.skip_next), contentDescription = null, tint = Color(0xFFD95D39))
            
            Text("Singer - Song Title", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            
            Icon(painter = painterResource(id = R.drawable.skip_next1), contentDescription = null, tint = Color(0xFFD95D39))
            Icon(painter = painterResource(id = R.drawable.vector), contentDescription = null, tint = Color(0xFFD95D39),modifier = Modifier.offset(y = 4.dp))
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = Color(0xFFD95D39))
        }
    }
}
