package com.workoutreminder.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workoutreminder.app.data.model.BeginnerLesson
import com.workoutreminder.app.data.model.ExerciseGuideRepository
import com.workoutreminder.app.data.model.ExerciseItem
import com.workoutreminder.app.data.model.MuscleCategory
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.ActiveGreenMuted
import com.workoutreminder.app.ui.theme.ChipBackground
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.CoralOrangeMuted
import com.workoutreminder.app.ui.theme.DarkBackground
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurfaceCard
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.ErrorRed
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite

@Composable
fun ExerciseGuideScreen(
    modifier: Modifier = Modifier,
    initialCategory: MuscleCategory = MuscleCategory.ALL
) {
    var selectedTopTab by remember { mutableIntStateOf(0) } // 0: Katalog Gerakan, 1: Tutorial Pemula
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedExerciseForDetail by remember { mutableStateOf<ExerciseItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Tab Pemilih: Katalog Gerakan vs Tutorial Pemula
        TabRow(
            selectedTabIndex = selectedTopTab,
            containerColor = DarkSurfaceElevated,
            contentColor = TextWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTopTab]),
                    color = CoralOrange,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTopTab == 0,
                onClick = { selectedTopTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTopTab == 0) CoralOrange else TextGrayMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Katalog Gerakan",
                            fontWeight = if (selectedTopTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTopTab == 0) TextWhite else TextGrayMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            )
            Tab(
                selected = selectedTopTab == 1,
                onClick = { selectedTopTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTopTab == 1) CoralOrange else TextGrayMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tutorial Pemula",
                            fontWeight = if (selectedTopTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTopTab == 1) TextWhite else TextGrayMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        if (selectedTopTab == 0) {
            // ==================== TAB 0: KATALOG GERAKAN ====================
            Column(modifier = Modifier.fillMaxSize()) {
                // Category Filter Bar (Horizontal Scroll)
                CategoryFilterBar(
                    selectedCategory = selectedCategory,
                    onSelectCategory = { selectedCategory = it }
                )

                val exercises = remember(selectedCategory) {
                    ExerciseGuideRepository.getExercisesByCategory(selectedCategory)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        // Banner Informasi Kategori
                        CategorySummaryBanner(category = selectedCategory)
                    }

                    items(exercises, key = { it.id }) { exercise ->
                        ExerciseCardItem(
                            exercise = exercise,
                            onClick = { selectedExerciseForDetail = exercise }
                        )
                    }
                }
            }
        } else {
            // ==================== TAB 1: TUTORIAL PEMULA ====================
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BeginnerGuideHeaderCard()
                }

                items(ExerciseGuideRepository.beginnerLessons, key = { it.id }) { lesson ->
                    BeginnerLessonCard(lesson = lesson)
                }
            }
        }
    }

    // Modal Dialog Detail Gerakan Latihan
    selectedExerciseForDetail?.let { exercise ->
        ExerciseDetailDialog(
            exercise = exercise,
            onDismiss = { selectedExerciseForDetail = null }
        )
    }
}

// -------------------------------------------------------------
// FILTER BAR KATEGORI
// -------------------------------------------------------------
@Composable
private fun CategoryFilterBar(
    selectedCategory: MuscleCategory,
    onSelectCategory: (MuscleCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MuscleCategory.values().forEach { cat ->
            val isSelected = cat == selectedCategory
            val catColor = Color(cat.colorHex)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) catColor else DarkSurfaceElevated)
                    .clickable { onSelectCategory(cat) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = cat.emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = cat.displayName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextWhite else TextGrayLight
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// BANNER RINGKASAN KATEGORI
// -------------------------------------------------------------
@Composable
private fun CategorySummaryBanner(category: MuscleCategory) {
    val catColor = Color(category.colorHex)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(catColor.copy(alpha = 0.18f), DarkSurfaceCard)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(catColor.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = category.emoji, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (category == MuscleCategory.ALL) "Semua Panduan Gerakan" else "Fokus Otot: ${category.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = category.subtitle,
                    fontSize = 11.sp,
                    color = TextGrayMuted
                )
            }
        }
    }
}

