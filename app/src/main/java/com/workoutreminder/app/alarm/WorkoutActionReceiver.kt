package com.workoutreminder.app.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.workoutreminder.app.WorkoutApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutActionReceiver : BroadcastReceiver() {

    private val TAG = "WorkoutActionReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getIntExtra(NotificationHelper.EXTRA_REMINDER_ID, -1)
        val splitName = intent.getStringExtra(NotificationHelper.EXTRA_SPLIT_NAME) ?: "Latihan"
        val notificationId = intent.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, -1)

        Log.d(TAG, "Menerima aksi 'Selesai' untuk split: $splitName, reminderId: $reminderId")

        // Tutup notifikasi
        if (notificationId != -1) {
            NotificationHelper.cancelNotification(context, notificationId)
        }

        // Catat riwayat latihan selesai ke Room Database
        val pendingResult = goAsync()
        val repository = (context.applicationContext as WorkoutApplication).repository

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.recordCompletion(
                    splitName = splitName,
                    reminderId = if (reminderId != -1) reminderId else null
                )
                Log.d(TAG, "Berhasil mencatat riwayat latihan selesai: $splitName")
            } catch (e: Exception) {
                Log.e(TAG, "Gagal mencatat riwayat: ${e.message}")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
