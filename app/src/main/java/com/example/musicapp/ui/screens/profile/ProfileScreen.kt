package com.example.musicapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.ui.components.items.ProfileMenuItem
import com.example.musicapp.ui.components.player.BottomPlayerBar
import com.example.musicapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        containerColor = BgDark,
        bottomBar = { BottomPlayerBar() }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Orange Header Background
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(85.dp))

                // Avatar
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
                    // Edit badge - Fixed palette (Yellow bg, White icon)
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
                            tint = White, // Fixed to White per screenshot
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "User",
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Orange Card - Now with rounded bottom (cut off earlier)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .wrapContentHeight(), // Cut off earlier
                    color = AccentOrange,
                    shape = RoundedCornerShape(40.dp) // Fully rounded container
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Default.Edit,
                            label = "Edit profile"
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
                            label = "Log Out"
                        )
                    }
                }
            }
        }
    }
}
