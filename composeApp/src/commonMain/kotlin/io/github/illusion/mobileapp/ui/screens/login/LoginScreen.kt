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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.ic_lock
import mobileapp.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToMain: () -> Unit = {},
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

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
            .padding(horizontal = 41.dp)
    ) {
        LoginContent(
            uiState = uiState,
            onLoginChange = loginViewModel::updateLogin,
            onPasswordChange = loginViewModel::updatePassword,
            onRememberMeChange = loginViewModel::updateRememberMe,
            onLoginClick = {
                loginViewModel.onLoginClick(
                    onSuccess = onNavigateToMain
                )
            },
            onNavigateToRegister = onNavigateToRegister
        )

        // Выплывающее уведомление об ошибке
        ErrorBanner(
            errorMessage = uiState.errorMessage,
            onDismiss = { loginViewModel.clearError() }
        )
    }
}

@Composable
fun LoginContent(
    uiState: LoginUIState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        TopSection()

        Spacer(modifier = Modifier.height(45.dp))

        InputsSection(
            uiState = uiState,
            onLoginChange = onLoginChange,
            onPasswordChange = onPasswordChange,
            onRememberMeChange = onRememberMeChange,
            onLoginClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(45.dp))

        BottomSection(
            onRegisterClick = onNavigateToRegister  // ← ПЕРЕДАЕМ В BottomSection
        )
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

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Движение вперёд с осознанием ответственности перед людьми и природой.",
            color = Color(0xFF999999),
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp)
        )
    }
}

@Composable
fun InputsSection(
    uiState: LoginUIState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit
) {
    Column {
        CustomTextField(
            value = uiState.login,
            onValueChange = onLoginChange,
            placeholder = "Логин",
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_user),
                    contentDescription = "user",
                    tint = Color(0xFF7D7C69),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CustomTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            placeholder = "Пароль",
            isPassword = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = "lock",
                    tint = Color(0xFF7D7C69),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(35.dp))

        BottomRow(
            isChecked = uiState.isRememberMe,
            onCheckedChange = onRememberMeChange
        )

        Spacer(modifier = Modifier.height(5.dp))

        LoginButton(
            enabled = uiState.isLoginEnabled,
            onClick = onLoginClick
        )
    }
}

@Composable
fun BottomRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFDBDBDB),
                    uncheckedColor = Color(0xFFDBDBDB),
                    checkmarkColor = Color.Black
                )
            )

            Text(
                "Запомнить меня",
                color = Color(0xFFDBDBDB),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Забыли пароль?",
            color = Color(0xFFC6C247),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                // TODO: Обработка восстановления пароля
            }
        )
    }
}

@Composable
fun LoginButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    // Анимируем цвета
    val buttonColor1 by animateColorAsState(
        targetValue = if (enabled) Color(0xFFE2D566) else Color(0xFF4D4B3D),
        animationSpec = tween(300),
        label = "buttonColor1"
    )

    val buttonColor2 by animateColorAsState(
        targetValue = if (enabled) Color(0xFFAD9B2A) else Color(0xFF3A382C),
        animationSpec = tween(300),
        label = "buttonColor2"
    )

    val textColor by animateColorAsState(
        targetValue = if (enabled) Color(0xFFDBDBDB) else Color(0xFF7D7D69),
        animationSpec = tween(300),
        label = "textColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(buttonColor1, buttonColor2)
                )
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ВОЙТИ",
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun BottomSection(
    onRegisterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Еще нет аккаунта?",
            color = Color(0xFF807F66),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Регистрация",
            color = Color(0xFFC6C247),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFF7D7D69),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = leadingIcon,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF4D4B3D),
            unfocusedContainerColor = Color(0xFF4D4B3D),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFFDBDBDB),
            unfocusedTextColor = Color(0xFFDBDBDB)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}