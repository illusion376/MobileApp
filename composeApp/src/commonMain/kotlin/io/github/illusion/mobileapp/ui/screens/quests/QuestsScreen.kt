package io.github.illusion.mobileapp.ui.screens.quests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.illusion.mobileapp.ui.screens.main.MainIcon
import io.github.illusion.mobileapp.ui.screens.main.MainIconType
import io.github.illusion.mobileapp.ui.screens.themes.scaledSp
import org.jetbrains.compose.resources.painterResource
import io.github.illusion.mobileapp.resources.Res
import io.github.illusion.mobileapp.resources.ic_crown
import org.koin.compose.koinInject

private val BgDark = Color(0xFF1B1B15)
private val SurfaceCard = Color(0xFF2E2D24)
private val Accent = Color(0xFFE2D566)
private val AccentDim = Color(0xFFAD9B2A)
private val TextPrimary = Color(0xFFDBDBDB)
private val TextSecondary = Color(0xFF999999)
private val TextDim = Color(0xFF7D7D69)
private val DarkOnAccent = Color(0xFF1B1B15)
private val GlassColor = Color(0xFF2E2D24).copy(alpha = 0.90f)
private val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.12f)
private val TrackColor = Color(0xFF2B2A21)

@Composable
fun QuestsScreen(
    onBack: () -> Unit,
    onInventoryClick: () -> Unit = {},
    onBossesClick: () -> Unit = {},
    viewModel: QuestsViewModel = koinInject(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF333329), BgDark)
                )
            )
    ) {
        TopBar(onBack = onBack)

        TabRow(
            selectedTab = state.selectedTab,
            onTabSelected = viewModel::selectTab,
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.selectedTab == QuestsTab.AVAILABLE) {
                Text(
                    text = "Можно принять только один квест",
                    color = TextDim,
                    fontSize = scaledSp(12),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }

            val visibleQuests = when (state.selectedTab) {
                QuestsTab.AVAILABLE -> state.quests.filter { !it.isActive }
                QuestsTab.ACTIVE -> state.quests.filter { it.isActive }
            }

            val hasActiveQuest = state.quests.any { it.isActive }

            visibleQuests.forEach { quest ->
                QuestCard(
                    quest = quest,
                    showActivate = state.selectedTab == QuestsTab.AVAILABLE,
                    canActivate = !hasActiveQuest,
                    onActivate = { viewModel.activateQuest(quest.id) },
                )
            }

            if (state.selectedTab == QuestsTab.ACTIVE) {
                WeeklyGoalCard(state.weeklyGoal)
            }

            Spacer(Modifier.height(8.dp))
        }

        BottomNav(
            onInventoryClick = onInventoryClick,
            onQuestsClick = {},
            onBackClick = onBack,
        )
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = "Квесты",
            color = TextPrimary,
            fontSize = scaledSp(20),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .border(1.dp, GlassBorder, CircleShape)
                .background(GlassColor)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "\u24D8",
                color = TextSecondary,
                fontSize = scaledSp(14),
            )
        }
    }
}

