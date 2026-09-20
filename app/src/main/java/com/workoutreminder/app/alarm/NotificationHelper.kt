package com.workoutreminder.app.alarm

import android.app.Notification
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

    const val CHANNEL_ID = "workout_alarm_channel_v2"
    const val CHANNEL_NAME = "Alarm & Pengingat Latihan"

    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_SPLIT_NAME = "extra_split_name"
    const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    const val EXTRA_SNOOZE_MINUTES = "extra_snooze_minutes"
    const val EXTRA_USE_SOUND_ALARM = "extra_use_sound_alarm"
    const val EXTRA_ALARM_SOUND_NAME = "extra_alarm_sound_name"
    const val EXTRA_ALARM_SOUND_URI = "extra_alarm_sound_uri"

    /**
     * Menghasilkan notification ID unik yang konsisten per reminderId.
     */
    fun getNotificationId(reminderId: Int): Int {
        return if (reminderId > 0) reminderId + 10000 else 99991
    }

    /**
     * Membuat Notification Channel khusus untuk alarm & pengingat latihan.
     * Menggunakan IMPORTANCE_HIGH dan USAGE_ALARM agar selalu berbunyi dan muncul di lockscreen.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Bersihkan channel versi lama jika ada
            try {
                manager.deleteNotificationChannel("workout_reminders_channel")
            } catch (_: Exception) {}

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi dan alarm pengingat latihan kebugaran harian"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 600, 300, 600, 300, 600)
                setSound(soundUri, audioAttributes)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                enableLights(true)
                lightColor = 0xFFFF5A3C.toInt()
                setShowBadge(true)
            }

            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Membangun notifikasi lengkap dengan tombol aksi: Matikan, Tunda 5 Mnt, dan Selesai.
     */
    fun buildWorkoutNotification(
        context: Context,
        reminderId: Int,
        splitName: String,
        soundName: String = "Energetic Gym Beat",
        soundUri: String? = null,
        useSoundAlarm: Boolean = false,
        useFullScreen: Boolean = false
    ): Notification {
        val notificationId = getNotificationId(reminderId)

        // 1. Content Intent: Saat bodi notifikasi ditekan
        val contentIntent = if (useSoundAlarm) {
            Intent(context, AlarmActivity::class.java).apply {
                putExtra("reminder_id", reminderId)
                putExtra("split_name", splitName)
                putExtra("sound_name", soundName)
                putExtra("sound_uri", soundUri)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            Intent(context, MainActivity::class.java).apply {
                putExtra("reminder_id", reminderId)
                putExtra("split_name", splitName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Tombol Aksi: MATIKAN ALARM
        val dismissIntent = Intent(context, WorkoutActionReceiver::class.java).apply {
            action = WorkoutActionReceiver.ACTION_DISMISS
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_SPLIT_NAME, splitName)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 10000,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Tombol Aksi: TUNDA (SNOOZE) 5 MENIT
        val snoozeIntent = Intent(context, WorkoutActionReceiver::class.java).apply {
            action = WorkoutActionReceiver.ACTION_SNOOZE
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_SPLIT_NAME, splitName)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(EXTRA_SNOOZE_MINUTES, 5)
            putExtra(EXTRA_USE_SOUND_ALARM, useSoundAlarm)
            putExtra(EXTRA_ALARM_SOUND_NAME, soundName)
            putExtra(EXTRA_ALARM_SOUND_URI, soundUri)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 20000,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 4. Tombol Aksi: SELESAI (CATAT RIWAYAT)
        val doneIntent = Intent(context, WorkoutActionReceiver::class.java).apply {
            action = WorkoutActionReceiver.ACTION_DONE
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_SPLIT_NAME, splitName)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val donePendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 30000,
            doneIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (useSoundAlarm) "⏰ Waktunya Latihan: $splitName" else "Waktunya Latihan: $splitName"
        val subtitle = if (useSoundAlarm) "Alarm berbunyi • Waktunya bergerak!" else "Waktunya jadwal latihan $splitName! Tetap konsisten 💪"
        val bigText = if (useSoundAlarm) {
            "Waktunya latihan $splitName!\nJadikan hari ini langkah lebih dekat ke targetmu. Matikan jika sudah siap, tunda jika sedang sibuk, atau tekan Selesai setelah latihan!"
        } else {
            "Waktunya latihan $splitName!\nJaga konsistensimu hari ini. Tekan Selesai untuk mencatat riwayat kebugaranmu!"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(bigText)
            )
            .setColor(0xFFFF5A3C.toInt()) // Coral Orange
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setOngoing(useSoundAlarm)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(dismissPendingIntent) // Jika notifikasi diswipe tutup, matikan suara alarm
            .addAction(
                R.drawable.ic_alarm_off,
                "Matikan",
                dismissPendingIntent
            )
            .addAction(
                R.drawable.ic_snooze,
                "Tunda 5 Mnt",
                snoozePendingIntent
            )
            .addAction(
                R.drawable.ic_check,
                "Selesai",
                donePendingIntent
            )

        if (useFullScreen) {
            val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                putExtra("reminder_id", reminderId)
                putExtra("split_name", splitName)
                putExtra("sound_name", soundName)
                putExtra("sound_uri", soundUri)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                reminderId + 20000,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setFullScreenIntent(fullScreenPendingIntent, true)
        }

        return builder.build()
    }

    /**
     * Menampilkan notifikasi latihan ke status bar.
     */
    fun showWorkoutNotification(
        context: Context,
        reminderId: Int,
        splitName: String,
        soundName: String = "Energetic Gym Beat",
        soundUri: String? = null,
        useSoundAlarm: Boolean = false,
        useFullScreen: Boolean = false
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = buildWorkoutNotification(
            context = context,
            reminderId = reminderId,
            splitName = splitName,
            soundName = soundName,
            soundUri = soundUri,
            useSoundAlarm = useSoundAlarm,
            useFullScreen = useFullScreen
        )
        val notificationId = getNotificationId(reminderId)
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Membatalkan notifikasi berdasarkan ID.
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}
