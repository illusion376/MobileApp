package io.github.illusion.mobileapp.ui.screens.main

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobileapp.composeapp.generated.resources.Res
import mobileapp.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 41.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Шапка с логотипом и кнопкой выхода
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "LOGO",
                    color = Color(0xFFDBDBDB),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Выйти",
                    color = Color(0xFFC6C247),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onLogout() }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Приветствие
            Text(
                text = "Добро пожаловать!",
                color = Color(0xFFDBDBDB),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Иконка пользователя
            Icon(
                painter = painterResource(Res.drawable.ic_user),
                contentDescription = "user",
                tint = Color(0xFFC6C247),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFF4D4B3D))
                    .padding(40.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Карточка с информацией
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF4D4B3D))
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Аккаунт подтвержден ✅",
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Вы успешно прошли верификацию email!",
                        color = Color(0xFFDBDBDB),
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Декоративный текст
            Text(
                text = "Движение вперёд с осознанием ответственности перед людьми и природой.",
                color = Color(0xFF999999),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}