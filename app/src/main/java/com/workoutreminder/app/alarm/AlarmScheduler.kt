package com.workoutreminder.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.workoutreminder.app.data.entity.ReminderEntity
import java.time.LocalDateTime
import java.time.ZoneId

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"
    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_SPLIT_NAME = "extra_split_name"

    /**
     * Menghitung kapan waktu berikutnya pengingat harus dibunyikan.
     * Mengembalikan Epoch millis, atau null jika tidak ada hari aktif.
     */
    fun calculateNextTrigger(
        hour: Int,
        minute: Int,
        daysOfWeek: List<Int>,
        afterMillis: Long = System.currentTimeMillis()
    ): Long? {
        if (daysOfWeek.isEmpty()) return null

        val afterDateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(afterMillis),
            ZoneId.systemDefault()
        )

        // Cek mulai dari hari ini hingga 8 hari ke depan untuk menemukan jadwal terdekat
        for (dayOffset in 0L..8L) {
            val candidateDate = afterDateTime.plusDays(dayOffset)
            val dayValue = candidateDate.dayOfWeek.value // 1 = Senin, 7 = Minggu

            if (daysOfWeek.contains(dayValue)) {
                val candidateDateTime = candidateDate
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0)

                if (candidateDateTime.isAfter(afterDateTime)) {
                    return candidateDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }
            }
        }
        return null
    }

    /**
     * Mendaftarkan alarm untuk [reminder] ke AlarmManager.
     */
    fun schedule(context: Context, reminder: ReminderEntity) {
        if (!reminder.isActive || reminder.daysOfWeek.isEmpty()) {
            cancel(context, reminder.id)
            return
        }

        val nextTriggerMillis = calculateNextTrigger(
            hour = reminder.hour,
            minute = reminder.minute,
            daysOfWeek = reminder.daysOfWeek
        ) ?: return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, WorkoutAlarmReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_SPLIT_NAME, reminder.splitName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextTriggerMillis,
                        pendingIntent
                    )
                } else {
                    // Fallback jika izin exact alarm belum aktif
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextTriggerMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Alarm dijadwalkan untuk ${reminder.splitName} pada millis: $nextTriggerMillis")
        } catch (e: SecurityException) {
            Log.e(TAG, "Gagal mengatur exact alarm (SecurityException): ${e.message}")
        }
    }

    /**
     * Membatalkan alarm untuk ID tertentu.
     */
    fun cancel(context: Context, reminderId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WorkoutAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Alarm dibatalkan untuk ID: $reminderId")
        }
    }
}
