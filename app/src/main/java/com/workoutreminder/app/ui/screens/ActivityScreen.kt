package com.workoutreminder.app.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity
import com.workoutreminder.app.data.model.BadgeItem
import com.workoutreminder.app.data.model.StreakInfo
import com.workoutreminder.app.data.model.UserLevelInfo
import com.workoutreminder.app.ui.components.DEFAULT_SPLITS
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ActivityScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val streakInfo by viewModel.streakInfo.collectAsState()
    val levelInfo by viewModel.userLevelInfo.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val allHistories by viewModel.allHistories.collectAsState()

    var showQuickLogDialog by remember { mutableStateOf(false) }
    var selectedBadgeForDetail by remember { mutableStateOf<BadgeItem?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Streak Card
        item {
            StreakCard(streakInfo = streakInfo)
        }

        // 2. Level & XP Progress Card
        item {
            LevelProgressCard(levelInfo = levelInfo)
        }

        // 3. Tombol Catat Latihan Cepat
        item {
            Button(
                onClick = { showQuickLogDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = TextWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Catat Selesai Latihan Cepat (+50 XP)",
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    fontSize = 15.sp
                )
            }
        }

        // 4. Header Badges / Prestasi
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = CoralOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lencana Prestasi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                val unlockedCount = badges.count { it.isUnlocked }
                Text(
                    text = "$unlockedCount / ${badges.size} Terbuka",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CoralOrange,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 5. Grid Lencana (Badges)
        item {
            BadgesGrid(
                badges = badges,
                onBadgeClick = { selectedBadgeForDetail = it }
            )
        }

        // 6. Header Riwayat Latihan Lengkap
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Lengkap Latihan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "${allHistories.size} Total Sesi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrayMuted
                )
            }
        }

        // 7. Daftar Riwayat
        if (allHistories.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Belum Ada Riwayat Latihan",
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tekan tombol catat di atas setelah kamu berolahraga!",
                            fontSize = 13.sp,
                            color = TextGrayMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(
                items = allHistories,
                key = { it.id }
            ) { history ->
                HistoryItemRow(
                    history = history,
                    onDelete = { viewModel.deleteHistory(history.id) }
                )
            }
        }
    }

    // Modal Catat Latihan Cepat
    if (showQuickLogDialog) {
        QuickLogDialog(
            onDismiss = { showQuickLogDialog = false },
            onConfirm = { split ->
                viewModel.markWorkoutCompleted(split)
                showQuickLogDialog = false
            }
        )
    }

    // Detail Badge Modal
    if (selectedBadgeForDetail != null) {
        BadgeDetailDialog(
            badge = selectedBadgeForDetail!!,
            onDismiss = { selectedBadgeForDetail = null }
        )
    }
}

@Composable
private fun StreakCard(streakInfo: StreakInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, CoralOrange.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF33160D),
                            DarkSurfaceCard
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(CoralOrangeMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = CoralOrange,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Streak Latihan",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextGrayLight
                            )
                            Text(
                                text = "${streakInfo.currentStreak} Hari Beruntun! 🔥",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = CoralOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (streakInfo.hasWorkedOutToday) {
                            "✅ Latihan hari ini selesai! Keren!"
                        } else {
                            "⚡ Latihan hari ini belum dicatat"
                        },
                        fontSize = 12.sp,
                        color = if (streakInfo.hasWorkedOutToday) ActiveGreen else TextGrayMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .background(DarkSurfaceElevated, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Rekor Terbaik", fontSize = 10.sp, color = TextGrayMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${streakInfo.bestStreak} Hari",
                            fontSize = 15.sp,
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
private fun LevelProgressCard(levelInfo: UserLevelInfo) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(ActiveGreenMuted, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = ActiveGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Level ${levelInfo.currentLevel}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = levelInfo.title,
                            fontSize = 12.sp,
                            color = ActiveGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(DarkSurfaceElevated, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${levelInfo.totalXp} XP",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = TextWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Bar XP
            LinearProgressIndicator(
                progress = { levelInfo.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = ActiveGreen,
                trackColor = DarkSurfaceElevated
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${levelInfo.totalXp} XP",
                    fontSize = 11.sp,
                    color = TextGrayMuted
                )
                Text(
                    text = "Target Level Berikutnya: ${levelInfo.levelMaxXp} XP",
                    fontSize = 11.sp,
                    color = TextGrayMuted
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BadgesGrid(
    badges: List<BadgeItem>,
    onBadgeClick: (BadgeItem) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        badges.forEach { badge ->
            BadgeGridItem(
                badge = badge,
                onClick = { onBadgeClick(badge) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BadgeGridItem(
    badge: BadgeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) DarkSurfaceElevated else Color(0xFF161922)
        ),
        border = BorderStroke(
            1.dp,
            if (badge.isUnlocked) CoralOrange.copy(alpha = 0.5f) else DarkBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (badge.isUnlocked) CoralOrangeMuted else DarkSurfaceCard,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (badge.isUnlocked) {
                    Text(text = badge.icon, fontSize = 24.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Terkunci",
                        tint = TextGrayMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = badge.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (badge.isUnlocked) TextWhite else TextGrayMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = if (badge.isUnlocked) "Terbuka 🎉" else badge.progressText,
                fontSize = 11.sp,
                color = if (badge.isUnlocked) ActiveGreen else TextGrayMuted
            )
        }
    }
}

@Composable
private fun HistoryItemRow(
    history: WorkoutHistoryEntity,
    onDelete: () -> Unit
) {
    val dateTimeStr = Instant.ofEpochMilli(history.completedAt)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(ActiveGreenMuted, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ActiveGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = history.splitName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextWhite
                    )
                    Text(
                        text = dateTimeStr,
                        fontSize = 12.sp,
                        color = TextGrayMuted
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Riwayat",
                    tint = TextGrayMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedSplit by remember { mutableStateOf("Dada") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkSurfaceCard,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Catat Selesai Latihan Cepat",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Pilih split/fokus latihan yang baru saja kamu selesaikan:",
                    fontSize = 13.sp,
                    color = TextGrayLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DEFAULT_SPLITS.forEach { split ->
                        val isSelected = selectedSplit == split
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CoralOrange else DarkSurfaceElevated)
                                .clickable { selectedSplit = split }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = split,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TextWhite else TextGrayLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = TextGrayMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(selectedSplit) },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                    ) {
                        Text("Simpan (+50 XP)", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeDetailDialog(
    badge: BadgeItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkSurfaceCard,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (badge.isUnlocked) CoralOrangeMuted else DarkSurfaceElevated,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = badge.icon, fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrayLight,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .background(
                            if (badge.isUnlocked) ActiveGreenMuted else DarkSurfaceElevated,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (badge.isUnlocked) "Status: Terbuka & Tercapai! 🎉" else "Progres: ${badge.progressText}",
                        color = if (badge.isUnlocked) ActiveGreen else CoralOrange,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                ) {
                    Text("Tutup", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
