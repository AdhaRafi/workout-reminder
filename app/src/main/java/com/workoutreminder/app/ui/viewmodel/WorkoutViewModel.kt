package com.workoutreminder.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.workoutreminder.app.alarm.AlarmScheduler
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity
import com.workoutreminder.app.data.model.BadgeItem
import com.workoutreminder.app.data.model.StreakInfo
import com.workoutreminder.app.data.model.UserLevelInfo
import com.workoutreminder.app.data.model.UserProfile
import com.workoutreminder.app.data.repository.GamificationCalculator
import com.workoutreminder.app.data.repository.UserProfileManager
import com.workoutreminder.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutViewModel(
    application: Application,
    private val repository: WorkoutRepository,
    private val userProfileManager: UserProfileManager
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // Profil Pengguna
    val userProfile: StateFlow<UserProfile> = userProfileManager.userProfile

    // Mengurutkan pengingat berdasarkan jadwal terdekat
    val reminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .map { list ->
            val now = System.currentTimeMillis()
            list.sortedWith(
                compareBy<ReminderEntity> { !it.isActive }
                    .thenBy {
                        AlarmScheduler.calculateNextTrigger(
                            it.hour,
                            it.minute,
                            it.daysOfWeek,
                            now
                        ) ?: Long.MAX_VALUE
                    }
                    .thenBy { it.hour }
                    .thenBy { it.minute }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Riwayat latihan minggu ini
    val thisWeekHistories: StateFlow<List<WorkoutHistoryEntity>> = repository.getThisWeekHistories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Seluruh riwayat latihan sepanjang waktu
    val allHistories: StateFlow<List<WorkoutHistoryEntity>> = repository.getAllHistories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Streak kalkulasi
    val streakInfo: StateFlow<StreakInfo> = allHistories
        .map { GamificationCalculator.calculateStreak(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StreakInfo()
        )

    // Level & XP kalkulasi
    val userLevelInfo: StateFlow<UserLevelInfo> = allHistories
        .map { GamificationCalculator.calculateLevel(it.size) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserLevelInfo()
        )

    // Lencana Prestasi (Badges)
    val badges: StateFlow<List<BadgeItem>> = combine(
        allHistories,
        reminders,
        userProfile,
        thisWeekHistories
    ) { histories, rems, profile, weekHistories ->
        GamificationCalculator.calculateBadges(
            histories = histories,
            reminders = rems,
            targetWorkoutsPerWeek = profile.targetWorkoutsPerWeek,
            thisWeekCount = weekHistories.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            if (reminder.id == 0) {
                val newId = repository.insertReminder(reminder).toInt()
                val updatedReminder = reminder.copy(id = newId)
                if (updatedReminder.isActive) {
                    AlarmScheduler.schedule(context, updatedReminder)
                }
            } else {
                repository.updateReminder(reminder)
                if (reminder.isActive) {
                    AlarmScheduler.schedule(context, reminder)
                } else {
                    AlarmScheduler.cancel(context, reminder.id)
                }
            }
        }
    }

    fun toggleReminderActive(reminder: ReminderEntity, isActive: Boolean) {
        viewModelScope.launch {
            repository.updateActiveStatus(reminder.id, isActive)
            val updated = reminder.copy(isActive = isActive)
            if (isActive) {
                AlarmScheduler.schedule(context, updated)
            } else {
                AlarmScheduler.cancel(context, reminder.id)
            }
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            AlarmScheduler.cancel(context, reminder.id)
            repository.deleteReminder(reminder)
        }
    }

    fun markWorkoutCompleted(splitName: String, reminderId: Int? = null) {
        viewModelScope.launch {
            repository.recordCompletion(splitName, reminderId)
        }
    }

    fun deleteHistory(id: Int) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        userProfileManager.saveProfile(profile)
    }

    fun setThemeMode(themeMode: String) {
        userProfileManager.saveThemeMode(themeMode)
    }
}
