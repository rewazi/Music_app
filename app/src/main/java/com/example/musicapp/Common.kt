package com.example.musicapp

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object RetrofitClient {
    // Replace with your local IP address for XAMPP (usually 10.0.2.2 for Android Emulator)
    private const val BASE_URL = "http://10.0.2.2/musicapp/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

@Composable
fun WaveTop() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveTop")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "waveTopOffset"
    )
    val horizontalShift by infiniteTransition.animateFloat(
        initialValue = -60f, targetValue = 60f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "waveTopHorizontal"
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val w = size.width
        val h = size.height
        val shift = waveOffset * 80f

        val backPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h * 0.55f)
            quadraticTo(w * 0.75f + horizontalShift, h * 1.1f - shift, w * 0.5f, h * 0.65f)
            quadraticTo(w * 0.25f - horizontalShift, h * 0.2f + shift, 0f, h * 0.75f)
            close()
        }
        drawPath(backPath, Color(0xFF511730))

        val frontPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h * 0.35f)
            quadraticTo(w * 0.75f - horizontalShift, h * 0.9f + shift, w * 0.5f, h * 0.45f)
            quadraticTo(w * 0.25f + horizontalShift, h * 0.05f - shift, 0f, h * 0.55f)
            close()
        }
        drawPath(frontPath, Color(0xFFD95D39))
    }
}

@Composable
fun WaveBottom() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveBottom")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "waveBottomOffset"
    )
    val horizontalShift by infiniteTransition.animateFloat(
        initialValue = 60f, targetValue = -60f,
        animationSpec = infiniteRepeatable(animation = tween(3500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "waveBottomHorizontal"
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val w = size.width
        val h = size.height
        val shift = waveOffset * 80f

        val backPath = Path().apply {
            moveTo(0f, h)
            lineTo(w, h)
            lineTo(w, h * 0.45f)
            quadraticTo(w * 0.75f + horizontalShift, h * -0.1f + shift, w * 0.5f, h * 0.35f)
            quadraticTo(w * 0.25f - horizontalShift, h * 0.8f - shift, 0f, h * 0.25f)
            close()
        }
        drawPath(backPath, Color(0xFF511730))

        val frontPath = Path().apply {
            moveTo(0f, h)
            lineTo(w, h)
            lineTo(w, h * 0.65f)
            quadraticTo(w * 0.75f - horizontalShift, h * 0.1f - shift, w * 0.5f, h * 0.55f)
            quadraticTo(w * 0.25f + horizontalShift, h * 0.95f + shift, 0f, h * 0.45f)
            close()
        }
        drawPath(frontPath, Color(0xFFD95D39))
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    isPassword: Boolean = false,
    leadingIcon: Int? = null,
    showBorder: Boolean = false
) {
    val shape = RoundedCornerShape(12.dp)

    val autoIcon: Painter = painterResource(
        id = leadingIcon ?: when {
            hint.contains("Email", true) -> R.drawable.mail
            hint.contains("Password", true) -> R.drawable.lock
            else -> R.drawable.user
        }
    )


    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint, color = Color(0xFFAAAAAA)) },
        leadingIcon = { Icon(autoIcon, contentDescription = null, tint = Color(0xFFC47A27)) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFffffff),
                shape = shape
            ),
        singleLine = true,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF6B2347).copy(alpha = 0.5f),
            unfocusedContainerColor = Color(0xFF6B2347).copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}