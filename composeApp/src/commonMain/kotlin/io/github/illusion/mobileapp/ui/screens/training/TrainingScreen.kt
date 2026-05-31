package io.github.illusion.mobileapp.ui.screens.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.blur
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.illusion.mobileapp.ui.screens.components.map.PlatformMapView
import io.github.illusion.mobileapp.ui.screens.themes.scaledSp
import org.koin.compose.koinInject


private val BgDark = Color(0xFF1B1B15)
private val BgGradientTop = Color(0xFF333329)
private val BgGradientBottom = Color(0xFF1E1E18)
private val Accent = Color(0xFFE2D566)
private val AccentDim = Color(0xFFAD9B2A)
private val TextPrimary = Color(0xFFDBDBDB)
private val TextSecondary = Color(0xFF999999)
private val TextDim = Color(0xFF7D7D69)
private val StepCircleBorder = Color(0xFFC6C247)
private val MapButtonBg = Color(0xFF3A3A30)
private val GlassColor = Color(0xFF2E2D24).copy(alpha = 0.85f)
private val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.10f)

@Composable
fun TrainingScreen(
    onBack: () -> Unit,
    viewModel: TrainingViewModel = koinInject(),
    onToggleTraining: () -> Unit = {},
    onTogglePause: () -> Unit = {},
    onTabSelected: (TrainingTab) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TopBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BgGradientTop, BgGradientBottom)))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextPrimary,
                    )
                }
                Text(
                    text = "Тренировка",
                    color = TextPrimary,
                    fontSize = scaledSp(18),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Настройки",
                        tint = TextPrimary,
                    )
                }
            }

            // 2. Встроенный инлайн-баннер серии дней активности
            if (state.showStreakBanner) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF26261F))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        val preLastDigit = state.streakDays % 100 / 10
                        val lastDigit = state.streakDays % 10
                        val word = if (preLastDigit == 1) {
                            "дней"
                        } else {
                            when (lastDigit) {
                                1 -> "день"
                                2, 3, 4 -> "дня"
                                else -> "дней"
                            }
                        }

                        Text(
                            text = "Вы тренируетесь ${state.streakDays} $word подряд! Так держать!",
                            color = Accent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 3. StatsBar
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(GlassColor)
                        .blur(radius = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "KM", color = TextSecondary, fontSize = scaledSp(12))
                        Text(
                            text = state.distanceKm.formatKm(),
                            color = TextPrimary,
                            fontSize = scaledSp(28),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .border(2.dp, StepCircleBorder, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${state.steps}",
                                color = StepCircleBorder,
                                fontSize = scaledSp(20),
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = "шагов", color = StepCircleBorder, fontSize = scaledSp(11))
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val timeText = "${state.durationMinutes}:${state.durationRemainderSeconds.toString().padStart(2, '0')}"
                        Text(
                            text = timeText,
                            color = TextPrimary,
                            fontSize = scaledSp(28),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(text = "мин", color = TextSecondary, fontSize = scaledSp(12))
                    }
                }
            }

            // 4. Карта и кнопки действий на карте
            Box(modifier = Modifier.weight(1f)) {
                PlatformMapView(
                    modifier = Modifier.fillMaxSize(),
                    userLatitude = state.userLatitude,
                    userLongitude = state.userLongitude,
                    routePoints = state.routePoints,
                    onMyLocationClick = {},
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MapButtonBg.copy(alpha = 0.85f))
                            .clickable(onClick = {}),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(imageVector = Icons.Filled.MyLocation, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(22.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MapButtonBg.copy(alpha = 0.85f))
                            .clickable(onClick = {}),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(imageVector = Icons.Filled.Layers, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }

        // 5. BottomSection
        val label = if (state.isRunning) "ЗАКОНЧИТЬ ТРЕНИРОВКУ" else "НАЧАТЬ ТРЕНИРОВКУ"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassColor)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                    .blur(radius = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Brush.horizontalGradient(listOf(Accent, AccentDim)))
                        .clickable(onClick = onToggleTraining),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        color = Color(0xFF1B1B15),
                        fontSize = scaledSp(16),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    // Пауза
                    val pauseTint = if (state.selectedTab == TrainingTab.PAUSE) TextPrimary else TextDim
                    Column(
                        modifier = Modifier
                            .clickable {
                                onTogglePause()
                                onTabSelected(TrainingTab.PAUSE)
                            }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(imageVector = Icons.Filled.Pause, contentDescription = "Пауза", tint = pauseTint, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = if (state.isPaused) "Продолжить" else "Пауза", color = pauseTint, fontSize = scaledSp(10))
                    }

                    // Местоположение
                    val locTint = if (state.selectedTab == TrainingTab.LOCATION) TextPrimary else TextDim
                    Column(
                        modifier = Modifier
                            .clickable { onTabSelected(TrainingTab.LOCATION) }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(imageVector = Icons.Filled.Place, contentDescription = "Местоположение", tint = locTint, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Местоположение", color = locTint, fontSize = scaledSp(10))
                    }

                    // Статистика
                    val statTint = if (state.selectedTab == TrainingTab.STATISTICS) TextPrimary else TextDim
                    Column(
                        modifier = Modifier
                            .clickable { onTabSelected(TrainingTab.STATISTICS) }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(imageVector = Icons.Filled.BarChart, contentDescription = "Статистика", tint = statTint, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Статистика", color = statTint, fontSize = scaledSp(10))
                    }
                }
            }
        }
    }
}

private fun Double.formatKm(): String {
    val whole = toInt()
    val frac = ((this - whole) * 100).toInt().toString().padStart(2, '0')
    return "$whole.$frac"
}