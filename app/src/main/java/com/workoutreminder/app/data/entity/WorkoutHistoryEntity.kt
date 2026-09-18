package com.workoutreminder.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas penyimpan riwayat latihan yang berhasil diselesaikan.
 * [completedAt] menyimpan timestamp Epoch millis saat latihan ditandai selesai.
 */
@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val splitName: String,
    val completedAt: Long = System.currentTimeMillis(),
    val reminderId: Int? = null
)
