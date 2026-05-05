package com.example.musicapp.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicapp.R
import com.example.musicapp.data.model.Album
import com.example.musicapp.data.model.Song
import com.example.musicapp.ui.components.player.BottomPlayerBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showSongInfo by remember { mutableStateOf(false) }
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    Log.e("MusicApp", "ExoPlayer Error: ${error.message}", error)
                    Toast.makeText(context, "Playback Error: ${error.errorCodeName}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(currentSong) {
        currentSong?.let { song ->
            Log.d("MusicApp", "Playing song: ${song.title} from URL: ${song.songUrl}")
            exoPlayer.setMediaItem(MediaItem.fromUri(song.songUrl))
            exoPlayer.prepare()
            if (isPlaying) exoPlayer.play()
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

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
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Welcome", 
                                    color = Color.White, 
                                    fontSize = 16.sp,
                                    modifier = Modifier.clickable { onNavigateToProfile() }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(R.drawable.qlementine_icons_user_16),
                                    contentDescription = "Profile",
                                    tint = Color(0xFFF0A202),
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable { onNavigateToProfile() }
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { 
                                if (selectedAlbum == null) {
                                    scope.launch { drawerState.open() }
                                } else {
                                    selectedAlbum = null
                                }
                            }) {
                                Icon(
                                    painter = painterResource(
                                        if (selectedAlbum == null) R.drawable.menu else R.drawable.ic_back_curved
                                    ),
                                    contentDescription = null, 
                                    tint = Color(0xFFF0A202)
                                )
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
                    BottomPlayerBar(
                        currentSong = currentSong,
                        isPlaying = isPlaying,
                        onTogglePlay = { isPlaying = !isPlaying },
                        onShowSongInfo = { if (currentSong != null) showSongInfo = true }
                    )
                },
                containerColor = Color(0xFF2D0B20)
            ) { padding ->
                if (selectedAlbum == null) {
                    MainContent(padding, onAlbumClick = { selectedAlbum = it })
                } else {
                    androidx.activity.compose.BackHandler {
                        selectedAlbum = null
                    }
                    AlbumDetailsScreen(
                        album = selectedAlbum!!,
                        padding = padding,
                        onBack = { selectedAlbum = null },
                        onSongClick = { 
                            currentSong = it
                            isPlaying = true
                        }
                    )
                }
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
            SongInfoFullScreen(
                song = currentSong,
                isPlaying = isPlaying,
                onTogglePlay = { isPlaying = !isPlaying },
                onClose = { showSongInfo = false }
            )
        }
    }
}
