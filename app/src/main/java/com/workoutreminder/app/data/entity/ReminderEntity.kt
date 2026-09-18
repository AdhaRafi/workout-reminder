package com.workoutreminder.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas penyimpan data pengingat latihan harian/mingguan.
 * [daysOfWeek] berisi daftar integer hari: 1 = Senin, 2 = Selasa, ..., 7 = Minggu.
 */
@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val splitName: String,
    val isCustomSplit: Boolean = false,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: List<Int>,
    val isActive: Boolean = true,
    val useSoundAlarm: Boolean = true,
    val alarmSoundName: String = "Energetic Gym Beat",
    val alarmSoundUri: String? = null
) {
    /**
     * Memformat jam dan menit menjadi string HH:mm (contoh: "07:05").
     */
    val formattedTime: String
        get() = String.format("%02d:%02d", hour, minute)
}
