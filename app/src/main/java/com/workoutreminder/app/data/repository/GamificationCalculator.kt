package com.workoutreminder.app.data.repository

import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity
import com.workoutreminder.app.data.model.BadgeItem
import com.workoutreminder.app.data.model.StreakInfo
import com.workoutreminder.app.data.model.UserLevelInfo
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object GamificationCalculator {

    fun calculateStreak(histories: List<WorkoutHistoryEntity>): StreakInfo {
        if (histories.isEmpty()) return StreakInfo(0, 0, false)

        val zoneId = ZoneId.systemDefault()
        val workoutDates = histories
            .map { Instant.ofEpochMilli(it.completedAt).atZone(zoneId).toLocalDate() }
            .toSet()

        val today = LocalDate.now(zoneId)
        val hasWorkedOutToday = workoutDates.contains(today)

        var currentStreak = 0
        var checkDate = if (hasWorkedOutToday) today else today.minusDays(1)

        while (workoutDates.contains(checkDate)) {
            currentStreak++
            checkDate = checkDate.minusDays(1)
        }

        // Hitung rekor terbaik (best streak)
        val sortedDates = workoutDates.sorted()
        var bestStreak = 0
        var tempStreak = 0
        var prevDate: LocalDate? = null

        for (date in sortedDates) {
            if (prevDate == null || date == prevDate.plusDays(1)) {
                tempStreak++
            } else {
                tempStreak = 1
            }
            if (tempStreak > bestStreak) {
                bestStreak = tempStreak
            }
            prevDate = date
        }

        if (currentStreak > bestStreak) {
            bestStreak = currentStreak
        }

        return StreakInfo(
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            hasWorkedOutToday = hasWorkedOutToday
        )
    }

    fun calculateLevel(historiesCount: Int): UserLevelInfo {
        val totalXp = historiesCount * 50

        val (level, title, minXp, maxXp) = when {
            totalXp < 100 -> Quad(1, "Pemula Bugar", 0, 100)
            totalXp < 250 -> Quad(2, "Konsisten Berlatih", 100, 250)
            totalXp < 500 -> Quad(3, "Pejuang Besi", 250, 500)
            totalXp < 1000 -> Quad(4, "Atlet Tangguh", 500, 1000)
            totalXp < 2000 -> Quad(5, "Titan Kebugaran", 1000, 2000)
            else -> Quad(6, "Legenda Gym 👑", 2000, 5000)
        }

        val range = maxXp - minXp
        val progress = if (range > 0) {
            ((totalXp - minXp).toFloat() / range).coerceIn(0f, 1f)
        } else 1f

        return UserLevelInfo(
            totalXp = totalXp,
            currentLevel = level,
            title = title,
            levelMinXp = minXp,
            levelMaxXp = maxXp,
            progressFraction = progress
        )
    }

    fun calculateBadges(
        histories: List<WorkoutHistoryEntity>,
        reminders: List<ReminderEntity>,
        targetWorkoutsPerWeek: Int,
        thisWeekCount: Int
    ): List<BadgeItem> {
        val totalCount = histories.size
        val streakInfo = calculateStreak(histories)
        val zoneId = ZoneId.systemDefault()

        val hasEarlyBird = histories.any {
            val localTime = Instant.ofEpochMilli(it.completedAt).atZone(zoneId).toLocalTime()
            localTime.isBefore(LocalTime.of(8, 0))
        }

        val distinctSplitsCount = reminders.map { it.splitName.trim().lowercase() }.distinct().size

        return listOf(
            BadgeItem(
                id = "first_step",
                icon = "🌟",
                title = "Langkah Pertama",
                description = "Selesaikan latihan pertamamu dan catat di aplikasi.",
                isUnlocked = totalCount >= 1,
                progressText = if (totalCount >= 1) "Selesai" else "$totalCount/1"
            ),
            BadgeItem(
                id = "streak_3",
                icon = "🔥",
                title = "Konsisten 3 Hari",
                description = "Latihan 3 hari berturut-turut tanpa terputus.",
                isUnlocked = streakInfo.bestStreak >= 3,
                progressText = "${streakInfo.currentStreak}/3 hari"
            ),
            BadgeItem(
                id = "streak_7",
                icon = "⚡",
                title = "Pejuang Besi 7 Hari",
                description = "Streak latihan spektakuler selama 1 minggu penuh.",
                isUnlocked = streakInfo.bestStreak >= 7,
                progressText = "${streakInfo.currentStreak}/7 hari"
            ),
            BadgeItem(
                id = "split_master",
                icon = "👑",
                title = "Master Variasi",
                description = "Membuat minimal 3 jenis split jadwal latihan berbeda.",
                isUnlocked = distinctSplitsCount >= 3,
                progressText = "$distinctSplitsCount/3 split"
            ),
            BadgeItem(
                id = "workout_10",
                icon = "🏆",
                title = "Klub 10 Sesi",
                description = "Mencapai total 10 sesi latihan yang terselesaikan.",
                isUnlocked = totalCount >= 10,
                progressText = "$totalCount/10 sesi"
            ),
            BadgeItem(
                id = "workout_25",
                icon = "💎",
                title = "Dedikasi 25 Sesi",
                description = "Dedikasi tinggi dengan 25 sesi latihan lengkap.",
                isUnlocked = totalCount >= 25,
                progressText = "$totalCount/25 sesi"
            ),
            BadgeItem(
                id = "early_bird",
                icon = "🌅",
                title = "Pejuang Pagi",
                description = "Menyelesaikan sesi latihan sebelum pukul 08:00 pagi.",
                isUnlocked = hasEarlyBird,
                progressText = if (hasEarlyBird) "Tercapai" else "Sebelum 08:00"
            ),
            BadgeItem(
                id = "weekly_goal",
                icon = "🎯",
                title = "Target Mingguan",
                description = "Mencapai target $targetWorkoutsPerWeek sesi latihan dalam seminggu.",
                isUnlocked = thisWeekCount >= targetWorkoutsPerWeek,
                progressText = "$thisWeekCount/$targetWorkoutsPerWeek sesi"
            )
        )
    }

    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
