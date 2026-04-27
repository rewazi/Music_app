package com.example.musicapp.ui.screens.login

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.data.network.RetrofitClient
import com.example.musicapp.ui.components.inputs.InputField
import com.example.musicapp.ui.components.layout.WaveBottom
import com.example.musicapp.ui.components.layout.WaveTop
import com.example.musicapp.ui.components.layout.WaveAnimationState
import com.example.musicapp.ui.components.social.ExternalSocialIcon
import com.example.musicapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    waveState: WaveAnimationState,
    onNavigateToRegistration: () -> Unit, 
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        WaveTop(offset = waveState.topOffset.value, horizontalShift = waveState.topHorizontal.value)
        Box(modifier = Modifier.align(Alignment.BottomCenter)) { 
            WaveBottom(offset = waveState.bottomOffset.value, horizontalShift = waveState.bottomHorizontal.value) 
        }
        
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = BgMedium.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Welcome back!",
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
            
            Text("Log in to existing account", color = White.copy(alpha = 0.8f), fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            InputField(
                value = email,
                onValueChange = { email = it },
                hint = "Email",
                leadingIcon = R.drawable.user,
                showBorder = true
            )
            InputField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                isPassword = true,
                leadingIcon = R.drawable.lock,
                showBorder = true
            )
            
            Text(
                text = "Forgot Password?",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End).padding(bottom = 24.dp)
            )
            
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.login(email, password)
                            if (response.success) {
                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
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
                modifier = Modifier.width(180.dp).height(52.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Or sign up using", color = White.copy(alpha = 0.8f), fontSize = 13.sp)
            
            Row(modifier = Modifier.padding(vertical = 16.dp)) {
                ExternalSocialIcon(iconResId = R.drawable.ic_facebook, contentDescription = "Facebook")
                Spacer(modifier = Modifier.width(16.dp))
                ExternalSocialIcon(iconResId = R.drawable.ic_google, contentDescription = "Google")
            }
            
            Row {
                Text("Don't have an account? ", color = White.copy(alpha = 0.8f), fontSize = 13.sp)
                Text(
                    text = "Sign up",
                    color = White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToRegistration() }
                )
            }
        }
    }
}
