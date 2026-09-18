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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutreminder.app.data.entity.TaskEntity
import com.workoutreminder.app.ui.components.TaskItemCard
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
import kotlinx.coroutines.launch

enum class TaskFilter {
    ALL, ACTIVE, COMPLETED
}

@Composable
fun TasksScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val activeTaskCount by viewModel.activeTaskCount.collectAsState()
    val completedTaskCount by viewModel.completedTaskCount.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var selectedTaskForEdit by remember { mutableStateOf<TaskEntity?>(null) }
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }
    var currentFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredTasks = when (currentFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.ACTIVE -> tasks.filter { !it.isCompleted }
        TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Header dengan Statistik
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Manajemen Task",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kelola semua tugas dan aktivitas Anda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrayMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TaskStatItem(
                            label = "Aktif",
                            count = activeTaskCount,
                            color = CoralOrange
                        )
                        TaskStatItem(
                            label = "Selesai",
                            count = completedTaskCount,
                            color = ActiveGreen
                        )
                        TaskStatItem(
                            label = "Total",
                            count = tasks.size,
                            color = TextGrayLight
                        )
                    }
                }
            }

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentFilter == TaskFilter.ALL,
                    onClick = { currentFilter = TaskFilter.ALL },
                    label = { Text("Semua (${tasks.size})") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoralOrange,
                        selectedLabelColor = TextWhite,
                        containerColor = DarkSurfaceElevated,
                        labelColor = TextGrayMuted
                    )
                )
                FilterChip(
                    selected = currentFilter == TaskFilter.ACTIVE,
                    onClick = { currentFilter = TaskFilter.ACTIVE },
                    label = { Text("Aktif ($activeTaskCount)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoralOrange,
                        selectedLabelColor = TextWhite,
                        containerColor = DarkSurfaceElevated,
                        labelColor = TextGrayMuted
                    )
                )
                FilterChip(
                    selected = currentFilter == TaskFilter.COMPLETED,
                    onClick = { currentFilter = TaskFilter.COMPLETED },
                    label = { Text("Selesai ($completedTaskCount)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoralOrange,
                        selectedLabelColor = TextWhite,
                        containerColor = DarkSurfaceElevated,
                        labelColor = TextGrayMuted
                    )
                )
            }

            // Daftar Task
            if (filteredTasks.isEmpty()) {
                EmptyTaskCard(
                    filter = currentFilter,
                    onAddClicked = {
                        selectedTaskForEdit = null
                        showAddEditDialog = true
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredTasks,
                        key = { it.id }
                    ) { task ->
                        TaskItemCard(
                            task = task,
                            onToggleComplete = {
                                viewModel.toggleTaskComplete(task)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (!task.isCompleted) "Task '${task.title}' selesai! ✅"
                                        else "Task '${task.title}' dibuka kembali"
                                    )
                                }
                            },
                            onEdit = {
                                selectedTaskForEdit = task
                                showAddEditDialog = true
                            },
                            onDelete = {
                                taskToDelete = task
                            }
                        )
                    }
                }
            }
        }

        // FAB Tambah Task
        androidx.compose.material3.FloatingActionButton(
            onClick = {
                selectedTaskForEdit = null
                showAddEditDialog = true
            },
            containerColor = CoralOrange,
            contentColor = TextWhite,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Task",
                modifier = Modifier.size(28.dp)
            )
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    // Dialog Tambah/Edit Task
    if (showAddEditDialog) {
        AddEditTaskDialog(
            initialTask = selectedTaskForEdit,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedTask ->
                viewModel.saveTask(savedTask)
                showAddEditDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Task '${savedTask.title}' berhasil disimpan!"
                    )
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus
    if (taskToDelete != null) {
        val target = taskToDelete!!
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Hapus Task?", color = TextWhite) },
            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus task '${target.title}'?",
                    color = TextGrayLight
                )
            },
            containerColor = DarkSurfaceElevated,
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTask(target)
                        taskToDelete = null
                        scope.launch {
                            snackbarHostState.showSnackbar("Task dihapus")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Hapus", color = TextWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text("Batal", color = TextGrayMuted)
                }
            }
        )
    }
}

@Composable
private fun TaskStatItem(
    label: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = color,
            fontSize = 32.sp
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextGrayMuted
        )
    }
}

@Composable
private fun EmptyTaskCard(
    filter: TaskFilter,
    onAddClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = CoralOrange,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (filter) {
                    TaskFilter.ALL -> "Belum Ada Task"
                    TaskFilter.ACTIVE -> "Tidak Ada Task Aktif"
                    TaskFilter.COMPLETED -> "Belum Ada Task Selesai"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = when (filter) {
                    TaskFilter.ALL -> "Mulai tambahkan task pertama Anda untuk mengelola aktivitas dengan lebih baik!"
                    TaskFilter.ACTIVE -> "Semua task Anda sudah selesai. Mantap! 🎉"
                    TaskFilter.COMPLETED -> "Task yang sudah selesai akan muncul di sini"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrayMuted,
                textAlign = TextAlign.Center
            )
            if (filter == TaskFilter.ALL) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onAddClicked,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextWhite)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Buat Task", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
