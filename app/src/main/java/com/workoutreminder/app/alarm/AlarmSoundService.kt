package com.workoutreminder.app.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.workoutreminder.app.MainActivity
import com.workoutreminder.app.R

class AlarmSoundService : Service() {

    private val TAG = "AlarmSoundService"
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    companion object {
        const val ACTION_STOP = "com.workoutreminder.app.alarm.ACTION_STOP"
        const val EXTRA_SOUND_URI = "extra_sound_uri"
        const val EXTRA_SOUND_NAME = "extra_sound_name"
        const val NOTIFICATION_ID = 99991

        fun stopAlarm(context: Context) {
            val stopIntent = Intent(context, AlarmSoundService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(stopIntent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopAlarmInternal()
            stopSelf()
            return START_NOT_STICKY
        }

        val soundName = intent?.getStringExtra(EXTRA_SOUND_NAME) ?: "Energetic Gym Beat"
        val soundUri = intent?.getStringExtra(EXTRA_SOUND_URI)

        startForegroundNotification(soundName)
        startPlayingSoundAndVibration(soundUri)

        return START_STICKY
    }

    private fun startForegroundNotification(soundName: String) {
        val stopIntent = Intent(this, AlarmSoundService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            101,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = Intent(this, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            102,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Alarm Latihan Berbunyi!")
            .setContentText("Musik: $soundName • Waktunya bergerak!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(R.drawable.ic_notification, "Matikan Alarm", stopPendingIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startPlayingSoundAndVibration(customSoundUri: String?) {
        try {
            var alertUri = if (!customSoundUri.isNullOrBlank()) {
                android.net.Uri.parse(customSoundUri)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }

            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(applicationContext, alertUri)
                } catch (_: Exception) {
                    // Fallback ke nada alarm default jika custom URI gagal
                    val fallbackUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    setDataSource(applicationContext, fallbackUri)
                }
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal memutar audio alarm: ${e.message}")
        }

        try {
            val pattern = longArrayOf(0, 800, 400, 800, 400)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, 0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal mengaktifkan vibrator: ${e.message}")
        }
    }

    private fun stopAlarmInternal() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error saat stop MediaPlayer: ${e.message}")
        }

        try {
            vibrator?.cancel()
        } catch (e: Exception) {
            Log.e(TAG, "Error saat stop Vibrator: ${e.message}")
        }
    }

    override fun onDestroy() {
        stopAlarmInternal()
        super.onDestroy()
    }
}
