package com.workoutreminder.app.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workoutreminder.app.data.model.UserProfile
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
import com.workoutreminder.app.ui.viewmodel.WorkoutViewModel

private val AVAILABLE_AVATARS = listOf(
    "🦁", "⚡", "🏋️", "🥊", "🧘", "🏃", "🦍", "🐺",
    "🦾", "🚀", "🐅", "🦅", "🥋", "🚴", "🏊", "🤸",
    "🏆", "🔥", "👑", "🛡️", "💪", "🎯", "🌟", "✨"
)

private val FITNESS_LEVELS = listOf("Pemula", "Menengah", "Mahir")
private val FAVORITE_CATEGORIES = listOf(
    "Punggung (Back)", "Dada (Chest)", "Kaki (Legs)",
    "Bahu (Shoulders)", "Lengan (Arms)", "Perut (Core)"
)
private val FITNESS_GOALS = listOf(
    "Bulking / Tambah Otot",
    "Cutting / Bakar Lemak",
    "Kebugaran & Stamina",
    "Jaga Bentuk Tubuh"
)

private val MOTIVATIONAL_QUOTES = listOf(
    "“Keringat hari ini adalah kekuatan dan rasa bangga hari esok.”",
    "“Disiplin adalah jembatan antara target dan hasil nyata.”",
    "“Jangan berhenti ketika lelah, berhentilah ketika kamu selesai.”",
    "“45 menit latihan hanyalah 4% dari harimu. Tidak ada alasan!”",
    "“Otot tidak tumbuh dari zona nyaman. Dorong batas kemampuanmu!”",
    "“Konsistensi kecil yang dilakukan setiap hari menghasilkan transformasi besar.”",
    "“Rasa sakit saat latihan bersifat sementara, rasa bangga bertahan selamanya.”"
)

@Composable
fun ProfileScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier,
    onOpenExerciseGuide: () -> Unit = {}
) {
    val profile by viewModel.userProfile.collectAsState()
    val thisWeekHistories by viewModel.thisWeekHistories.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    var quoteIndex by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Kartu Header Profil
        item {
            ProfileHeaderCard(
                profile = profile,
                onEditClick = { showEditDialog = true }
            )
        }

        // 2. Banner Pintas ke Panduan & Tutorial Pemula
        item {
            WorkoutGuideBannerCard(onOpenGuide = onOpenExerciseGuide)
        }

        // 3. Kartu BMI & Komposisi Fisik
        item {
            BmiMetricCard(profile = profile)
        }

        // 4. Kartu Target Mingguan & Asupan Sehat
        item {
            DailyHealthCard(
                profile = profile,
                completedThisWeek = thisWeekHistories.size
            )
        }

        // 5. Kartu Generator Kata Motivasi Fitness
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CoralOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Motivasi Hari Ini",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        IconButton(
                            onClick = {
                                quoteIndex = (quoteIndex + 1) % MOTIVATIONAL_QUOTES.size
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Ganti Motivasi",
                                tint = CoralOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = MOTIVATIONAL_QUOTES[quoteIndex],
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextWhite,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            currentProfile = profile,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                viewModel.saveUserProfile(updated)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun UserAvatarDisplay(
    profile: UserProfile,
    size: Dp = 72.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember(profile.photoUri) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(profile.photoUri) {
        if (!profile.photoUri.isNullOrBlank()) {
            try {
                val uri = Uri.parse(profile.photoUri)
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    bitmap = BitmapFactory.decodeStream(stream)
                }
            } catch (e: Exception) {
                bitmap = null
            }
        } else {
            bitmap = null
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(CoralOrange, Color(0xFFFF8A65))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val currentBitmap = bitmap
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap.asImageBitmap(),
                contentDescription = "Foto Profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = profile.avatar,
                fontSize = (size.value * 0.5f).sp
            )
        }
    }
}

