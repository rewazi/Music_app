package com.example.musicapp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.ui.components.items.DrawerItem
import com.example.musicapp.ui.components.items.PlaylistItem

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFE8622A))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "MATVIKO", 
                color = Color.White, 
                fontSize = 24.sp, 
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        DrawerItem("HOME", Icons.Default.Home, active = true)
        DrawerItem("SEARCH", Icons.Default.Search)
        DrawerItem("LIBRARY", Icons.AutoMirrored.Filled.List)

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "PLAYLISTS", 
            color = Color.White, 
            fontSize = 18.sp, 
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(
            color = Color.White.copy(alpha = 0.3f), 
            modifier = Modifier.padding(vertical = 8.dp)
        )

        repeat(3) {
            PlaylistItem("Your Playlist")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically, 
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text("...", color = Color(0xFFE8622A), fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Other playlists", color = Color.White)
        }
    }
}
