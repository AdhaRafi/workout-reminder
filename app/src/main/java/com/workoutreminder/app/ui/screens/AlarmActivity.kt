package com.workoutreminder.app.ui.screens

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutreminder.app.MainActivity
import com.workoutreminder.app.WorkoutApplication
import com.workoutreminder.app.alarm.AlarmScheduler
import com.workoutreminder.app.alarm.AlarmSoundService
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.CoralOrangeMuted
import com.workoutreminder.app.ui.theme.DarkBackground
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite
import com.workoutreminder.app.ui.theme.WorkoutReminderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlarmActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        turnOnScreenAndDismissKeyguard()

        val reminderId = intent.getIntExtra("reminder_id", -1)
        val splitName = intent.getStringExtra("split_name") ?: "Latihan Harian"
        val soundName = intent.getStringExtra("sound_name") ?: "Energetic Gym Beat"

        val themeMode = getSharedPreferences("workout_user_profile", Context.MODE_PRIVATE)
            .getString("key_theme_mode", "SYSTEM") ?: "SYSTEM"

        setContent {
            val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val isDark = when (themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemDark
            }

            WorkoutReminderTheme(darkTheme = isDark) {
                AlarmScreenContent(
                    splitName = splitName,
                    soundName = soundName,
                    onDone = {
                        stopAlarm()
                        recordCompletion(reminderId, splitName)
                        finish()
                    },
                    onStartWorkout = {
                        stopAlarm()
                        val mainIntent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                        startActivity(mainIntent)
                        finish()
                    },
                    onSnooze = {
                        stopAlarm()
                        snoozeAlarm(reminderId, splitName)
                        finish()
                    }
                )
            }
        }
    }

    private fun turnOnScreenAndDismissKeyguard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun stopAlarm() {
        AlarmSoundService.stopAlarm(this)
    }

    private fun recordCompletion(reminderId: Int, splitName: String) {
        val repository = (application as WorkoutApplication).repository
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.recordCompletion(
                    splitName = splitName,
                    reminderId = if (reminderId != -1) reminderId else null
                )
            } catch (_: Exception) {}
        }
    }

    private fun snoozeAlarm(reminderId: Int, splitName: String) {
        val snoozeTime = LocalTime.now().plusMinutes(5)
        val tempReminder = ReminderEntity(
            id = if (reminderId != -1) reminderId + 90000 else 99999,
            splitName = "$splitName (Tunda)",
            hour = snoozeTime.hour,
            minute = snoozeTime.minute,
            daysOfWeek = listOf(java.time.LocalDate.now().dayOfWeek.value),
            isActive = true
        )
        AlarmScheduler.schedule(this, tempReminder)
    }

    override fun onDestroy() {
        stopAlarm()
        super.onDestroy()
    }
}

@Composable
private fun AlarmScreenContent(
    splitName: String,
    soundName: String,
    onDone: () -> Unit,
    onStartWorkout: () -> Unit,
    onSnooze: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "alarm_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C130D),
                            DarkBackground,
                            Color(0xFF10131B)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Bagian Atas: Waktu & Status
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Text(
                        text = currentTime,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Waktunya Latihan Hari Ini!",
                        style = MaterialTheme.typography.titleMedium,
                        color = CoralOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Bagian Tengah: Animasi Ikon & Nama Split
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(scale)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(CoralOrange.copy(alpha = 0.5f), Color.Transparent)
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(CoralOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Dumbbell",
                                tint = TextWhite,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = splitName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .background(CoralOrangeMuted, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "🎵 $soundName",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CoralOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Jaga komitmen dan konsistensi fisikmu. Latihan 45 menit hanya 4% dari harimu!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrayLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                // Bagian Bawah: Tombol Aksi
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ActiveGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Latihan Selesai! (Catat)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = TextWhite
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Buka Jadwal & Mulai",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onSnooze,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGrayMuted)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Snooze,
                            contentDescription = null,
                            tint = TextGrayLight
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tunda 5 Menit",
                            color = TextGrayLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
