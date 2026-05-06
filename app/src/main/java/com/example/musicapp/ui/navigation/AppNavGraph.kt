package com.example.musicapp.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.ui.components.layout.rememberWaveAnimationState
import com.example.musicapp.ui.screens.login.LoginScreen
import com.example.musicapp.ui.screens.main.MainScreen
import com.example.musicapp.ui.screens.profile.EditProfileScreen
import com.example.musicapp.ui.screens.profile.ProfileScreen
import com.example.musicapp.ui.screens.registration.RegistrationScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val waveState = rememberWaveAnimationState()

    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable("login") {
            LoginScreen(
                waveState = waveState,
                onNavigateToRegistration = {
                    navController.navigate("registration")
                },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("registration") {
            RegistrationScreen(
                waveState = waveState,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("registration") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate("edit_profile")
                }
            )
        }
        composable("edit_profile") {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
