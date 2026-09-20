package com.workoutreminder.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.workoutreminder.app.WorkoutApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutActionReceiver : BroadcastReceiver() {

    private val TAG = "WorkoutActionReceiver"

    companion object {
        const val ACTION_DONE = "com.workoutreminder.app.alarm.ACTION_DONE"
        const val ACTION_DISMISS = "com.workoutreminder.app.alarm.ACTION_DISMISS"
        const val ACTION_SNOOZE = "com.workoutreminder.app.alarm.ACTION_SNOOZE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: ACTION_DONE
        val reminderId = intent.getIntExtra(NotificationHelper.EXTRA_REMINDER_ID, -1)
        val splitName = intent.getStringExtra(NotificationHelper.EXTRA_SPLIT_NAME) ?: "Latihan"
        val notificationId = intent.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, -1)

        Log.d(TAG, "Menerima aksi '$action' untuk split: $splitName, reminderId: $reminderId, notifId: $notificationId")

        // 1. Selalu hentikan suara alarm jika sedang aktif berbunyi
        AlarmSoundService.stopAlarm(context)

        // 2. Tutup notifikasi dari status bar
        if (notificationId != -1) {
            NotificationHelper.cancelNotification(context, notificationId)
        }

        when (action) {
            ACTION_DISMISS -> {
                Log.d(TAG, "Alarm dimatikan oleh pengguna dari tombol notifikasi.")
                showToast(context, "Alarm dimatikan")
            }

            ACTION_SNOOZE -> {
                val snoozeMinutes = intent.getIntExtra(NotificationHelper.EXTRA_SNOOZE_MINUTES, 5)
                val useSoundAlarm = intent.getBooleanExtra(NotificationHelper.EXTRA_USE_SOUND_ALARM, true)
                val soundName = intent.getStringExtra(NotificationHelper.EXTRA_ALARM_SOUND_NAME) ?: "Energetic Gym Beat"
                val soundUri = intent.getStringExtra(NotificationHelper.EXTRA_ALARM_SOUND_URI)

                Log.d(TAG, "Menunda alarm untuk $splitName selama $snoozeMinutes menit")
                AlarmScheduler.scheduleSnooze(
                    context = context,
                    reminderId = reminderId,
                    splitName = splitName,
                    snoozeMinutes = snoozeMinutes,
                    useSoundAlarm = useSoundAlarm,
                    soundName = soundName,
                    soundUri = soundUri
                )
                showToast(context, "Alarm ditunda $snoozeMinutes menit ⏱️")
            }

            ACTION_DONE -> {
                val pendingResult = goAsync()
                val repository = (context.applicationContext as WorkoutApplication).repository

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        repository.recordCompletion(
                            splitName = splitName,
                            reminderId = if (reminderId != -1 && reminderId < 90000) reminderId else null
                        )
                        Log.d(TAG, "Berhasil mencatat riwayat latihan selesai: $splitName")
                        showToast(context, "Hebat! Latihan selesai dicatat 💪")
                    } catch (e: Exception) {
                        Log.e(TAG, "Gagal mencatat riwayat: ${e.message}")
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {}
        }
    }
}