@Composable
private fun WorkoutGuideBannerCard(onOpenGuide: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenGuide),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, CoralOrange.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CoralOrangeMuted, DarkSurfaceCard)
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(CoralOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PANDUAN & TUTORIAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralOrange
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(ActiveGreenMuted, RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(text = "PEMULA", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ActiveGreen)
                        }
                    }
                    Text(
                        text = "Katalog Gerakan & Teknik Benar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Pelajari cara latihan punggung, dada, kaki, dll.",
                        fontSize = 11.sp,
                        color = TextGrayLight
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Buka",
                tint = CoralOrange,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatarDisplay(
                        profile = profile,
                        size = 72.dp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${profile.gender} • ${profile.age} Tahun • ${profile.fitnessLevel}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrayLight,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(CoralOrangeMuted, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = profile.fitnessGoal,
                                    color = CoralOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(ChipBackground, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = profile.favoriteCategory,
                                    color = TextGrayLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(DarkSurfaceElevated, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profil",
                        tint = CoralOrange,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (profile.bio.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceElevated, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "“${profile.bio}”",
                        fontSize = 12.sp,
                        color = TextGrayLight,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onEditClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = CoralOrange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ganti Foto, Nama & Data Profil",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BmiMetricCard(profile: UserProfile) {
    val bmi = profile.bmi
    val category = profile.bmiCategory
    val categoryColor = Color(profile.bmiCategoryColor)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kalkulator Indeks Massa Tubuh (BMI)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Box(
                    modifier = Modifier
                        .background(categoryColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = category,
                        color = categoryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$bmi",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = categoryColor,
                        fontSize = 38.sp
                    )
                    Text(
                        text = "Skor BMI",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrayMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricMiniCard(
                        title = "Tinggi",
                        value = "${profile.heightCm.toInt()} cm"
                    )
                    MetricMiniCard(
                        title = "Berat",
                        value = "${profile.weightKg.toInt()} kg"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rentang Berat Badan Ideal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = ActiveGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Berat Badan Ideal Anda:",
                            fontSize = 12.sp,
                            color = TextGrayLight
                        )
                        Text(
                            text = "${profile.idealWeightMin} kg – ${profile.idealWeightMax} kg",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricMiniCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 11.sp, color = TextGrayMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
    }
}

@Composable
private fun DailyHealthCard(
    profile: UserProfile,
    completedThisWeek: Int
) {
    val target = profile.targetWorkoutsPerWeek

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Target & Rekomendasi Sehat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Asupan Air Harian
                HealthInfoBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Opacity,
                    iconColor = Color(0xFF38BDF8),
                    title = "Target Air Harian",
                    mainValue = "${profile.dailyWaterLiters} L",
                    subValue = "${profile.dailyWaterMl} ml / hari"
                )

                // Estimasi Kalori BMR
                HealthInfoBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = CoralOrange,
                    title = "Kalori BMR Dasar",
                    mainValue = "${profile.estimatedBmrCalories}",
                    subValue = "kkal / hari"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Target Latihan Mingguan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = ActiveGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Target Latihan Mingguan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                            Text(
                                text = "$completedThisWeek dari $target sesi minggu ini",
                                fontSize = 12.sp,
                                color = TextGrayLight
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (completedThisWeek >= target) ActiveGreen else CoralOrange,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (completedThisWeek >= target) "Tercapai! 🎉" else "$completedThisWeek/$target",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthInfoBox(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    mainValue: String,
    subValue: String
) {
    Box(
        modifier = modifier
            .background(DarkSurfaceElevated, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 11.sp, color = TextGrayMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = mainValue,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = subValue,
                fontSize = 11.sp,
                color = TextGrayLight
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun EditProfileDialog(
    currentProfile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile.name) }
    var avatar by remember { mutableStateOf(currentProfile.avatar) }
    var customAvatarInput by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf(currentProfile.photoUri) }
    var bio by remember { mutableStateOf(currentProfile.bio) }
    var fitnessLevel by remember { mutableStateOf(currentProfile.fitnessLevel) }
    var favoriteCategory by remember { mutableStateOf(currentProfile.favoriteCategory) }
    var gender by remember { mutableStateOf(currentProfile.gender) }
    var ageText by remember { mutableStateOf(currentProfile.age.toString()) }
    var heightText by remember { mutableStateOf(currentProfile.heightCm.toInt().toString()) }
    var weightText by remember { mutableStateOf(currentProfile.weightKg.toInt().toString()) }
    var targetWorkouts by remember { mutableIntStateOf(currentProfile.targetWorkoutsPerWeek) }
    var fitnessGoal by remember { mutableStateOf(currentProfile.fitnessGoal) }

    // Launcher untuk memilih foto dari galeri HP
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

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
                    Text(
                        text = "Edit Profil & Avatar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = TextGrayMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bagian Foto / Avatar Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceElevated, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            UserAvatarDisplay(
                                profile = currentProfile.copy(avatar = avatar, photoUri = photoUri),
                                size = 64.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = if (photoUri != null) "Foto Galeri Aktif" else "Avatar Emoji Aktif",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextWhite
                                )
                                Text(
                                    text = if (photoUri != null) "Foto dari galeri HP kamu" else "Tampilan: $avatar",
                                    fontSize = 11.sp,
                                    color = TextGrayMuted
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoralOrange),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextWhite)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Pilih Foto", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }

                            if (photoUri != null) {
                                TextButton(
                                    onClick = { photoUri = null },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Pakai Emoji", color = ErrorRed, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Koleksi Avatar Pilihan
                Text(text = "Atau Pilih Avatar Favorit", fontSize = 13.sp, color = TextGrayLight, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AVAILABLE_AVATARS.forEach { av ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (avatar == av && photoUri == null) CoralOrange else DarkSurfaceElevated)
                                .clickable {
                                    avatar = av
                                    photoUri = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = av, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input Custom Emoji / Karakter Bebas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customAvatarInput,
                        onValueChange = { customAvatarInput = it },
                        placeholder = { Text("Ketik emoji / karakter lain...") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = CoralOrange,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (customAvatarInput.isNotBlank()) {
                                avatar = customAvatarInput.trim()
                                photoUri = null
                                customAvatarInput = ""
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(text = "Terapkan", fontSize = 12.sp, color = CoralOrange, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nama Lengkap
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Pengguna") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = CoralOrange,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bio / Motto Kebugaran
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Motto / Bio Kebugaran") },
                    placeholder = { Text("Contoh: Konsisten latihan demi masa depan sehat! 💪") },
                    singleLine = false,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = CoralOrange,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tingkat Kebugaran (Experience Level)
                Text(text = "Tingkat Pengalaman Latihan", fontSize = 13.sp, color = TextGrayLight, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FITNESS_LEVELS.forEach { level ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (fitnessLevel == level) CoralOrange else DarkSurfaceElevated)
                                .clickable { fitnessLevel = level },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = level,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (fitnessLevel == level) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Kategori Favorit
                Text(text = "Fokus Latihan Favorit", fontSize = 13.sp, color = TextGrayLight, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FAVORITE_CATEGORIES.forEach { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (favoriteCategory == cat) CoralOrange else DarkSurfaceElevated)
                                .clickable { favoriteCategory = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (favoriteCategory == cat) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Jenis Kelamin
                Text(text = "Jenis Kelamin", fontSize = 13.sp, color = TextGrayLight, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Pria", "Wanita").forEach { g ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (gender == g) CoralOrange else DarkSurfaceElevated)
                                .clickable { gender = g },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = g,
                                fontWeight = FontWeight.Bold,
                                color = if (gender == g) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Umur, Tinggi, Berat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it },
                        label = { Text("Umur") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = CoralOrange,
                            unfocusedBorderColor = DarkBorder
                        )
                    )

                    OutlinedTextField(
                        value = heightText,
                        onValueChange = { heightText = it },
                        label = { Text("Tinggi (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = CoralOrange,
                            unfocusedBorderColor = DarkBorder
                        )
                    )

                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Berat (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = CoralOrange,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Target Latihan Per Minggu (2 - 6)
                Text(
                    text = "Target Sesi Latihan Per Minggu: $targetWorkouts Sesi",
                    fontSize = 13.sp,
                    color = TextGrayLight,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (2..6).forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (targetWorkouts == num) CoralOrange else DarkSurfaceElevated)
                                .clickable { targetWorkouts = num },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${num}x",
                                fontWeight = FontWeight.Bold,
                                color = if (targetWorkouts == num) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Target Kebugaran (Goals)
                Text(text = "Tujuan Kebugaran", fontSize = 13.sp, color = TextGrayLight, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FITNESS_GOALS.forEach { goal ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (fitnessGoal == goal) CoralOrange else DarkSurfaceElevated)
                                .clickable { fitnessGoal = goal }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = goal,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (fitnessGoal == goal) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Simpan Profil
                Button(
                    onClick = {
                        val parsedAge = ageText.toIntOrNull() ?: currentProfile.age
                        val parsedHeight = heightText.toFloatOrNull() ?: currentProfile.heightCm
                        val parsedWeight = weightText.toFloatOrNull() ?: currentProfile.weightKg

                        val updated = currentProfile.copy(
                            name = name.ifBlank { "Sobat Bugar" },
                            avatar = avatar,
                            photoUri = photoUri,
                            bio = bio.ifBlank { "Konsisten latihan untuk tubuh lebih sehat! 💪" },
                            fitnessLevel = fitnessLevel,
                            favoriteCategory = favoriteCategory,
                            gender = gender,
                            age = parsedAge,
                            heightCm = parsedHeight,
                            weightKg = parsedWeight,
                            targetWorkoutsPerWeek = targetWorkouts,
                            fitnessGoal = fitnessGoal
                        )
                        onSave(updated)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                ) {
                    Text(
                        text = "Simpan Perubahan Profil",
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
