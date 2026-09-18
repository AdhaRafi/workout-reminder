package com.workoutreminder.app

import android.app.Application
import com.workoutreminder.app.alarm.NotificationHelper
import com.workoutreminder.app.data.AppDatabase
import com.workoutreminder.app.data.repository.UserProfileManager
import com.workoutreminder.app.data.repository.WorkoutRepository

class WorkoutApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        WorkoutRepository(
            reminderDao = database.reminderDao(),
            workoutHistoryDao = database.workoutHistoryDao()
        )
    }
    val userProfileManager by lazy { UserProfileManager(this) }

    override fun onCreate() {
        super.onCreate()
        // Buat saluran notifikasi agar alarm siap digunakan
        NotificationHelper.createNotificationChannel(this)
    }
}
