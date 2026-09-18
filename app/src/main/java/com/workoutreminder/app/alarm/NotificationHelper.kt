package com.workoutreminder.app.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.workoutreminder.app.MainActivity
import com.workoutreminder.app.R
import com.workoutreminder.app.ui.screens.AlarmActivity

object NotificationHelper {

    const val CHANNEL_ID = "workout_reminders_channel"
    const val CHANNEL_NAME = "Pengingat Latihan"

    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_SPLIT_NAME = "extra_split_name"
    const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_description)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(soundUri, audioAttributes)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showWorkoutNotification(
        context: Context,
        reminderId: Int,
        splitName: String,
        useFullScreen: Boolean = false
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = reminderId + 10000

        // Intent saat notifikasi utama diklik -> Buka MainActivity
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent tombol aksi "Selesai"
        val doneIntent = Intent(context, WorkoutActionReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_SPLIT_NAME, splitName)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 50000,
            doneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Waktunya Latihan!")
            .setContentText("Waktunya latihan $splitName! Tetap konsisten dan semangat 💪")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Waktunya latihan $splitName!\nJadikan hari ini langkah lebih dekat ke target kebugaranmu. Tekan 'Selesai' setelah latihan!")
            )
            .setColor(0xFFFF5A3C.toInt()) // Coral Orange
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .addAction(
                R.drawable.ic_notification,
                context.getString(R.string.notification_action_done),
                donePendingIntent
            )

        if (useFullScreen) {
            val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                putExtra("reminder_id", reminderId)
                putExtra("split_name", splitName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                reminderId + 20000,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}
