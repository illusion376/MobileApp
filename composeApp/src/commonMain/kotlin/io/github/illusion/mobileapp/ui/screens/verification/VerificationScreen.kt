package io.github.illusion.mobileapp.ui.screens.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.illusion.mobileapp.ui.screens.themes.scaledSp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.illusion.mobileapp.resources.Res
import io.github.illusion.mobileapp.resources.ic_email
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerificationScreen(
    email: String,
    onVerificationComplete: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: VerificationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Устанавливаем email при первом запуске
    LaunchedEffect(Unit) {
        viewModel.setEmail(email)
        viewModel.checkVerificationStatus(onVerificationComplete)
    }

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
        VerificationContent(
            uiState = uiState,
            onResendClick = {
                viewModel.resendVerificationEmail {
                    // Показываем уведомление об успешной отправке
                }
            },
            onBackToLogin = onBackToLogin
        )

        // Баннер ошибки (автоматически исчезает через 3 секунды)
        VerificationErrorBanner(
            errorMessage = uiState.errorMessage,
            onDismiss = viewModel::clearError
        )
    }
}

@Composable
fun VerificationContent(
    uiState: VerificationUIState,
    onResendClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Иконка почты (увеличена)
        Icon(
            painter = painterResource(Res.drawable.ic_email),
            contentDescription = "email",
            tint = Color(0xFFC6C247),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Заголовок - крупный и жирный
        Text(
            text = "ПОДТВЕРДИТЕ EMAIL",
            color = Color(0xFFDBDBDB),
            fontSize = scaledSp(24),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Текст с инструкцией - крупнее
        Text(
            text = "Мы отправили письмо со ссылкой для подтверждения на адрес",
            color = Color(0xFF999999),
            fontSize = scaledSp(16),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email пользователя - крупный и жирный
        Text(
            text = uiState.email,
            color = Color(0xFFC6C247),
            fontSize = scaledSp(18),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Информация о проверке
        Text(
            text = "После подтверждения вы будете автоматически перенаправлены в приложение",
            color = Color(0xFF7D7D69),
            fontSize = scaledSp(14),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Кнопка повторной отправки - крупнее
        Text(
            text = "Отправить письмо повторно",
            color = Color(0xFFC6C247),
            fontSize = scaledSp(16),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onResendClick() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Ссылка на вход
        Text(
            text = "Вернуться ко входу",
            color = Color(0xFF807F66),
            fontSize = scaledSp(14),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBackToLogin() }
                .padding(8.dp)
        )

        // Индикатор ожидания подтверждения
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Ожидаем подтверждения...",
            color = Color(0xFF7D7D69),
            fontSize = scaledSp(14),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}