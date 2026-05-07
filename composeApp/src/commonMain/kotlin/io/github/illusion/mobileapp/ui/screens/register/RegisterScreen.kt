package io.github.illusion.mobileapp.ui.screens.register

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.ic_email
import mobileapp.composeapp.generated.resources.ic_lock
import mobileapp.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit = {},
    onNavigateToVerification: (String) -> Unit = {},  // ← НОВЫЙ ПАРАМЕТР: переход на верификацию с email
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        RegisterContent(
            uiState = uiState,
            onEmailChange = viewModel::updateEmail,
            onUsernameChange = viewModel::updateUsername,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onTermsAcceptedChange = viewModel::updateTermsAccepted,
            onRegisterClick = {
                viewModel.onRegisterClick(
                    onSuccess = { email ->  // ← Теперь передаем email
                        onNavigateToVerification(email)  // ← Переход на экран верификации
                    }
                )
            },
            onBackToLogin = onBackToLogin
        )

        // Баннер ошибки
        ErrorBannerRegister(
            errorMessage = uiState.errorMessage,
            onDismiss = viewModel::clearError
        )
    }
}

@Composable
fun RegisterContent(
    uiState: RegisterUIState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        RegisterTopSection()

        Spacer(modifier = Modifier.height(45.dp))

        RegisterInputsSection(
            uiState = uiState,
            onEmailChange = onEmailChange,
            onUsernameChange = onUsernameChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onTermsAcceptedChange = onTermsAcceptedChange
        )

        Spacer(modifier = Modifier.height(5.dp))

        RegisterBottomSection(
            isRegisterEnabled = uiState.isRegisterEnabled,
            onRegisterClick = onRegisterClick,
            onBackToLogin = onBackToLogin
        )
    }
}

@Composable
fun RegisterTopSection() {
    Column {
        Text(
            text = "LOGO",
            color = Color(0xFFDBDBDB),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Заполните поля регистрации",
            color = Color(0xFF999999),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun RegisterInputsSection(
    uiState: RegisterUIState,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit
) {
    Column {
        CustomRegisterTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            placeholder = "Email",
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_email),
                    contentDescription = "email",
                    tint = Color(0xFF7D7C69),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        CustomRegisterTextField(
            value = uiState.username,
            onValueChange = onUsernameChange,
            placeholder = "Имя пользователя",
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

        CustomRegisterTextField(
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

        Spacer(modifier = Modifier.height(18.dp))

        CustomRegisterTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Подтвердите пароль",
            isPassword = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = "confirm password",
                    tint = Color(0xFF7D7C69),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(35.dp))

        RegisterTermsRow(
            isChecked = uiState.isTermsAccepted,
            onCheckedChange = onTermsAcceptedChange
        )
    }
}

@Composable
fun RegisterTermsRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!isChecked) }
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
            text = "Я принимаю условия пользовательского соглашения",
            color = Color(0xFFDBDBDB),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun RegisterBottomSection(
    isRegisterEnabled: Boolean,
    onRegisterClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Column {
        RegisterButton(
            enabled = isRegisterEnabled,
            onClick = onRegisterClick
        )

        Spacer(modifier = Modifier.height(45.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Уже есть аккаунт?",
                color = Color(0xFF807F66),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Войти",
                color = Color(0xFFC6C247),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }
}

@Composable
fun RegisterButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
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
            text = "СОЗДАТЬ",
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun CustomRegisterTextField(
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