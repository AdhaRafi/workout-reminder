package com.workoutreminder.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.workoutreminder.app.WorkoutApplication
import com.workoutreminder.app.ui.screens.AlarmActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutAlarmReceiver : BroadcastReceiver() {

    private val TAG = "WorkoutAlarmReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getIntExtra(AlarmScheduler.EXTRA_REMINDER_ID, -1)
        val originalReminderId = intent.getIntExtra(AlarmScheduler.EXTRA_ORIGINAL_REMINDER_ID, reminderId)
        val splitName = intent.getStringExtra(AlarmScheduler.EXTRA_SPLIT_NAME) ?: "Latihan"
        val isSnooze = intent.getBooleanExtra(AlarmScheduler.EXTRA_IS_SNOOZE, false)
        val extraUseSoundAlarm = intent.getBooleanExtra(AlarmScheduler.EXTRA_USE_SOUND_ALARM, true)
        val extraSoundName = intent.getStringExtra(AlarmScheduler.EXTRA_ALARM_SOUND_NAME) ?: "Energetic Gym Beat"
        val extraSoundUri = intent.getStringExtra(AlarmScheduler.EXTRA_ALARM_SOUND_URI)

        Log.d(TAG, "Menerima broadcast alarm untuk reminderId=$reminderId, isSnooze=$isSnooze, split=$splitName")

        val repository = (context.applicationContext as WorkoutApplication).repository
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reminder = if (originalReminderId > 0 && originalReminderId < 90000) {
                    repository.getReminderById(originalReminderId)
                } else null

                val useSoundAlarm = if (isSnooze) {
                    extraUseSoundAlarm
                } else {
                    reminder?.useSoundAlarm ?: extraUseSoundAlarm
                }

                val soundName = if (isSnooze) {
                    extraSoundName
                } else {
                    reminder?.alarmSoundName ?: extraSoundName
                }

                val soundUri = if (isSnooze) {
                    extraSoundUri
                } else {
                    reminder?.alarmSoundUri ?: extraSoundUri
                }

                val isActive = if (isSnooze) true else (reminder?.isActive ?: true)
                val effectiveReminderId = if (isSnooze) originalReminderId else reminderId

                // 1. Jalankan Foreground Service suara jika useSoundAlarm aktif
                if (useSoundAlarm && isActive) {
                    try {
                        val serviceIntent = Intent(context, AlarmSoundService::class.java).apply {
                            putExtra(AlarmSoundService.EXTRA_REMINDER_ID, effectiveReminderId)
                            putExtra(AlarmSoundService.EXTRA_SPLIT_NAME, splitName)
                            putExtra(AlarmSoundService.EXTRA_SOUND_NAME, soundName)
                            putExtra(AlarmSoundService.EXTRA_SOUND_URI, soundUri)
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Gagal memulai AlarmSoundService dari background: ${e.message}")
                    }

                    // Coba luncurkan AlarmActivity layar penuh
                    try {
                        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                            putExtra("reminder_id", effectiveReminderId)
                            putExtra("split_name", splitName)
                            putExtra("sound_name", soundName)
                            putExtra("sound_uri", soundUri)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                        context.startActivity(alarmIntent)
                    } catch (e: Exception) {
                        Log.w(TAG, "Tidak dapat membuka AlarmActivity langsung dari background: ${e.message}")
                    }
                }

                // 2. SELALU tampilkan notifikasi lokal dengan tombol Matikan, Tunda 5 Mnt, & Selesai
                // Dijalankan dalam try-catch mandiri agar dijamin muncul tanpa terpengaruh kondisi di atas
                try {
                    NotificationHelper.showWorkoutNotification(
                        context = context,
                        reminderId = effectiveReminderId,
                        splitName = splitName,
                        soundName = soundName,
                        soundUri = soundUri,
                        useSoundAlarm = useSoundAlarm,
                        useFullScreen = (useSoundAlarm && isActive)
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Gagal menampilkan notifikasi latihan: ${e.message}")
                }

                // 3. Jadwalkan ulang jadwal mingguan berikutnya jika bukan alarm snooze
                if (!isSnooze && reminder != null && reminder.isActive) {
                    AlarmScheduler.schedule(context, reminder)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saat memproses broadcast alarm: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
