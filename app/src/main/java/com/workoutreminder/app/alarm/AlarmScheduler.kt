package com.workoutreminder.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.workoutreminder.app.MainActivity
import com.workoutreminder.app.data.entity.ReminderEntity
import java.time.LocalDateTime
import java.time.ZoneId

object AlarmScheduler {

    private const val TAG = "AlarmScheduler"
    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_ORIGINAL_REMINDER_ID = "extra_original_reminder_id"
    const val EXTRA_SPLIT_NAME = "extra_split_name"
    const val EXTRA_USE_SOUND_ALARM = "extra_use_sound_alarm"
    const val EXTRA_ALARM_SOUND_NAME = "extra_alarm_sound_name"
    const val EXTRA_ALARM_SOUND_URI = "extra_alarm_sound_uri"
    const val EXTRA_IS_SNOOZE = "extra_is_snooze"

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
     * Mendaftarkan alarm reguler untuk [reminder] ke AlarmManager.
     * Menggunakan setAlarmClock agar diakui Android sebagai Alarm Resmi (kebal Doze Mode & hemat baterai).
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
            putExtra(EXTRA_ORIGINAL_REMINDER_ID, reminder.id)
            putExtra(EXTRA_SPLIT_NAME, reminder.splitName)
            putExtra(EXTRA_USE_SOUND_ALARM, reminder.useSoundAlarm)
            putExtra(EXTRA_ALARM_SOUND_NAME, reminder.alarmSoundName)
            putExtra(EXTRA_ALARM_SOUND_URI, reminder.alarmSoundUri)
            putExtra(EXTRA_IS_SNOOZE, false)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = Intent(context, MainActivity::class.java)
        val showPendingIntent = PendingIntent.getActivity(
            context,
            reminder.id + 500,
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    val alarmClockInfo = AlarmManager.AlarmClockInfo(nextTriggerMillis, showPendingIntent)
                    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextTriggerMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val alarmClockInfo = AlarmManager.AlarmClockInfo(nextTriggerMillis, showPendingIntent)
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTriggerMillis, pendingIntent)
            }
            Log.d(TAG, "Alarm dijadwalkan untuk ${reminder.splitName} pada millis: $nextTriggerMillis")
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengatur alarm dengan setAlarmClock: ${e.message}, mencoba fallback...")
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerMillis,
                    pendingIntent
                )
            } catch (fallbackEx: Exception) {
                Log.e(TAG, "Fallback alarm juga gagal: ${fallbackEx.message}")
            }
        }
    }

    /**
     * Menjadwalkan alarm tunda (snooze) selama beberapa menit dari sekarang.
     */
    fun scheduleSnooze(
        context: Context,
        reminderId: Int,
        splitName: String,
        snoozeMinutes: Int = 5,
        useSoundAlarm: Boolean = true,
        soundName: String = "Energetic Gym Beat",
        soundUri: String? = null
    ) {
        val triggerMillis = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000L)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val snoozeReminderId = if (reminderId > 0) reminderId + 90000 else 99999

        val intent = Intent(context, WorkoutAlarmReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_ID, snoozeReminderId)
            putExtra(EXTRA_ORIGINAL_REMINDER_ID, reminderId)
            putExtra(EXTRA_SPLIT_NAME, splitName)
            putExtra(EXTRA_USE_SOUND_ALARM, useSoundAlarm)
            putExtra(EXTRA_ALARM_SOUND_NAME, soundName)
            putExtra(EXTRA_ALARM_SOUND_URI, soundUri)
            putExtra(EXTRA_IS_SNOOZE, true)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            snoozeReminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val showIntent = Intent(context, MainActivity::class.java)
        val showPendingIntent = PendingIntent.getActivity(
            context,
            snoozeReminderId + 100,
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerMillis, showPendingIntent)
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
            Log.d(TAG, "Alarm snooze dijadwalkan untuk $splitName dalam $snoozeMinutes menit pada millis: $triggerMillis")
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengatur alarm snooze: ${e.message}")
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            } catch (_: Exception) {}
        }
    }

    /**
     * Membatalkan alarm untuk ID tertentu (termasuk potensi snooze-nya).
     */
    fun cancel(context: Context, reminderId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Batalkan alarm utama
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
            Log.d(TAG, "Alarm utama dibatalkan untuk ID: $reminderId")
        }

        // Batalkan alarm snooze jika ada
        val snoozeReminderId = if (reminderId > 0) reminderId + 90000 else 99999
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            snoozeReminderId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (snoozePendingIntent != null) {
            alarmManager.cancel(snoozePendingIntent)
            snoozePendingIntent.cancel()
            Log.d(TAG, "Alarm snooze dibatalkan untuk ID: $snoozeReminderId")
        }
    }
}
