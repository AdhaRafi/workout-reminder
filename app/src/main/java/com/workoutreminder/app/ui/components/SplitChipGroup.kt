package com.workoutreminder.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workoutreminder.app.ui.theme.ActiveGreen
import com.workoutreminder.app.ui.theme.ChipBackground
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite

val DEFAULT_SPLITS = listOf(
    "Dada",
    "Punggung",
    "Bahu",
    "Lengan",
    "Kaki",
    "Perut",
    "Kardio",
    "Full Body",
    "Istirahat"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SplitChipGroup(
    selectedSplit: String,
    isCustom: Boolean,
    customText: String,
    onSplitSelected: (String) -> Unit,
    onCustomToggled: (Boolean) -> Unit,
    onCustomTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Jenis / Split Latihan",
            color = TextGrayLight,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DEFAULT_SPLITS.forEach { split ->
                val isSelected = !isCustom && selectedSplit.equals(split, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onCustomToggled(false)
                        onSplitSelected(split)
                    },
                    label = { Text(split, color = if (isSelected) TextWhite else TextGrayLight) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = ChipBackground,
                        selectedContainerColor = CoralOrange,
                        selectedLabelColor = TextWhite
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = DarkBorder,
                        selectedBorderColor = CoralOrange
                    )
                )
            }

            // Opsi Kustom
            FilterChip(
                selected = isCustom,
                onClick = { onCustomToggled(!isCustom) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Custom Split",
                        tint = if (isCustom) TextWhite else TextGrayMuted
                    )
                },
                label = { Text("Kustom / Lainnya", color = if (isCustom) TextWhite else TextGrayLight) },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = ChipBackground,
                    selectedContainerColor = CoralOrange,
                    selectedLabelColor = TextWhite
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isCustom,
                    borderColor = DarkBorder,
                    selectedBorderColor = CoralOrange
                )
            )
        }

        AnimatedVisibility(visible = isCustom) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    value = customText,
                    onValueChange = onCustomTextChanged,
                    label = { Text("Nama Split Kustom (mis. Kalistenik, Berenang)") },
                    placeholder = { Text("Ketik nama latihan...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceElevated,
                        unfocusedContainerColor = DarkSurfaceElevated,
                        focusedBorderColor = CoralOrange,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = CoralOrange,
                        unfocusedLabelColor = TextGrayMuted
                    )
                )
            }
        }
    }
}
