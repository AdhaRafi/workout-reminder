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
        val splitName = intent.getStringExtra(AlarmScheduler.EXTRA_SPLIT_NAME) ?: "Latihan"

        Log.d(TAG, "Menerima broadcast alarm untuk reminderId=$reminderId, split=$splitName")

        val repository = (context.applicationContext as WorkoutApplication).repository
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reminder = repository.getReminderById(reminderId)
                
                // 1. Cek apakah harus menggunakan alarm suara
                if (reminder != null && reminder.useSoundAlarm && reminder.isActive) {
                    // Jalankan service suara dengan sound pilihan
                    val serviceIntent = Intent(context, AlarmSoundService::class.java).apply {
                        putExtra(AlarmSoundService.EXTRA_SOUND_NAME, reminder.alarmSoundName)
                        putExtra(AlarmSoundService.EXTRA_SOUND_URI, reminder.alarmSoundUri)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }

                    // Buka Activity layar penuh jika diizinkan sistem
                    try {
                        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                            putExtra("reminder_id", reminderId)
                            putExtra("split_name", splitName)
                            putExtra("sound_name", reminder.alarmSoundName)
                            putExtra("sound_uri", reminder.alarmSoundUri)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(alarmIntent)
                    } catch (e: Exception) {
                        Log.w(TAG, "Tidak dapat membuka AlarmActivity langsung dari background: ${e.message}")
                    }
                }

                // 2. Tampilkan notifikasi lokal (dengan fullScreenIntent jika useSoundAlarm aktif)
                NotificationHelper.showWorkoutNotification(
                    context = context,
                    reminderId = reminderId,
                    splitName = splitName,
                    useFullScreen = (reminder?.useSoundAlarm == true && reminder.isActive)
                )

                // 3. Jadwalkan ulang alarm berikutnya
                if (reminder != null && reminder.isActive) {
                    AlarmScheduler.schedule(context, reminder)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saat memproses alarm: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
