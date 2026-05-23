package io.github.illusion.mobileapp.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.koinInject

private val BgDark = Color(0xFF1B1B15)       // Темный фон приложения (как на экране авторизации)
private val SurfaceCard = Color(0xFF2E2D24)  // Фон для карточек и полей ввода
private val TrackColor = Color(0xFF2B2A21)   // Цвет незаполненных элементов / неактивной кнопки
private val Accent = Color(0xFFBBBD3D)       // Более мягкий, приглушенный желтый цвет
private val AccentDim = Color(0xFF535246)    // Темный оттенок для неактивного текста на кнопке
private val TextPrimary = Color(0xFFEDEDE0)  // Яркий белый текст (для LOGO)
private val TextSecondary = Color(0xFF7E7D73)// Серый текст для подписей и описания
private val DividerColor = Color(0xFFFFFFFF).copy(alpha = 0.07f) // Цвет разделителей в тон карточек
private val DarkOnAccent = Color(0xFF1B1B15) // Темный цвет текста для контраста на желтом фоне
private val ButtonGradientStart = Color(0xFFE2E74A) // Яркий лимонный (верх кнопки)
private val ButtonGradientEnd = Color(0xFFB0B232)   // Приглушенный оливково-желтый (низ кнопк
val GlassColor = Color(0xFFFFFFFF).copy(alpha = 0.03f)

// 2. Граница «стекла», чтобы подчеркнуть форму
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.06f)

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinInject(),
) {
    val state by viewModel.uiState.collectAsState()
    val logout = remember(onLogout) { onLogout }

    MainContent(
        state = state,
        onRefresh = viewModel::loadCharacter,
        onDismissError = viewModel::dismissError,
        onInventoryClick = viewModel::onInventoryClick,
        onQuestsClick = viewModel::onQuestsClick,
        onBossesClick = viewModel::onBossesClick,
        onStartTrainingClick = viewModel::onStartTrainingClick,
        onLogoutClick = logout,
        modifier = modifier,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    state: MainUIState,
    onRefresh: () -> Unit,
    onDismissError: () -> Unit,
    onInventoryClick: () -> Unit,
    onQuestsClick: () -> Unit,
    onBossesClick: () -> Unit,
    onStartTrainingClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .background(BgDark),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
                .padding(top = 8.dp, bottom = 16.dp),
        ) {
            TopBar(onLogoutClick = onLogoutClick)

            state.errorMessage?.let { msg ->
                Spacer(Modifier.height(8.dp))
                ErrorBannerMain(message = msg, onDismiss = onDismissError)
            }

            Spacer(Modifier.height(4.dp))
            TopStatsRow(
                steps = state.todaySteps,
                calories = state.caloriesBurned,
                minutes = state.walkMinutes,
            )
            Spacer(Modifier.height(10.dp))

            DisciplineCard(
                title = state.disciplineTitle,
                subtitle = state.disciplineSubtitle,
            )
            Spacer(Modifier.height(8.dp))

            NextLevelCard(
                nextLevel = state.nextLevel,
                currentXp = state.currentXp,
                xpToNext = state.xpToNextLevel,
                progress = state.xpProgress,
            )
            Spacer(Modifier.height(8.dp))

            StreakCard(
                days = state.weeklyStreakDone,
                currentIndex = state.currentWeekdayIndex,
                streakDays = state.streakDays,
            )
            Spacer(Modifier.height(8.dp))

            StatsCard(stats = state.stats)
            Spacer(Modifier.height(8.dp))

            ActionsRow(
                onInventoryClick = onInventoryClick,
                onQuestsClick = onQuestsClick,
                onBossesClick = onBossesClick,
            )
            Spacer(Modifier.weight(1f, fill = true).heightIn(min = 10.dp))

            StartTrainingButton(onClick = onStartTrainingClick)
        }
    }
}

@Composable
private fun TopBar(onLogoutClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SurfaceCard)
                .clickable(onClick = onLogoutClick),
            contentAlignment = Alignment.Center,
        ) {
            MainIcon(
                type = MainIconType.Close,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun TopStatsRow(steps: Int, calories: Int, minutes: Int) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .matchParentSize() // Заполняет размер родительского Box
                .clip(RoundedCornerShape(12.dp))
                .background(GlassColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                .blur(radius = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiniStat(
                icon = MainIconType.Boot,
                value = steps.toString(),
                label = "ШАГОВ",
                modifier = Modifier.weight(1f),
            )
            ColumnDivider(height = 28.dp)
            MiniStat(
                icon = MainIconType.Fire,
                value = calories.toString(),
                label = "ККАЛ",
                modifier = Modifier.weight(1f),
            )
            ColumnDivider(height = 28.dp)
            MiniStat(
                icon = MainIconType.Clock,
                value = minutes.toString(),
                label = "МИН",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MiniStat(
    icon: MainIconType,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        MainIcon(
            type = icon,
            tint = Accent,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp,
            )
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                lineHeight = 10.sp,
            )
        }
    }
}

@Composable
private fun DisciplineCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard)
            .padding(14.dp),
    ) {
        Text(
            text = "ТВОЙ ПРОГРЕСС СЕГОДНЯ",
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MainIcon(
                type = MainIconType.Emblem,
                tint = Accent,
                modifier = Modifier.size(120.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                )
            }
        }
    }
}

