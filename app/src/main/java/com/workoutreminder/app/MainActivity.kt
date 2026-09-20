package com.workoutreminder.app

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.workoutreminder.app.ui.screens.MainScreen
import com.workoutreminder.app.ui.theme.WorkoutReminderTheme
import com.workoutreminder.app.ui.viewmodel.TaskViewModel
import com.workoutreminder.app.ui.viewmodel.TaskViewModelFactory
import com.workoutreminder.app.ui.viewmodel.WorkoutViewModel
import com.workoutreminder.app.ui.viewmodel.WorkoutViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: WorkoutViewModel by viewModels {
        val app = application as WorkoutApplication
        WorkoutViewModelFactory(app, app.repository, app.userProfileManager)
    }

    private val taskViewModel: TaskViewModel by viewModels {
        val app = application as WorkoutApplication
        TaskViewModelFactory(app.database.taskDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val userProfile by viewModel.userProfile.collectAsState()
            val isSystemDark = isSystemInDarkTheme()
            val isDark = when (userProfile.themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemDark
            }

            WorkoutReminderTheme(darkTheme = isDark) {
                val lifecycleOwner = LocalLifecycleOwner.current

                var hasNotificationPermission by remember {
                    mutableStateOf(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        } else {
                            true
                        }
                    )
                }

                var hasExactAlarmPermission by remember {
                    mutableStateOf(checkExactAlarmPermission())
                }

                // Perbarui status izin saat pengguna kembali ke aplikasi dari halaman Pengaturan
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                hasNotificationPermission = ContextCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                            }
                            hasExactAlarmPermission = checkExactAlarmPermission()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                // Launcher untuk meminta izin notifikasi (Android 13+)
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    hasNotificationPermission = isGranted
                }

                // Otomatis minta izin notifikasi saat pertama kali dibuka jika belum diizinkan (Android 13+)
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                MainScreen(
                    viewModel = viewModel,
                    taskViewModel = taskViewModel,
                    hasNotificationPermission = hasNotificationPermission,
                    hasExactAlarmPermission = hasExactAlarmPermission,
                    onRequestNotificationPermission = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    onRequestExactAlarmPermission = {
                        requestExactAlarmPermission()
                    }
                )
            }
        }
    }

    private fun checkExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }
    }
}
