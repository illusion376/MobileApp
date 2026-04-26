package io.github.illusion.mobileapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF333329),
                        Color(0xFF1E1E18)
                    )
                )
            )
            .padding(24.dp)
    ) {
        LoginContent()
    }
}

@Composable
fun LoginContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        TopSection()

        Spacer(modifier = Modifier.height(40.dp))

        InputsSection()

        Spacer(modifier = Modifier.height(16.dp))

        BottomSection()
    }
}

@Composable
fun TopSection() {
    Column {

        Text(
            text = "LOGO",
            color = Color(0xFFDBDBDB),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Движение вперёд с осознанием ответственности перед людьми и природой.",
            color = Color(0xFF999999),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun InputsSection() {
    Column {

        CustomTextField("Логин")

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField("Пароль")

        Spacer(modifier = Modifier.height(12.dp))

        BottomRow()

        Spacer(modifier = Modifier.height(20.dp))

        LoginButton()
    }
}

@Composable
fun BottomRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFDBDBDB),
                    uncheckedColor = Color(0xFFDBDBDB),
                    checkmarkColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Запомнить меня",
                color = Color(0xFFDBDBDB),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Забыли пароль?",
            color = Color(0xFFC6C247),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LoginButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFE2D566),
                        Color(0xFFAD9B2A)
                    )
                )
            )
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ВОЙТИ",
            color = Color(0xFFDBDBDB),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun BottomSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Don’t have an account?",
            color = Color(0xFF807F66),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Sign up",
            color = Color(0xFFC6C247),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CustomTextField(placeholder: String) {
    TextField(
        value = "",
        onValueChange = {},
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF7D7D69),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF4D4B3D),
            unfocusedContainerColor = Color(0xFF4D4B3D),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
    )
}