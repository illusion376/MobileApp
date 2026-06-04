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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var recenterTrigger by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onBack = onBack)

            StatsBar(state = state)

            Box(modifier = Modifier.weight(1f)) {
                PlatformMapView(
                    modifier = Modifier.fillMaxSize(),
                    userLatitude = state.userLatitude,
                    userLongitude = state.userLongitude,
                    routePoints = state.routePoints,
                    recenterTrigger = recenterTrigger,
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    MapActionButton(
                        icon = Icons.Filled.MyLocation,
                        onClick = { recenterTrigger++ },
                    )
                    MapActionButton(
                        icon = Icons.Filled.Layers,
                        onClick = {},
                    )
                }
            }
        }

        BottomSection(
            state = state,
            onToggleTraining = viewModel::toggleTraining,
            onTogglePause = viewModel::togglePause,
            onTabSelected = viewModel::selectTab,
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        state.completion?.let { result ->
            TrainingCompletionDialog(
                result = result,
                onConfirm = {
                    viewModel.dismissCompletion()
                    onBack()
                },
            )
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(BgGradientTop, BgGradientBottom))
            )
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
}

@Composable
private fun StatsBar(state: TrainingUIState) {
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
                Text(
                    text = "KM",
                    color = TextSecondary,
                    fontSize = scaledSp(12),
                )
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
                    Text(
                        text = "шагов",
                        color = StepCircleBorder,
                        fontSize = scaledSp(11),
                    )
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
                Text(
                    text = "мин",
                    color = TextSecondary,
                    fontSize = scaledSp(12),
                )
            }
        }
    }
}

@Composable
private fun MapActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MapButtonBg.copy(alpha = 0.85f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun BottomSection(
    state: TrainingUIState,
    onToggleTraining: () -> Unit,
    onTogglePause: () -> Unit,
    onTabSelected: (TrainingTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = if (state.isRunning) "ЗАКОНЧИТЬ ТРЕНИРОВКУ" else "НАЧАТЬ ТРЕНИРОВКУ"
    Box(
        modifier = modifier
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
                    .background(
                        Brush.horizontalGradient(listOf(Accent, AccentDim))
                    )
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
                BottomNavItem(
                    icon = Icons.Filled.Pause,
                    label = if (state.isPaused) "Продолжить" else "Пауза",
                    isSelected = state.selectedTab == TrainingTab.PAUSE,
                    onClick = {
                        onTogglePause()
                        onTabSelected(TrainingTab.PAUSE)
                    },
                )
                BottomNavItem(
                    icon = Icons.Filled.Place,
                    label = "Местоположение",
                    isSelected = state.selectedTab == TrainingTab.LOCATION,
                    onClick = { onTabSelected(TrainingTab.LOCATION) },
                )
                BottomNavItem(
                    icon = Icons.Filled.BarChart,
                    label = "Статистика",
                    isSelected = state.selectedTab == TrainingTab.STATISTICS,
                    onClick = { onTabSelected(TrainingTab.STATISTICS) },
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (isSelected) TextPrimary else TextDim
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = scaledSp(10),
        )
    }
}

private fun Double.formatKm(): String {
    val whole = toInt()
    val frac = ((this - whole) * 100).toInt().toString().padStart(2, '0')
    return "$whole.$frac"
}