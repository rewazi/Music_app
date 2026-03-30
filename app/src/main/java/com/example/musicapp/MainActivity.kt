package com.example.musicapp

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.ui.theme.MusicAppTheme
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
            Text("Create an account on MusicApp to get all features", color = Color(0xFFCCCCCC), fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(28.dp))
            
            InputField(value = username, onValueChange = { username = it }, hint = "Username")
            InputField(value = email, onValueChange = { email = it }, hint = "Email")
            InputField(value = password, onValueChange = { password = it }, hint = "Password", isPassword = true)
            InputField(value = confirmPassword, onValueChange = { confirmPassword = it }, hint = "Confirm Password", isPassword = true)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.register(username, email, password)
                            if (response.success) {
                                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                onNavigateToLogin()
                            } else {
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8622A))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Create", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
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
