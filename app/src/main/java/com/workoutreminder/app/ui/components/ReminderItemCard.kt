package com.workoutreminder.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.ActiveGreenMuted
import com.workoutreminder.app.ui.theme.ChipBackground
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurfaceCard
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.ErrorRed
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite

private val DAY_NAMES_SHORT = mapOf(
    1 to "Sen",
    2 to "Sel",
    3 to "Rab",
    4 to "Kam",
    5 to "Jum",
    6 to "Sab",
    7 to "Min"
)

@Composable
fun ReminderItemCard(
    reminder: ReminderEntity,
    onToggleActive: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMarkDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDays = if (reminder.daysOfWeek.size == 7) {
        "Setiap Hari"
    } else {
        reminder.daysOfWeek
            .sorted()
            .mapNotNull { DAY_NAMES_SHORT[it] }
            .joinToString(separator = ", ")
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(
            1.dp,
            if (reminder.isActive) DarkBorder else DarkBorder.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Split Badge + Switch Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Split Badge
                Box(
                    modifier = Modifier
                        .background(
                            if (reminder.isActive) CoralOrange else DarkSurfaceElevated,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = reminder.splitName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (reminder.isActive) TextWhite else TextGrayMuted
                    )
                }

                // Switch Aktif / Nonaktif
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (reminder.isActive) "Aktif" else "Nonaktif",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (reminder.isActive) ActiveGreen else TextGrayMuted,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = reminder.isActive,
                        onCheckedChange = onToggleActive,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = ActiveGreen,
                            uncheckedThumbColor = TextGrayMuted,
                            uncheckedTrackColor = DarkSurfaceElevated
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Middle Row: Bold Time and Days
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Waktu dalam tipografi tegas & besar
                    Text(
                        text = reminder.formattedTime,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = if (reminder.isActive) TextWhite else TextGrayMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = formattedDays,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (reminder.isActive) TextGrayLight else TextGrayMuted
                    )
                }

                // Action Buttons: Selesai, Edit, Hapus
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Selesai Cepat
                    FilledTonalIconButton(
                        onClick = onMarkDone,
                        modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = ActiveGreenMuted,
                            contentColor = ActiveGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Tandai Selesai",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tombol Edit
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = TextGrayLight
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Ubah",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tombol Hapus
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = ErrorRed
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
