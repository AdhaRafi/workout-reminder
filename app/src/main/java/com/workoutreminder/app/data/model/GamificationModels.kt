package com.workoutreminder.app.data.model

data class StreakInfo(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val hasWorkedOutToday: Boolean = false
)

data class UserLevelInfo(
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val title: String = "Pemula Bugar",
    val levelMinXp: Int = 0,
    val levelMaxXp: Int = 100,
    val progressFraction: Float = 0f
)

data class BadgeItem(
    val id: String,
    val icon: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val progressText: String
)
