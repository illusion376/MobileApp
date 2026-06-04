package io.github.illusion.mobileapp.ui.screens.training

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import io.github.illusion.mobileapp.ui.screens.themes.scaledSp

private val BgDark = Color(0xFF1B1B15)
private val CardColor = Color(0xFF2E2D24)
private val Accent = Color(0xFFE2D566)
private val AccentDim = Color(0xFFAD9B2A)
private val TextPrimary = Color(0xFFDBDBDB)
private val TextSecondary = Color(0xFF999999)
private val TextDim = Color(0xFF7D7D69)
private val TrackColor = Color(0xFF2B2A21)
private val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.10f)
private val DarkOnAccent = Color(0xFF1B1B15)

@Composable
fun TrainingCompletionDialog(
    result: TrainingResult,
    onConfirm: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable(onClick = onConfirm),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(CardColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                .clickable(enabled = false, onClick = {})
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SuccessBadge()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Готово!",
                color = TextPrimary,
                fontSize = scaledSp(32),
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Хорошая работа",
                color = TextSecondary,
                fontSize = scaledSp(16),
            )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                RewardStat(
                    value = "+${result.gainedXp} XP",
                    label = "Получено опыта",
                    valueColor = Accent,
                )
                RewardStat(
                    value = "${result.level}",
                    label = "Текущий уровень",
                    valueColor = TextPrimary,
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "${result.currentXp} / ${result.xpToNextLevel} XP до ${result.nextLevel} уровня",
                color = TextSecondary,
                fontSize = scaledSp(14),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            XpProgressBar(
                fromProgress = result.previousProgress,
                toProgress = result.progress,
            )

            Spacer(Modifier.height(28.dp))

            // Кнопка «ОТЛИЧНО»
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Accent, AccentDim))
                    )
                    .clickable(onClick = onConfirm),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "ОТЛИЧНО",
                    color = DarkOnAccent,
                    fontSize = scaledSp(16),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun XpProgressBar(
    fromProgress: Float,
    toProgress: Float,
) {
    var animationStarted by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) toProgress else fromProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "xpProgress",
    )

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(TrackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Brush.horizontalGradient(listOf(AccentDim, Accent))
                ),
        )
    }
}

@Composable
private fun RewardStat(
    value: String,
    label: String,
    valueColor: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = valueColor,
            fontSize = scaledSp(28),
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = TextDim,
            fontSize = scaledSp(13),
        )
    }
}

@Composable
private fun SuccessBadge() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(AccentDim.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(3.dp, Accent, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            CompletionCheck(
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
private fun CompletionCheck(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val u = minOf(w, h) / 24f
        val strokeWidth = 2.5f * u

        drawLine(
            color = Accent,
            start = Offset(x = 5f * u, y = 13f * u),
            end = Offset(x = 10f * u, y = 18f * u),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Accent,
            start = Offset(x = 10f * u, y = 18f * u),
            end = Offset(x = 19f * u, y = 6f * u),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}