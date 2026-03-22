package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.ui.theme.MusicAppTheme

sealed class Screen {
    object Registration : Screen()
    object Login : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Registration) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        is Screen.Registration -> RegistrationScreen(onNavigateToLogin = { currentScreen = Screen.Login })
                        is Screen.Login -> LoginScreen(onNavigateToRegistration = { currentScreen = Screen.Registration })
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(onNavigateToLogin: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF4A1535))) {
        WaveTop()
        Box(modifier = Modifier.align(Alignment.BottomCenter)) { WaveBottom() }
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Let's get started!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Create an account on MATVIKO to get all features", color = Color(0xFFCCCCCC), fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(28.dp))
            InputField(hint = "Username")
            InputField(hint = "Email")
            InputField(hint = "Password", isPassword = true)
            InputField(hint = "Confirm Password", isPassword = true)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8622A))
            ) {
                Text("Create", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text("Already have an account? ", color = Color(0xFFCCCCCC), fontSize = 13.sp)
                Text(
                    text = "Login here",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
