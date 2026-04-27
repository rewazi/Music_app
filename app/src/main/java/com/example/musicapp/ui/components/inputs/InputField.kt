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
import com.example.musicapp.ui.theme.*

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    isPassword: Boolean = false,
    leadingIcon: Int? = null,
    showBorder: Boolean = true
) {
    val shape = RoundedCornerShape(15.dp) // Figma specs: 15dp corner radius

    val autoIcon: Painter = painterResource(
        id = leadingIcon ?: when {
            hint.contains("Email", true) || hint.contains("Username", true) -> R.drawable.user
            hint.contains("Password", true) -> R.drawable.lock
            else -> R.drawable.user
        }
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint, color = White.copy(alpha = 0.5f), fontSize = 16.sp) },
        leadingIcon = { 
            Icon(
                painter = autoIcon, 
                contentDescription = null, 
                tint = HighlightOrange, 
                modifier = Modifier.padding(start = 12.dp) 
            ) 
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(width = 1.dp, color = White.copy(alpha = 0.8f), shape = shape), // Figma specs: 1px border thickness
        singleLine = true,
        textStyle = TextStyle(color = White, fontSize = 16.sp),
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BgMedium.copy(alpha = 0.4f),
            unfocusedContainerColor = BgMedium.copy(alpha = 0.4f),
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}