@Composable
private fun TabRow(
    selectedTab: QuestsTab,
    onTabSelected: (QuestsTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TabButton(
            text = "ДОСТУПНЫЕ",
            selected = selectedTab == QuestsTab.AVAILABLE,
            onClick = { onTabSelected(QuestsTab.AVAILABLE) },
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(8.dp))
        TabButton(
            text = "АКТИВНЫЕ",
            selected = selectedTab == QuestsTab.ACTIVE,
            onClick = { onTabSelected(QuestsTab.ACTIVE) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) Accent else GlassColor
    val textColor = if (selected) DarkOnAccent else TextSecondary
    val borderColor = if (selected) Accent else GlassBorder

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = scaledSp(12),
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}

@Composable
private fun QuestCard(
    quest: QuestItem,
    showActivate: Boolean,
    canActivate: Boolean = true,
    onActivate: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(GlassColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                .blur(radius = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QuestBadge(quest.iconType)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quest.title,
                    color = TextPrimary,
                    fontSize = scaledSp(14),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = quest.description,
                    color = TextSecondary,
                    fontSize = scaledSp(11),
                )
                Spacer(Modifier.height(8.dp))

                if (quest.isActive) {
                    if (quest.isStreak) {
                        StreakDots(
                            done = quest.streakDays,
                            total = quest.streakTarget,
                        )
                    } else {
                        LinearProgressIndicator(
                            progress = { quest.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Accent,
                            trackColor = TrackColor,
                            strokeCap = StrokeCap.Round,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = quest.progressText,
                        color = TextDim,
                        fontSize = scaledSp(11),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            if (showActivate) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "+${quest.rewardXp} XP",
                        color = Accent,
                        fontSize = scaledSp(11),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (canActivate) {
                                    Brush.horizontalGradient(listOf(Accent, AccentDim))
                                } else {
                                    Brush.horizontalGradient(listOf(TrackColor, TrackColor))
                                }
                            )
                            .then(
                                if (canActivate) Modifier.clickable(onClick = onActivate)
                                else Modifier
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "ПРИНЯТЬ",
                            color = if (canActivate) DarkOnAccent else TextDim,
                            fontSize = scaledSp(10),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "+${quest.rewardXp} XP",
                        color = Accent,
                        fontSize = scaledSp(13),
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = ">",
                    color = TextDim,
                    fontSize = scaledSp(16),
                )
            }
        }
    }
}

@Composable
private fun QuestBadge(iconType: QuestIconType) {
    val mainIcon = when (iconType) {
        QuestIconType.BOOT -> MainIconType.Boot
        QuestIconType.FIRE -> MainIconType.Fire
        QuestIconType.SHIELD -> MainIconType.Shield
        QuestIconType.TARGET -> MainIconType.Target
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, AccentDim.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        MainIcon(
            type = mainIcon,
            tint = Accent,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun StreakDots(done: Int, total: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 0 until total) {
            val isDone = i < done
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isDone) Accent else TrackColor)
                    .border(1.dp, if (isDone) Accent else GlassBorder, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (isDone) {
                    Text(
                        text = "\u2713",
                        color = DarkOnAccent,
                        fontSize = scaledSp(14),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyGoalCard(goal: WeeklyGoal) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(GlassColor)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                .blur(radius = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_crown),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ЕЖЕНЕДЕЛЬНАЯ ЦЕЛЬ",
                    color = TextPrimary,
                    fontSize = scaledSp(13),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Выполни ${goal.totalQuests} квестов на этой неделе\nи получи бонус!",
                    color = TextSecondary,
                    fontSize = scaledSp(10),
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (i in 0 until goal.totalQuests) {
                        val isDone = i < goal.completedQuests
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isDone) Accent else TrackColor)
                                .border(1.dp, if (isDone) Accent else GlassBorder, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (isDone) {
                                Text(
                                    text = "\u2713",
                                    color = DarkOnAccent,
                                    fontSize = scaledSp(12),
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                        if (i < goal.totalQuests - 1) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(2.dp)
                                    .background(if (isDone) Accent else TrackColor)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCard)
                    .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                MainIcon(
                    type = MainIconType.Backpack,
                    tint = Accent,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "БОНУС",
                    color = TextSecondary,
                    fontSize = scaledSp(8),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "+${goal.bonusXp} XP",
                    color = Accent,
                    fontSize = scaledSp(11),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun BottomNav(
    onInventoryClick: () -> Unit,
    onQuestsClick: () -> Unit,
    onBackClick: () -> Unit,
) {
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
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavItem(
                icon = MainIconType.Backpack,
                label = "ИНВЕНТАРЬ",
                selected = false,
                onClick = onInventoryClick,
            )
            NavItem(
                icon = MainIconType.Clipboard,
                label = "КВЕСТЫ",
                selected = true,
                onClick = onQuestsClick,
            )
            NavItem(
                icon = MainIconType.Close,
                label = "НАЗАД",
                selected = false,
                onClick = onBackClick,
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: MainIconType,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (selected) Accent else TextDim
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        MainIcon(
            type = icon,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = tint,
            fontSize = scaledSp(9),
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}
