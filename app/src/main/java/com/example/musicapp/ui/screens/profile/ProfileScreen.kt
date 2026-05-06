package com.example.musicapp.ui.screens.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.musicapp.R
import com.example.musicapp.data.local.PreferenceManager
import com.example.musicapp.ui.components.items.ProfileMenuItem
import com.example.musicapp.ui.components.player.BottomPlayerBar
import com.example.musicapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val username = remember { preferenceManager.getUsername() }

    Scaffold(
        containerColor = BgDark,
        bottomBar = { BottomPlayerBar() }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Orange Header Background (Doesn't scroll)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                    .background(AccentOrange)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(16.dp).statusBarsPadding()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_curved),
                        contentDescription = "Back",
                        tint = BgDark,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(85.dp))


                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.size(110.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(3.dp, AccentOrange, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(BgMedium),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize(0.7f)
                        )
                    }
                    // Edit badge

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(HighlightOrange)
                            .border(1.5.dp, BgDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = username,
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Orange Card

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .wrapContentHeight(),
                    color = AccentOrange,
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Default.Edit,
                            label = "Edit profile",
                            onClick = onNavigateToEditProfile
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Lock,
                            label = "Change password"
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Settings,
                            label = "Settings"
                        )
                        ProfileMenuItem(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            label = "Log Out",
                            onClick = {
                                preferenceManager.clear()
                                onLogout()
                            }
                        )
                    }
                }
                
                // Extra spacer for player
                Spacer(modifier = Modifier.height(72.dp))
            }

            // Scroll indicator
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
}