//@Composable
//private fun DisciplineCard(
//    title: String,
//    subtitle: String,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier.fillMaxWidth()
//    ) {
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .clip(RoundedCornerShape(14.dp))
//                .background(GlassColor)
//                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
//                .blur(radius = 16.dp)
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(14.dp),
//        ) {
//            Text(
//                text = "ТВОЙ ПРОГРЕСС СЕГОДНЯ",
//                color = TextPrimary,
//                fontSize = 11.sp,
//                fontWeight = FontWeight.Bold,
//                letterSpacing = 1.5.sp,
//            )
//            Spacer(Modifier.height(10.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                MainIcon(
//                    type = MainIconType.Emblem,
//                    tint = Accent,
//                    modifier = Modifier.size(120.dp),
//                )
//                Spacer(Modifier.width(12.dp))
//                Column(Modifier.weight(1f)) {
//                    Text(
//                        text = title,
//                        color = TextPrimary,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        letterSpacing = 0.5.sp,
//                    )
//                    Spacer(Modifier.height(6.dp))
//                    Text(
//                        text = subtitle,
//                        color = TextSecondary,
//                        fontSize = 12.sp,
//                        lineHeight = 16.sp,
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
private fun NextLevelCard(
    nextLevel: Int,
    currentXp: Int,
    xpToNext: Int,
    progress: Float,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "ДО СЛЕДУЮЩЕГО УРОВНЯ",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Accent)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = "LEVEL $nextLevel",
                    color = DarkOnAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = formatNumber(currentXp),
                color = Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = " / ${formatNumber(xpToNext)} XP",
                color = TextSecondary,
                fontSize = 12.sp,
            )
        }
        Spacer(Modifier.height(6.dp))
        ThinProgress(progress = progress)
    }
}

@Composable
private fun StreakCard(
    days: List<Boolean>,
    currentIndex: Int,
    streakDays: Int,
) {
    val labels = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Text(
            text = "ТВОЯ СЕРИЯ",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            labels.forEachIndexed { idx, label ->
                StreakDay(
                    label = label,
                    isCompleted = days.getOrNull(idx) == true,
                    isCurrent = idx == currentIndex,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$streakDays дней подряд! Продолжай!",
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StreakDay(label: String, isCompleted: Boolean, isCurrent: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(AccentDim)
                .then(
                    if (isCurrent) Modifier.border(1.5.dp, Accent, CircleShape) else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            when {
                isCompleted -> CheckMark(
                    color = Accent,
                    modifier = Modifier.size(16.dp),
                )

                isCurrent -> Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Accent),
                )
            }
        }
    }
}

@Composable
private fun CheckMark(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val u = minOf(size.width, size.height) / 24f
        val w = 2.6f * u
        drawLine(
            color = color,
            start = Offset(5f * u, 12.5f * u),
            end = Offset(10.5f * u, 17.5f * u),
            strokeWidth = w,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(10.5f * u, 17.5f * u),
            end = Offset(19f * u, 7.5f * u),
            strokeWidth = w,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun StatsCard(stats: PlayerStats) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .background(GlassColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                .blur(radius = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatItem(
                icon = MainIconType.Sword,
                label = "СТР",
                value = stats.strength,
                modifier = Modifier.weight(1f),
            )
            ColumnDivider(height = 24.dp)
            StatItem(
                icon = MainIconType.Shield,
                label = "ВИТ",
                value = stats.vitality,
                modifier = Modifier.weight(1f),
            )
            ColumnDivider(height = 24.dp)
            StatItem(
                icon = MainIconType.Sparkle,
                label = "СТА",
                value = stats.stamina,
                modifier = Modifier.weight(1f),
            )
        }
    }
}


@Composable
private fun StatItem(
    icon: MainIconType,
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MainIcon(
            type = icon,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = value.toString(),
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Composable
private fun ActionsRow(
    onInventoryClick: () -> Unit,
    onQuestsClick: () -> Unit,
    onBossesClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Слой 1: Размытая стеклянная подложка
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .background(GlassColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                .blur(radius = 16.dp)
        )

        // Слой 2: Четкие интерактивные кнопки поверх размытия
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionTile(
                label = "ИНВЕНТАРЬ",
                icon = MainIconType.Backpack,
                onClick = onInventoryClick,
                modifier = Modifier.weight(1f),
            )

            ColumnDivider(height = 24.dp) // Уменьшили высоту разделителя до 24.dp для аккуратного вида

            ActionTile(
                label = "КВЕСТЫ",
                icon = MainIconType.Clipboard,
                onClick = onQuestsClick,
                modifier = Modifier.weight(1f),
            )

            ColumnDivider(height = 24.dp)

            ActionTile(
                label = "БОССЫ",
                icon = MainIconType.Crown,
                onClick = onBossesClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}


@Composable
private fun ActionTile(
    label: String,
    icon: MainIconType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MainIcon(
            type = icon,
            tint = TextPrimary,
            modifier = Modifier.size(26.dp),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ColumnDivider(height: Dp) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(DividerColor),
    )
}

@Composable
private fun StartTrainingButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFFFFFFFF).copy(alpha = 0.02f))
                .border(1.dp, Color(0xFFFFFFFF).copy(alpha = 0.08f), RoundedCornerShape(26.dp))
                .blur(radius = 8.dp)
        )

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = DarkOnAccent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE2E74A),
                            Color(0xFFB0B232)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(26.dp)
                ),
        ) {
            MainIcon(
                type = MainIconType.Run,
                tint = DarkOnAccent,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "НАЧАТЬ ТРЕНИРОВКУ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
            )
        }
    }
}

@Composable
private fun ThinProgress(progress: Float) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(50)),
        color = Accent,
        trackColor = TrackColor,
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {},
    )
}

private fun formatNumber(value: Int): String {
    if (value < 1_000) return value.toString()
    val s = value.toString()
    val sb = StringBuilder(s.length + s.length / 3)
    var count = 0
    for (i in s.indices.reversed()) {
        sb.append(s[i])
        count++
        if (count % 3 == 0 && i != 0) sb.append(',')
    }
    return sb.reverse().toString()
}
