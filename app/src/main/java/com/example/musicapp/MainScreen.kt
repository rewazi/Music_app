package com.example.musicapp

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
fun MainScreen(
    onNavigateToProfile: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showSongInfo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { onNavigateToProfile() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text("Welcome", color = Color.White, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(R.drawable.qlementine_icons_user_16),
                                    contentDescription = "Profile",
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
                bottomBar = {
                    BottomPlayerBar(onShowSongInfo = { showSongInfo = true })
                },
                containerColor = Color(0xFF2D0B20)
            ) { padding ->
                MainContent(padding)
            }
        }


        AnimatedVisibility(
            visible = showSongInfo,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(350)
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            SongInfoFullScreen(onClose = { showSongInfo = false })
        }
    }
}

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

@Composable
fun InfoChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
fun BottomPlayerBar(onShowSongInfo: () -> Unit = {}) {
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isWorking by remember { mutableStateOf(false) }
    var repeatWhite by remember { mutableStateOf(false) }
    var prevWhite by remember { mutableStateOf(false) }
    var nextWhite by remember { mutableStateOf(false) }
    var ChangeWhite by remember { mutableStateOf(false) }
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
                    tint = if (isWorking || ChangeWhite) Color.White else Color(0xFFD95D39),
                    modifier = Modifier.offset(y = 0.dp)
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