package com.workoutreminder.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.ui.components.HistorySummaryCard
import com.workoutreminder.app.ui.components.ReminderItemCard
import com.workoutreminder.app.ui.components.WeeklyScheduleCard
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBackground
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurfaceCard
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.ErrorRed
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite
import com.workoutreminder.app.ui.viewmodel.TaskViewModel
import com.workoutreminder.app.ui.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WorkoutViewModel,
    taskViewModel: TaskViewModel,
    hasNotificationPermission: Boolean,
    hasExactAlarmPermission: Boolean,
    onRequestNotificationPermission: () -> Unit,
    onRequestExactAlarmPermission: () -> Unit
) {
    val reminders by viewModel.reminders.collectAsState()
    val histories by viewModel.thisWeekHistories.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var selectedReminderForEdit by remember { mutableStateOf<ReminderEntity?>(null) }
    var reminderToDelete by remember { mutableStateOf<ReminderEntity?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(CoralOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (selectedTab) {
                                    1 -> Icons.Default.School
                                    2 -> Icons.Default.EmojiEvents
                                    3 -> Icons.Default.CheckCircle
                                    4 -> Icons.Default.Person
                                    else -> Icons.Default.FitnessCenter
                                },
                                contentDescription = "Logo",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = when (selectedTab) {
                                    1 -> "Panduan & Gerakan"
                                    2 -> "Aktivitas & Prestasi"
                                    3 -> "Task Manager"
                                    4 -> "Profil Saya"
                                    else -> "Workout Reminder"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Text(
                                text = when (selectedTab) {
                                    1 -> "Katalog Gerakan & Tutorial Pemula"
                                    2 -> "Streak, Level & Badges Kebugaran"
                                    3 -> "Kelola Tugas & Aktivitas"
                                    4 -> "Kebugaran & Metrik Tubuh"
                                    else -> "Jadwal & Pengingat Latihan"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextGrayMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurfaceElevated,
                contentColor = TextWhite
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Jadwal") },
                    label = { Text("Jadwal", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = CoralOrange,
                        indicatorColor = CoralOrange,
                        unselectedIconColor = TextGrayMuted,
                        unselectedTextColor = TextGrayMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.School, contentDescription = "Panduan") },
                    label = { Text("Panduan", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = CoralOrange,
                        indicatorColor = CoralOrange,
                        unselectedIconColor = TextGrayMuted,
                        unselectedTextColor = TextGrayMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Aktivitas") },
                    label = { Text("Aktivitas", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = CoralOrange,
                        indicatorColor = CoralOrange,
                        unselectedIconColor = TextGrayMuted,
                        unselectedTextColor = TextGrayMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Tasks") },
                    label = { Text("Tasks", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = CoralOrange,
                        indicatorColor = CoralOrange,
                        unselectedIconColor = TextGrayMuted,
                        unselectedTextColor = TextGrayMuted
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = CoralOrange,
                        indicatorColor = CoralOrange,
                        unselectedIconColor = TextGrayMuted,
                        unselectedTextColor = TextGrayMuted
                    )
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = {
                        selectedReminderForEdit = null
                        showAddEditDialog = true
                    },
                    containerColor = CoralOrange,
                    contentColor = TextWhite,
                    shape = CircleShape,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Pengingat",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            1 -> {
                ExerciseGuideScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            2 -> {
                ActivityScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            3 -> {
                TasksScreen(
                    viewModel = taskViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            4 -> {
                ProfileScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                    onOpenExerciseGuide = { selectedTab = 1 }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
            // 1. Permission Warning Banners jika belum diberikan
            if (!hasNotificationPermission) {
                item {
                    PermissionWarningBanner(
                        title = "Izin Notifikasi Belum Aktif",
                        description = "Aplikasi memerlukan izin notifikasi agar pengingat latihan dapat muncul saat waktu tiba.",
                        buttonText = "Izinkan Notifikasi",
                        icon = Icons.Default.NotificationsActive,
                        onClick = onRequestNotificationPermission
                    )
                }
            }

            if (!hasExactAlarmPermission) {
                item {
                    PermissionWarningBanner(
                        title = "Izin Alarm Tepat Waktu (Exact Alarm)",
                        description = "Diperlukan agar alarm dapat berbunyi tepat waktu bahkan saat HP sedang sleep/mode hemat daya.",
                        buttonText = "Aktifkan Izin",
                        icon = Icons.Default.Schedule,
                        onClick = onRequestExactAlarmPermission
                    )
                }
            }

            // 2. Ringkasan Latihan Selesai Minggu Ini
            item {
                HistorySummaryCard(histories = histories)
            }

            // 3. Ringkasan Jadwal Mingguan (Senin - Minggu)
            item {
                WeeklyScheduleCard(reminders = reminders)
            }

            // 4. Header Daftar Pengingat
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Pengingat",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "${reminders.size} Pengingat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrayMuted
                    )
                }
            }

            // 5. Daftar Item Pengingat (Sorted by upcoming time)
            if (reminders.isEmpty()) {
                item {
                    EmptyReminderCard(
                        onAddClicked = {
                            selectedReminderForEdit = null
                            showAddEditDialog = true
                        }
                    )
                }
            } else {
                items(
                    items = reminders,
                    key = { it.id }
                ) { reminder ->
                    ReminderItemCard(
                        reminder = reminder,
                        onToggleActive = { isActive ->
                            viewModel.toggleReminderActive(reminder, isActive)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (isActive) "Alarm untuk ${reminder.splitName} diaktifkan"
                                    else "Alarm untuk ${reminder.splitName} dinonaktifkan"
                                )
                            }
                        },
                        onEdit = {
                            selectedReminderForEdit = reminder
                            showAddEditDialog = true
                        },
                        onDelete = {
                            reminderToDelete = reminder
                        },
                        onMarkDone = {
                            viewModel.markWorkoutCompleted(reminder.splitName, reminder.id)
                            scope.launch {
                                snackbarHostState.showSnackbar("Latihan ${reminder.splitName} dicatat selesai! 💪")
                            }
                        }
                    )
                }
            }
        }
    }
}
}

    // Modal Tambah / Edit
    if (showAddEditDialog) {
        AddEditReminderDialog(
            initialReminder = selectedReminderForEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedReminder ->
                viewModel.saveReminder(savedReminder)
                showAddEditDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Pengingat ${savedReminder.splitName} berhasil disimpan!")
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus
    if (reminderToDelete != null) {
        val target = reminderToDelete!!
        AlertDialog(
            onDismissRequest = { reminderToDelete = null },
            title = { Text("Hapus Pengingat?", color = TextWhite) },
            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus pengingat latihan ${target.splitName} (${target.formattedTime})?",
                    color = TextGrayLight
                )
            },
            containerColor = DarkSurfaceElevated,
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteReminder(target)
                        reminderToDelete = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Pengingat dihapus.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Hapus", color = TextWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { reminderToDelete = null }) {
                    Text("Batal", color = TextGrayMuted)
                }
            }
        )
    }
}

@Composable
private fun PermissionWarningBanner(
    title: String,
    description: String,
    buttonText: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF281F1A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, CoralOrange.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CoralOrange,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CoralOrange
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayLight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
            ) {
                Text(buttonText, color = TextWhite, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EmptyReminderCard(onAddClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(DarkSurfaceElevated, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = CoralOrange,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Belum Ada Pengingat Latihan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tambahkan jadwal split latihan pertama Anda agar tidak melewatkan sesi latihan!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onAddClicked,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextWhite)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Buat Pengingat", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        }
    }
}
