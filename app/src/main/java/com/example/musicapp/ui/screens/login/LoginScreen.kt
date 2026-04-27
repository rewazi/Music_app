package com.example.musicapp.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.data.network.RetrofitClient
import com.example.musicapp.ui.components.inputs.InputField
import com.example.musicapp.ui.components.layout.WaveBottom
import com.example.musicapp.ui.components.layout.WaveTop
import com.example.musicapp.ui.components.layout.WaveAnimationState
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // Changed to Top to have absolute control over height
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
                    text = "Welcome back!",
                    color = White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
                )
            }
            
            Text(
                text = "Log in to existing account", 
                color = White.copy(alpha = 0.7f), 
                fontSize = 14.sp 
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            InputField(
                value = email,
                onValueChange = { email = it },
                hint = "Username",
                leadingIcon = R.drawable.user
            )
            InputField(
                value = password,
                onValueChange = { password = it },
                hint = "Password",
                isPassword = true,
                leadingIcon = R.drawable.lock
            )
            
            Text(
                text = "Forgot Password?",
                color = White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .offset(y = (-8).dp)
                    .clickable { /* Handle forgot password */ }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
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
                        "Log In", 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Or sign up using", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
            
            Row(modifier = Modifier.padding(vertical = 12.dp)) {
                SocialIcon(iconResId = R.drawable.ic_facebook, color = Color(0xFF1877F2))
                Spacer(modifier = Modifier.width(20.dp))
                SocialIcon(iconResId = R.drawable.ic_google, color = White)
            }
            
            Row {
                Text("Don't have an account? ", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
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

@Composable
fun SocialIcon(iconResId: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = if (color == White) Color.Unspecified else White
        )
    }
}
