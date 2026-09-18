package com.workoutreminder.app.ui.preview

import com.workoutreminder.app.data.entity.TaskEntity
import com.workoutreminder.app.data.entity.TaskPriority
import java.time.LocalDateTime

/**
 * Sample data untuk preview dan testing fitur Task Management
 */
object TaskPreviewData {
    
    // Task yang sudah overdue
    val overdueTask = TaskEntity(
        id = 1,
        title = "Beli Protein Powder dan Suplemen",
        description = "Stok protein powder habis, perlu beli yang baru di toko suplemen",
        isCompleted = false,
        priority = TaskPriority.HIGH,
        category = "Belanja",
        dueDate = LocalDateTime.now().minusDays(2), // 2 hari lalu
        createdAt = LocalDateTime.now().minusDays(5)
    )
    
    // Task urgent yang jatuh tempo hari ini
    val urgentTodayTask = TaskEntity(
        id = 2,
        title = "Konsultasi dengan Personal Trainer",
        description = "Meeting untuk review program latihan bulan ini dan diskusi target berat badan",
        isCompleted = false,
        priority = TaskPriority.URGENT,
        category = "Latihan",
        dueDate = LocalDateTime.now().plusHours(3), // 3 jam lagi
        createdAt = LocalDateTime.now().minusDays(7)
    )
    
    // Task biasa dengan deskripsi panjang
    val normalTask = TaskEntity(
        id = 3,
        title = "Riset Resep Meal Prep untuk Minggu Depan",
        description = "Cari 5 resep meal prep yang tinggi protein, rendah karbo. Fokus pada chicken breast, sayuran hijau, dan quinoa. Budget maksimal 500rb untuk seminggu.",
        isCompleted = false,
        priority = TaskPriority.MEDIUM,
        category = "Kesehatan",
        dueDate = LocalDateTime.now().plusDays(2),
        createdAt = LocalDateTime.now().minusDays(1)
    )
    
    // Task tanpa deadline
    val noDeadlineTask = TaskEntity(
        id = 4,
        title = "Update Playlist Workout",
        description = "Tambah lagu-lagu baru yang energik untuk motivasi gym",
        isCompleted = false,
        priority = TaskPriority.LOW,
        category = "Pribadi",
        dueDate = null,
        createdAt = LocalDateTime.now()
    )
    
    // Task yang sudah selesai
    val completedTask = TaskEntity(
        id = 5,
        title = "Beli Matras Yoga Baru",
        description = "Yang lama sudah rusak, beli yang tebal 6mm",
        isCompleted = true,
        priority = TaskPriority.MEDIUM,
        category = "Belanja",
        dueDate = LocalDateTime.now().minusDays(1),
        createdAt = LocalDateTime.now().minusDays(3),
        completedAt = LocalDateTime.now().minusHours(2)
    )
    
    // Task kerja
    val workTask = TaskEntity(
        id = 6,
        title = "Selesaikan Laporan Q1 Fitness Progress",
        description = "Compile data workout, body measurements, dan progress photos untuk quarterly review",
        isCompleted = false,
        priority = TaskPriority.HIGH,
        category = "Kerja",
        dueDate = LocalDateTime.now().plusDays(5),
        createdAt = LocalDateTime.now()
    )
    
    // Task belajar
    val studyTask = TaskEntity(
        id = 7,
        title = "Baca Artikel tentang Nutrisi Makro",
        description = "Pelajari cara menghitung protein, karbohidrat, dan lemak yang tepat untuk cutting phase",
        isCompleted = false,
        priority = TaskPriority.MEDIUM,
        category = "Belajar",
        dueDate = LocalDateTime.now().plusDays(1),
        createdAt = LocalDateTime.now().minusHours(5)
    )
    
    // Task latihan
    val workoutTask = TaskEntity(
        id = 8,
        title = "Coba Leg Day Routine Baru",
        description = "Test program baru: Squat 4x8, Leg Press 3x12, Lunges 3x10 each leg, Calf Raises 4x15",
        isCompleted = false,
        priority = TaskPriority.HIGH,
        category = "Latihan",
        dueDate = LocalDateTime.now().plusDays(3),
        createdAt = LocalDateTime.now()
    )
    
    // Daftar semua sample tasks
    val allTasks = listOf(
        urgentTodayTask,
        overdueTask,
        workTask,
        workoutTask,
        studyTask,
        normalTask,
        noDeadlineTask,
        completedTask
    )
    
    val activeTasks = allTasks.filter { !it.isCompleted }
    val completedTasks = allTasks.filter { it.isCompleted }
}
