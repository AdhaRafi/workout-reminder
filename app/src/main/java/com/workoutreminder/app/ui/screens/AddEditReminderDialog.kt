package com.workoutreminder.app.ui.screens

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.ui.components.DEFAULT_SPLITS
import com.workoutreminder.app.ui.components.SplitChipGroup
import com.workoutreminder.app.ui.components.TimePickerModal
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.ChipBackground
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurface
import com.workoutreminder.app.ui.theme.DarkSurfaceCard
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.ErrorRed
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite

private val ALL_DAYS = listOf(
    1 to "Sen",
    2 to "Sel",
    3 to "Rab",
    4 to "Kam",
    5 to "Jum",
    6 to "Sab",
    7 to "Min"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditReminderDialog(
    initialReminder: ReminderEntity? = null,
    onDismiss: () -> Unit,
    onSave: (ReminderEntity) -> Unit
) {
    val isEditing = initialReminder != null

    val isPresetSplit = initialReminder?.splitName in DEFAULT_SPLITS
    var selectedSplit by remember { mutableStateOf(initialReminder?.splitName ?: "Dada") }
    var isCustomSplit by remember { mutableStateOf(initialReminder?.isCustomSplit ?: (!isPresetSplit && isEditing)) }
    var customSplitText by remember { mutableStateOf(if (isCustomSplit) initialReminder?.splitName ?: "" else "") }

    var selectedHour by remember { mutableIntStateOf(initialReminder?.hour ?: 7) }
    var selectedMinute by remember { mutableIntStateOf(initialReminder?.minute ?: 0) }

    // Multi-select hari: default Senin & Kamis untuk latihan baru jika kosong
    var selectedDays by remember {
        mutableStateOf(
            initialReminder?.daysOfWeek?.toSet() ?: setOf(1, 4)
        )
    }

    var isActive by remember { mutableStateOf(initialReminder?.isActive ?: true) }
    var useSoundAlarm by remember { mutableStateOf(initialReminder?.useSoundAlarm ?: true) }
    var selectedSoundName by remember { mutableStateOf(initialReminder?.alarmSoundName ?: "Energetic Gym Beat") }
    var selectedSoundUri by remember { mutableStateOf(initialReminder?.alarmSoundUri) }

    val context = LocalContext.current
    val ringtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
            if (uri != null) {
                val ringtone = RingtoneManager.getRingtone(context, uri)
                val title = ringtone.getTitle(context) ?: "Musik Kustom HP"
                selectedSoundName = title
                selectedSoundUri = uri.toString()
            }
        }
    }

    var showTimePicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkSurface,
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEditing) "Ubah Pengingat" else "Tambah Pengingat Latihan",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 20.sp,
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

                // 1. Pemilih Split / Latihan
                SplitChipGroup(
                    selectedSplit = selectedSplit,
                    isCustom = isCustomSplit,
                    customText = customSplitText,
                    onSplitSelected = { selectedSplit = it },
                    onCustomToggled = { isCustomSplit = it },
                    onCustomTextChanged = { customSplitText = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Pemilih Jam Pengingat
                Text(
                    text = "Jam Pengingat",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextGrayLight
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { showTimePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Pilih Jam",
                                tint = CoralOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = String.format("%02d:%02d", selectedHour, selectedMinute),
                                style = MaterialTheme.typography.displayLarge,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }

                        Text(
                            text = "Ubah Waktu",
                            color = CoralOrange,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Pemilih Hari dalam Seminggu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hari Latihan",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextGrayLight
                    )
                    TextButton(
                        onClick = {
                            selectedDays = if (selectedDays.size == 7) emptySet() else (1..7).toSet()
                        }
                    ) {
                        Text(
                            text = if (selectedDays.size == 7) "Hapus Semua" else "Setiap Hari",
                            color = CoralOrange,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol toggle 7 hari
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ALL_DAYS.forEach { (dayNum, shortLabel) ->
                        val isSelected = selectedDays.contains(dayNum)
                        DayTogglePill(
                            label = shortLabel,
                            isSelected = isSelected,
                            onToggle = {
                                selectedDays = if (isSelected) {
                                    selectedDays - dayNum
                                } else {
                                    selectedDays + dayNum
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Status Aktif / Nonaktif Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Status Pengingat",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite
                        )
                        Text(
                            text = if (isActive) "Alarm akan aktif" else "Alarm dijeda sementara",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrayMuted
                        )
                    }

                    Switch(
                        checked = isActive,
                        onCheckedChange = { isActive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = ActiveGreen,
                            uncheckedThumbColor = TextGrayMuted,
                            uncheckedTrackColor = DarkSurfaceElevated
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Alarm Suara & Layar Penuh Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Alarm Layar Penuh & Suara",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite
                        )
                        Text(
                            text = if (useSoundAlarm) "Layar penuh + suara alarm berdering" else "Hanya notifikasi push standar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextGrayMuted,
                            fontSize = 12.sp
                        )
                    }

                    Switch(
                        checked = useSoundAlarm,
                        onCheckedChange = { useSoundAlarm = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = CoralOrange,
                            uncheckedThumbColor = TextGrayMuted,
                            uncheckedTrackColor = DarkSurfaceElevated
                        )
                    )
                }

                // 6. Pemilih Suara / Musik Alarm
                if (useSoundAlarm) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Pilihan Musik / Nada Alarm",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextGrayLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val presetSounds = listOf(
                        "Energetic Gym Beat",
                        "Hard Rock Workout",
                        "Electronic Bass Drop",
                        "Epic Motivation Horn",
                        "Nada Alarm Klasik"
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetSounds.forEach { sound ->
                            val isSelected = selectedSoundName == sound
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) CoralOrange else DarkSurfaceElevated)
                                    .clickable {
                                        selectedSoundName = sound
                                        selectedSoundUri = null
                                    }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = sound,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TextWhite else TextGrayLight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tombol Pilih dari Ringtone / Musik HP
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                                putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM or RingtoneManager.TYPE_RINGTONE)
                                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
                            }
                            ringtonePickerLauncher.launch(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralOrange)
                    ) {
                        Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = CoralOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedSoundUri != null) "Musik HP: $selectedSoundName" else "Pilih dari Musik / Ringtone HP 📁",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }

                // Error Message Display
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Simpan (Besar & Mudah Disentuh)
                Button(
                    onClick = {
                        val finalSplitName = if (isCustomSplit) {
                            customSplitText.trim()
                        } else {
                            selectedSplit.trim()
                        }

                        when {
                            finalSplitName.isBlank() -> {
                                errorMessage = "Nama jenis/split latihan tidak boleh kosong."
                            }
                            selectedDays.isEmpty() -> {
                                errorMessage = "Pilih minimal satu hari dalam seminggu."
                            }
                            else -> {
                                errorMessage = null
                                val reminder = ReminderEntity(
                                    id = initialReminder?.id ?: 0,
                                    splitName = finalSplitName,
                                    isCustomSplit = isCustomSplit,
                                    hour = selectedHour,
                                    minute = selectedMinute,
                                    daysOfWeek = selectedDays.toList().sorted(),
                                    isActive = isActive,
                                    useSoundAlarm = useSoundAlarm,
                                    alarmSoundName = selectedSoundName,
                                    alarmSoundUri = selectedSoundUri
                                )
                                onSave(reminder)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralOrange)
                ) {
                    Text(
                        text = if (isEditing) "Simpan Perubahan" else "Tambah Pengingat",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }
    }

    if (showTimePicker) {
        TimePickerModal(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            onConfirm = { hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
private fun DayTogglePill(
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(if (isSelected) CoralOrange else ChipBackground)
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) TextWhite else TextGrayLight
        )
    }
}
