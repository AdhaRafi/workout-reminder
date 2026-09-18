package com.workoutreminder.app.data.repository

import com.workoutreminder.app.data.dao.ReminderDao
import com.workoutreminder.app.data.dao.WorkoutHistoryDao
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class WorkoutRepository(
    private val reminderDao: ReminderDao,
    private val workoutHistoryDao: WorkoutHistoryDao
) {
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    suspend fun getActiveReminders(): List<ReminderEntity> {
        return reminderDao.getActiveReminders()
    }

    suspend fun getReminderById(id: Int): ReminderEntity? {
        return reminderDao.getReminderById(id)
    }

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun deleteReminderById(id: Int) {
        reminderDao.deleteReminderById(id)
    }

    suspend fun updateActiveStatus(id: Int, isActive: Boolean) {
        reminderDao.updateActiveStatus(id, isActive)
    }

    suspend fun recordCompletion(splitName: String, reminderId: Int? = null): Long {
        val history = WorkoutHistoryEntity(
            splitName = splitName,
            completedAt = System.currentTimeMillis(),
            reminderId = reminderId
        )
        return workoutHistoryDao.insertHistory(history)
    }

    /**
     * Mengambil riwayat latihan mulai dari awal minggu ini (Senin 00:00:00).
     */
    fun getThisWeekHistories(): Flow<List<WorkoutHistoryEntity>> {
        val startOfWeek = LocalDate.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return workoutHistoryDao.getHistoriesSince(startOfWeek)
    }

    fun getAllHistories(): Flow<List<WorkoutHistoryEntity>> {
        return workoutHistoryDao.getAllHistories()
    }

    suspend fun deleteHistory(id: Int) {
        workoutHistoryDao.deleteHistoryById(id)
    }
}
