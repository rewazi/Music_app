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
import com.example.musicapp.R
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

    Box(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
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
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(R.drawable.menu), 
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
