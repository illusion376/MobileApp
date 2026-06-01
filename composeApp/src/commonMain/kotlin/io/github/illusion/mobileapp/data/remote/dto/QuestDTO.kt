package io.github.illusion.mobileapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestDTO(
    val id: String,
    val title: String,
    val description: String,
    val iconType: String,
    val target: Float,
    val unit: String,
    val rewardXp: Int,
    val isStreak: Boolean,
    val streakTarget: Int,
    val isDaily: Boolean,
    val isActive: Boolean,
    val progress: Float,
    val isCompleted: Boolean,
)
