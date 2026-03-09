package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.animation.core.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RegistrationScreen()
            }
        }
    }
}


@Composable
fun WaveTop() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveTop")


    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveTopOffset"
    )


    val horizontalShift by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
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
            quadraticBezierTo(
                w * 0.75f + horizontalShift,  // 🆕 двигаем точку притяжения
                h * 1.1f - shift,
                w * 0.5f,
                h * 0.65f
            )
            quadraticBezierTo(
                w * 0.25f - horizontalShift,  // 🆕 в противоположную сторону
                h * 0.2f + shift,
                0f,
                h * 0.75f
            )
            close()
        }
        drawPath(backPath, Color(0xFF8B1A4A))

        val frontPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h * 0.35f)
            quadraticBezierTo(
                w * 0.75f - horizontalShift,  // 🆕 передняя волна в другую сторону
                h * 0.9f + shift,
                w * 0.5f,
                h * 0.45f
            )
            quadraticBezierTo(
                w * 0.25f + horizontalShift,
                h * 0.05f - shift,
                0f,
                h * 0.55f
            )
            close()
        }
        drawPath(frontPath, Color(0xFFC0392B))
    }
}

@Composable
fun WaveBottom() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveBottom")

    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveBottomOffset"
    )


    val horizontalShift by infiniteTransition.animateFloat(
        initialValue = 60f,
        targetValue = -60f,  
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
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
            quadraticBezierTo(
                w * 0.75f + horizontalShift,
                h * -0.1f + shift,
                w * 0.5f,
                h * 0.35f
            )
            quadraticBezierTo(
                w * 0.25f - horizontalShift,
                h * 0.8f - shift,
                0f,
                h * 0.25f
            )
            close()
        }
        drawPath(backPath, Color(0xFF8B1A4A))

        val frontPath = Path().apply {
            moveTo(0f, h)
            lineTo(w, h)
            lineTo(w, h * 0.65f)
            quadraticBezierTo(
                w * 0.75f - horizontalShift,
                h * 0.1f - shift,
                w * 0.5f,
                h * 0.55f
            )
            quadraticBezierTo(
                w * 0.25f + horizontalShift,
                h * 0.95f + shift,
                0f,
                h * 0.45f
            )
            close()
        }
        drawPath(frontPath, Color(0xFFC0392B))
    }
}

@Composable
fun InputField(hint: String, isPassword: Boolean = false) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(hint, color = Color(0xFFAAAAAA)) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF6B2347),
            unfocusedContainerColor = Color(0xFF6B2347),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun RegistrationScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4A1535))
    ) {

        WaveTop()


        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            WaveBottom()
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Let's get started!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create an account on MATVIKO to get all features",
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            InputField(hint = "Username")
            InputField(hint = "Email")
            InputField(hint = "Password", isPassword = true)
            InputField(hint = "Confirm Password", isPassword = true)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8622A)
                )
            ) {
                Text("Create", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account? Login here",
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp
            )
        }
    }
}