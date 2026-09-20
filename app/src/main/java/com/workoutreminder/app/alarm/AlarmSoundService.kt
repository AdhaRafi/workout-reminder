package com.workoutreminder.app.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class AlarmSoundService : Service() {

    private val TAG = "AlarmSoundService"
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private val timeoutHandler = Handler(Looper.getMainLooper())
    private val autoStopRunnable = Runnable {
        Log.d(TAG, "Batas waktu alarm 10 menit tercapai, menghentikan alarm otomatis...")
        stopAlarmInternal()
        stopSelf()
    }

    companion object {
        const val ACTION_STOP = "com.workoutreminder.app.alarm.ACTION_STOP"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        const val EXTRA_SPLIT_NAME = "extra_split_name"
        const val EXTRA_SOUND_URI = "extra_sound_uri"
        const val EXTRA_SOUND_NAME = "extra_sound_name"

        fun stopAlarm(context: Context) {
            val stopIntent = Intent(context, AlarmSoundService::class.java).apply {
                action = ACTION_STOP
            }
            try {
                context.startService(stopIntent)
            } catch (e: Exception) {
                Log.w("AlarmSoundService", "Gagal mengirim stop intent ke AlarmSoundService: ${e.message}")
            }
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

        val reminderId = intent?.getIntExtra(EXTRA_REMINDER_ID, -1) ?: -1
        val splitName = intent?.getStringExtra(EXTRA_SPLIT_NAME) ?: "Latihan"
        val soundName = intent?.getStringExtra(EXTRA_SOUND_NAME) ?: "Energetic Gym Beat"
        val soundUri = intent?.getStringExtra(EXTRA_SOUND_URI)

        // 1. Tampilkan notifikasi foreground lengkap (dengan tombol Matikan, Tunda 5 Mnt, dan Selesai)
        val notification = NotificationHelper.buildWorkoutNotification(
            context = this,
            reminderId = reminderId,
            splitName = splitName,
            soundName = soundName,
            soundUri = soundUri,
            useSoundAlarm = true,
            useFullScreen = true
        )
        val notificationId = NotificationHelper.getNotificationId(reminderId)
        startForeground(notificationId, notification)

        // 2. Mainkan suara dan getaran
        startPlayingSoundAndVibration(soundUri)

        // 3. Pasang auto-timeout 10 menit agar baterai tidak habis jika HP ditinggal
        timeoutHandler.removeCallbacks(autoStopRunnable)
        timeoutHandler.postDelayed(autoStopRunnable, 10 * 60 * 1000L)

        return START_STICKY
    }

    private fun startPlayingSoundAndVibration(customSoundUri: String?) {
        try {
            val alertUri = if (!customSoundUri.isNullOrBlank()) {
                android.net.Uri.parse(customSoundUri)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(applicationContext, alertUri)
                } catch (_: Exception) {
                    // Fallback ke nada alarm bawaan jika custom URI gagal dimuat
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
            Log.e(TAG, "Gagal mengaktifkan getaran: ${e.message}")
        }
    }

    private fun stopAlarmInternal() {
        timeoutHandler.removeCallbacks(autoStopRunnable)

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        super.onDestroy()
    }
}
