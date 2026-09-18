package com.workoutreminder.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurface
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pilih Jam Pengingat",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = TextWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = DarkSurfaceElevated,
                        clockDialSelectedContentColor = TextWhite,
                        clockDialUnselectedContentColor = TextGrayLight,
                        selectorColor = CoralOrange,
                        periodSelectorBorderColor = DarkBorder,
                        periodSelectorSelectedContainerColor = CoralOrange,
                        periodSelectorUnselectedContainerColor = DarkSurfaceElevated,
                        periodSelectorSelectedContentColor = TextWhite,
                        periodSelectorUnselectedContentColor = TextGrayMuted,
                        timeSelectorSelectedContainerColor = CoralOrange,
                        timeSelectorUnselectedContainerColor = DarkSurfaceElevated,
                        timeSelectorSelectedContentColor = TextWhite,
                        timeSelectorUnselectedContentColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = TextGrayMuted)
                    }
                    Button(
                        onClick = {
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("OK", color = TextWhite)
                    }
                }
            }
        }
    }
}
