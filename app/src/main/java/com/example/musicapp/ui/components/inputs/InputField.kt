package com.example.musicapp.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R

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
            .let { 
                if (showBorder) it.border(width = 1.dp, color = Color.White, shape = shape) 
                else it 
            },
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
