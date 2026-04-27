package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.components.layout.rememberWaveAnimationState
import com.example.musicapp.ui.screens.login.LoginScreen
import com.example.musicapp.ui.screens.main.MainScreen
import com.example.musicapp.ui.screens.profile.ProfileScreen
import com.example.musicapp.ui.screens.registration.RegistrationScreen
import com.example.musicapp.ui.theme.MusicAppTheme

sealed class Screen {
    object Registration : Screen()
    object Login : Screen()
    object Main : Screen()
    object Profile : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Registration) }
                val waveState = rememberWaveAnimationState()

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        is Screen.Registration -> RegistrationScreen(
                            waveState = waveState,
                            onNavigateToLogin = { currentScreen = Screen.Login }
                        )
                        is Screen.Login -> LoginScreen(
                            waveState = waveState,
                            onNavigateToRegistration = { currentScreen = Screen.Registration },
                            onLoginSuccess = { currentScreen = Screen.Main }
                        )
                        is Screen.Main -> MainScreen(
                            onNavigateToProfile = { currentScreen = Screen.Profile }
                        )
                        is Screen.Profile -> ProfileScreen(
                            onNavigateBack = { currentScreen = Screen.Main }
                        )
                    }
                }
            }
        }
    }
}
