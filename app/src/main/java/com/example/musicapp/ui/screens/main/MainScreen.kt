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
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.musicapp.R
import com.example.musicapp.data.model.Album
import com.example.musicapp.data.model.Song
import com.example.musicapp.ui.components.player.BottomPlayerBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.OptIn

@androidx.media3.common.util.UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showSongInfo by remember { mutableStateOf(false) }
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    var currentAlbumSongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isShuffleMode by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableIntStateOf(Player.REPEAT_MODE_OFF) }

    val context = LocalContext.current
    val exoPlayer = remember {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .setAllowCrossProtocolRedirects(true)
            .setDefaultRequestProperties(mapOf(
                "Accept" to "*/*",
                "Connection" to "keep-alive"
            ))
            
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(httpDataSourceFactory))
            .build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        duration = contentDuration
                    }
                }
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentSong = currentAlbumSongs.find { it.songUrl == mediaItem?.localConfiguration?.uri.toString() }
                }
                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    Log.e("MusicApp", "ExoPlayer Error: ${error.message}", error)
                    Toast.makeText(context, "Playback Error: ${error.errorCodeName}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun playNext() {
        if (currentAlbumSongs.isNotEmpty() && currentSong != null) {
            val currentIndex = currentAlbumSongs.indexOf(currentSong)
            val nextIndex = (currentIndex + 1) % currentAlbumSongs.size
            currentSong = currentAlbumSongs[nextIndex]
            isPlaying = true
        }
    }

    fun playPrevious() {
        if (currentAlbumSongs.isNotEmpty() && currentSong != null) {
            val currentIndex = currentAlbumSongs.indexOf(currentSong)
            val prevIndex = if (currentIndex > 0) currentIndex - 1 else currentAlbumSongs.size - 1
            currentSong = currentAlbumSongs[prevIndex]
            isPlaying = true
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = exoPlayer.currentPosition
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    LaunchedEffect(isShuffleMode) {
        exoPlayer.shuffleModeEnabled = isShuffleMode
    }

    LaunchedEffect(repeatMode) {
        exoPlayer.repeatMode = repeatMode
    }

    LaunchedEffect(currentSong) {
        currentSong?.let { song ->
            val currentMediaUri = exoPlayer.currentMediaItem?.localConfiguration?.uri?.toString()
            if (currentMediaUri != song.songUrl) {
                exoPlayer.setMediaItem(MediaItem.fromUri(song.songUrl))
                exoPlayer.prepare()
                exoPlayer.play()
            }
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
            @OptIn(ExperimentalMaterial3Api::class)
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
                        isShuffleMode = isShuffleMode,
                        repeatMode = repeatMode,
                        onTogglePlay = { isPlaying = !isPlaying },
                        onNext = { playNext() },
                        onPrevious = { playPrevious() },
                        onToggleShuffle = { isShuffleMode = !isShuffleMode },
                        onToggleRepeat = {
                            repeatMode = when (repeatMode) {
                                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                                else -> Player.REPEAT_MODE_OFF
                            }
                        },
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
                        onSongsLoaded = { currentAlbumSongs = it },
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
                isShuffleMode = isShuffleMode,
                repeatMode = repeatMode,
                currentPosition = currentPosition,
                duration = duration,
                onTogglePlay = { isPlaying = !isPlaying },
                onToggleShuffle = { isShuffleMode = !isShuffleMode },
                onToggleRepeat = {
                    repeatMode = when (repeatMode) {
                        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                        Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                        else -> Player.REPEAT_MODE_OFF
                    }
                },
                onNext = { playNext() },
                onPrevious = { playPrevious() },
                onSeek = { fraction ->
                    val seekPos = (fraction * duration).toLong()
                    exoPlayer.seekTo(seekPos)
                    currentPosition = seekPos
                },
                onClose = { showSongInfo = false }
            )
        }
    }
}
