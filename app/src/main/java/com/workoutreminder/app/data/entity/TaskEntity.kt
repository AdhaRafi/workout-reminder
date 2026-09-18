package com.workoutreminder.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Entitas untuk menyimpan task/tugas pengguna.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: String = "Umum",
    val dueDate: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime? = null
) {
    val formattedDueDate: String?
        get() = dueDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))

    val formattedCreatedAt: String
        get() = createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    val isOverdue: Boolean
        get() = dueDate?.isBefore(LocalDateTime.now()) == true && !isCompleted
}

enum class TaskPriority(val displayName: String, val color: Long) {
    LOW("Rendah", 0xFF4CAF50),
    MEDIUM("Sedang", 0xFFFF9800),
    HIGH("Tinggi", 0xFFF44336),
    URGENT("Mendesak", 0xFFD32F2F)
}
