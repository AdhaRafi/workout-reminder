package com.workoutreminder.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.workoutreminder.app.WorkoutApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    private val TAG = "BootReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "Menerima broadcast: $action")

        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED
        ) {
            val pendingResult = goAsync()
            val repository = (context.applicationContext as WorkoutApplication).repository

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val activeReminders = repository.getActiveReminders()
                    Log.d(TAG, "Mendaftarkan ulang ${activeReminders.size} alarm aktif setelah reboot/waktu berubah")
                    for (reminder in activeReminders) {
                        AlarmScheduler.schedule(context, reminder)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error saat mendaftarkan ulang alarm: ${e.message}")
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