// -------------------------------------------------------------
// KARTU GERAKAN LATIHAN
// -------------------------------------------------------------
@Composable
private fun ExerciseCardItem(
    exercise: ExerciseItem,
    onClick: () -> Unit
) {
    val catColor = Color(exercise.category.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Visual Movement Diagram Thumbnail
                    MovementVisualThumbnail(
                        illustrationType = exercise.illustrationType,
                        accentColor = catColor
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(catColor.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = exercise.category.displayName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = catColor
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(ChipBackground, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = exercise.difficulty,
                                    fontSize = 10.sp,
                                    color = TextGrayLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 16.sp
                        )

                        Text(
                            text = "🎯 ${exercise.primaryMuscles}",
                            fontSize = 11.sp,
                            color = catColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Detail",
                    tint = TextGrayMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = exercise.shortDescription,
                fontSize = 12.sp,
                color = TextGrayMuted,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer info: Alat & Rekomendasi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛠️ ${exercise.equipment}",
                    fontSize = 11.sp,
                    color = TextGrayLight,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = exercise.beginnerRecommendation.substringBefore("•").trim(),
                    fontSize = 11.sp,
                    color = CoralOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -------------------------------------------------------------
// VISUAL DIAGRAM THUMBNAIL (Gambar Gerakan / Vektor Ilustrasi)
// -------------------------------------------------------------
@Composable
fun MovementVisualThumbnail(
    illustrationType: String,
    accentColor: Color,
    modifier: Modifier = Modifier.size(64.dp)
) {
    Box(
        modifier = modifier
            .background(DarkSurfaceElevated, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            val w = size.width
            val h = size.height
            val primaryColor = accentColor
            val secondaryColor = Color(0xFFE2E2E6)

            when (illustrationType) {
                "lat_pulldown" -> {
                    // Bar atas
                    drawLine(secondaryColor, Offset(w * 0.1f, h * 0.2f), Offset(w * 0.9f, h * 0.2f), strokeWidth = 5f)
                    // Kabel ke bawah
                    drawLine(Color.Gray, Offset(w * 0.5f, 0f), Offset(w * 0.5f, h * 0.2f), strokeWidth = 3f)
                    // Panah arah tarikan ke bawah
                    drawLine(primaryColor, Offset(w * 0.3f, h * 0.25f), Offset(w * 0.3f, h * 0.65f), strokeWidth = 4f)
                    drawLine(primaryColor, Offset(w * 0.7f, h * 0.25f), Offset(w * 0.7f, h * 0.65f), strokeWidth = 4f)
                    // Kepala & torso
                    drawCircle(secondaryColor, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.45f))
                    drawRoundRect(primaryColor, topLeft = Offset(w * 0.38f, h * 0.6f), size = androidx.compose.ui.geometry.Size(w * 0.24f, h * 0.32f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f))
                }
                "dumbbell_row", "cable_row" -> {
                    // Punggung miring & siku menarik ke belakang
                    drawCircle(secondaryColor, radius = w * 0.12f, center = Offset(w * 0.3f, h * 0.3f))
                    drawLine(secondaryColor, Offset(w * 0.3f, h * 0.4f), Offset(w * 0.65f, h * 0.6f), strokeWidth = 6f)
                    // Siku & beban
                    drawLine(primaryColor, Offset(w * 0.45f, h * 0.5f), Offset(w * 0.55f, h * 0.25f), strokeWidth = 5f)
                    drawCircle(primaryColor, radius = w * 0.1f, center = Offset(w * 0.55f, h * 0.25f))
                    // Panah tarikan
                    drawLine(Color(0xFF38BDF8), Offset(w * 0.55f, h * 0.7f), Offset(w * 0.55f, h * 0.35f), strokeWidth = 4f)
                }
                "pushup", "bench_press", "incline_press" -> {
                    // Bangku datar / lantai
                    drawLine(Color.Gray, Offset(w * 0.1f, h * 0.75f), Offset(w * 0.9f, h * 0.75f), strokeWidth = 4f)
                    // Torso datar & dada menonjol
                    drawRoundRect(primaryColor, topLeft = Offset(w * 0.25f, h * 0.62f), size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.12f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f))
                    // Kepala
                    drawCircle(secondaryColor, radius = w * 0.1f, center = Offset(w * 0.2f, h * 0.62f))
                    // Lengan dorong ke atas & dumbbell
                    drawLine(primaryColor, Offset(w * 0.5f, h * 0.62f), Offset(w * 0.5f, h * 0.25f), strokeWidth = 5f)
                    drawLine(Color.White, Offset(w * 0.35f, h * 0.22f), Offset(w * 0.65f, h * 0.22f), strokeWidth = 6f)
                    drawCircle(primaryColor, radius = w * 0.08f, center = Offset(w * 0.35f, h * 0.22f))
                    drawCircle(primaryColor, radius = w * 0.08f, center = Offset(w * 0.65f, h * 0.22f))
                }
                "squat", "lunges", "rdl" -> {
                    // Kepala
                    drawCircle(secondaryColor, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.2f))
                    // Torso
                    drawLine(secondaryColor, Offset(w * 0.5f, h * 0.32f), Offset(w * 0.5f, h * 0.52f), strokeWidth = 6f)
                    // Paha tertekuk 90 derajat & betis
                    drawLine(primaryColor, Offset(w * 0.5f, h * 0.52f), Offset(w * 0.75f, h * 0.68f), strokeWidth = 6f)
                    drawLine(primaryColor, Offset(w * 0.75f, h * 0.68f), Offset(w * 0.75f, h * 0.92f), strokeWidth = 6f)
                    // Panah naik turun
                    drawLine(Color(0xFF3DDC84), Offset(w * 0.22f, h * 0.35f), Offset(w * 0.22f, h * 0.75f), strokeWidth = 3f)
                }
                "shoulder_press", "lateral_raise", "face_pull" -> {
                    // Kepala & bahu
                    drawCircle(secondaryColor, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.28f))
                    // Kedua lengan merentang ke samping / atas
                    drawLine(primaryColor, Offset(w * 0.5f, h * 0.42f), Offset(w * 0.15f, h * 0.35f), strokeWidth = 5f)
                    drawLine(primaryColor, Offset(w * 0.5f, h * 0.42f), Offset(w * 0.85f, h * 0.35f), strokeWidth = 5f)
                    drawCircle(Color.White, radius = w * 0.09f, center = Offset(w * 0.15f, h * 0.35f))
                    drawCircle(Color.White, radius = w * 0.09f, center = Offset(w * 0.85f, h * 0.35f))
                    // Badan
                    drawLine(secondaryColor, Offset(w * 0.5f, h * 0.42f), Offset(w * 0.5f, h * 0.85f), strokeWidth = 6f)
                }
                "bicep_curl", "hammer_curl", "tricep_pushdown" -> {
                    // Lengan & siku menekuk
                    drawCircle(secondaryColor, radius = w * 0.12f, center = Offset(w * 0.35f, h * 0.22f))
                    drawLine(secondaryColor, Offset(w * 0.35f, h * 0.34f), Offset(w * 0.35f, h * 0.8f), strokeWidth = 6f)
                    // Lengan atas diam
                    drawLine(primaryColor, Offset(w * 0.35f, h * 0.45f), Offset(w * 0.35f, h * 0.65f), strokeWidth = 6f)
                    // Lengan bawah menekuk memegang dumbbell
                    drawLine(primaryColor, Offset(w * 0.35f, h * 0.65f), Offset(w * 0.68f, h * 0.42f), strokeWidth = 5f)
                    drawCircle(Color(0xFFF59E0B), radius = w * 0.12f, center = Offset(w * 0.68f, h * 0.42f))
                }
                else -> {
                    // Plank / Core
                    // Garis horizontal tubuh lurus
                    drawLine(primaryColor, Offset(w * 0.15f, h * 0.55f), Offset(w * 0.85f, h * 0.55f), strokeWidth = 7f)
                    // Tumpuan siku & kaki
                    drawLine(secondaryColor, Offset(w * 0.25f, h * 0.55f), Offset(w * 0.25f, h * 0.82f), strokeWidth = 5f)
                    drawLine(secondaryColor, Offset(w * 0.85f, h * 0.55f), Offset(w * 0.85f, h * 0.82f), strokeWidth = 5f)
                    // Lantai
                    drawLine(Color.Gray, Offset(w * 0.1f, h * 0.82f), Offset(w * 0.9f, h * 0.82f), strokeWidth = 3f)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// MODAL DIALOG DETAIL GERAKAN LENGKAP
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExerciseDetailDialog(
    exercise: ExerciseItem,
    onDismiss: () -> Unit
) {
    val catColor = Color(exercise.category.colorHex)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkSurfaceCard,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = exercise.category.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = exercise.category.displayName.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = catColor
                            )
                        }
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = TextGrayMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Illustration Box (Gambar Visual Besar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(catColor.copy(alpha = 0.2f), DarkSurfaceElevated)
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MovementVisualThumbnail(
                            illustrationType = exercise.illustrationType,
                            accentColor = catColor,
                            modifier = Modifier.size(90.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Panduan Visual Gerakan • ${exercise.equipment}",
                            fontSize = 11.sp,
                            color = TextGrayLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tag Otot Target
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "Otot Utama (Primary)", fontSize = 10.sp, color = TextGrayMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = exercise.primaryMuscles, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = catColor)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(text = "Otot Pendukung (Secondary)", fontSize = 10.sp, color = TextGrayMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = exercise.secondaryMuscles, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rekomendasi Pemula Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CoralOrangeMuted, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Rekomendasi Latihan Pemula:", fontSize = 11.sp, color = CoralOrange, fontWeight = FontWeight.Bold)
                            Text(text = exercise.beginnerRecommendation, fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Langkah Pelaksanaan
                Text(
                    text = "Langkah Pelaksanaan (Step-by-Step)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                exercise.steps.forEachIndexed { idx, step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(catColor.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${idx + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = catColor)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = step, fontSize = 13.sp, color = TextGrayLight, lineHeight = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tips Form Penting
                Text(
                    text = "💡 Tips Form & Rahasia Aktivasi Otot",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ActiveGreen
                )
                Spacer(modifier = Modifier.height(6.dp))
                exercise.formTips.forEach { tip ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ActiveGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = tip, fontSize = 12.sp, color = TextGrayLight)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Kesalahan Umum yang Harus Dihindari
                Text(
                    text = "⚠️ Kesalahan Fatal Pemula (Hindari!)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ErrorRed
                )
                Spacer(modifier = Modifier.height(6.dp))
                exercise.commonMistakes.forEach { mistake ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = mistake, fontSize = 12.sp, color = TextGrayLight)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated)
                ) {
                    Text(text = "Tutup Panduan", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HEADER KARTU TUTORIAL PEMULA
// -------------------------------------------------------------
@Composable
private fun BeginnerGuideHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, CoralOrange.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CoralOrangeMuted, Color.Transparent)
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎓", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Panduan Memulai Workout",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Text(
                            text = "Pedoman Lengkap untuk Pemula dari Nol",
                            fontSize = 12.sp,
                            color = CoralOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Jangan biarkan rasa bingung atau malu menghalangimu. Pelajari 6 prinsip dasar berikut agar latihanmu efektif, terhindar dari cedera, dan menghasilkan otot yang kuat!",
                    fontSize = 13.sp,
                    color = TextGrayLight,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// KARTU MATERI PEMBELAJARAN PEMULA
// -------------------------------------------------------------
@Composable
private fun BeginnerLessonCard(lesson: BeginnerLesson) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CoralOrangeMuted, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = lesson.icon, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Box(
                            modifier = Modifier
                                .background(DarkSurfaceElevated, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "MODUL ${lesson.number} • ${lesson.tag.uppercase()}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CoralOrange)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = lesson.summary,
                fontSize = 13.sp,
                color = TextGrayLight,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Poin-poin Penting
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                lesson.keyPoints.forEach { point ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text(text = "• ", color = CoralOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = point, fontSize = 12.sp, color = TextGrayLight, lineHeight = 17.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Golden Rule
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ActiveGreenMuted, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Kunci Emas: ${lesson.goldenRule}",
                        fontSize = 11.sp,
                        color = ActiveGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
