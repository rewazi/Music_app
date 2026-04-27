package com.example.musicapp.ui.screens.registration

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.data.network.RetrofitClient
import com.example.musicapp.ui.components.inputs.InputField
import com.example.musicapp.ui.components.layout.WaveBottom
import com.example.musicapp.ui.components.layout.WaveTop
import com.example.musicapp.ui.components.layout.WaveAnimationState
import com.example.musicapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    waveState: WaveAnimationState,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        WaveTop(offset = waveState.topOffset.value, horizontalShift = waveState.topHorizontal.value)
        Box(modifier = Modifier.align(Alignment.BottomCenter)) { 
            WaveBottom(offset = waveState.bottomOffset.value, horizontalShift = waveState.bottomHorizontal.value) 
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // Changed to Top to match LoginScreen and have absolute control
            verticalArrangement = Arrangement.Top 
        ) {
            // Shared fixed Spacer to align starting point of content
            Spacer(modifier = Modifier.fillMaxHeight(0.22f))

            // Standardized Header Pill with fixed width
            Surface(
                color = BgMedium.copy(alpha = 0.6f),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .width(280.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Let's get started!",
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                )
            }
            
            Text(
                text = "Create an account on MusicApp to get all features", 
                color = White.copy(alpha = 0.7f), 
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            InputField(value = username, onValueChange = { username = it }, hint = "Username", showBorder = true)
            InputField(value = email, onValueChange = { email = it }, hint = "Email", showBorder = true)
            InputField(value = password, onValueChange = { password = it }, hint = "Password", isPassword = true, showBorder = true)
            InputField(value = confirmPassword, onValueChange = { confirmPassword = it }, hint = "Confirm Password", isPassword = true, showBorder = true)

            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier.fillMaxWidth(0.8f).height(64.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Create", 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Already have an account? ", color = White.copy(alpha = 0.8f), fontSize = 13.sp)
                Text(
                    text = "Login here",
                    color = White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
