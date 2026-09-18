package com.workoutreminder.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.workoutreminder.app.ui.theme.CoralOrange
import com.workoutreminder.app.ui.theme.DarkBorder
import com.workoutreminder.app.ui.theme.DarkSurfaceCard
import com.workoutreminder.app.ui.theme.DarkSurfaceElevated
import com.workoutreminder.app.ui.theme.TextGrayLight
import com.workoutreminder.app.ui.theme.TextGrayMuted
import com.workoutreminder.app.ui.theme.TextWhite
import java.time.LocalDate

data class DayInfo(
    val dayNumber: Int, // 1 = Senin s/d 7 = Minggu
    val shortName: String,
    val fullName: String
)

val DAYS_LIST = listOf(
    DayInfo(1, "Sen", "Senin"),
    DayInfo(2, "Sel", "Selasa"),
    DayInfo(3, "Rab", "Rabu"),
    DayInfo(4, "Kam", "Kamis"),
    DayInfo(5, "Jum", "Jumat"),
    DayInfo(6, "Sab", "Sabtu"),
    DayInfo(7, "Min", "Minggu")
)

@Composable
fun WeeklyScheduleCard(
    reminders: List<ReminderEntity>,
    modifier: Modifier = Modifier
) {
    val todayDayNumber = LocalDate.now().dayOfWeek.value // 1..7

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jadwal Mingguan",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextWhite
                )
                Text(
                    text = "Senin – Minggu",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextGrayMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row horizontal yang dapat di-scroll jika layar kecil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DAYS_LIST.forEach { dayInfo ->
                    val isToday = dayInfo.dayNumber == todayDayNumber

                    // Cari split aktif di hari ini
                    val splitsForDay = reminders
                        .filter { it.isActive && it.daysOfWeek.contains(dayInfo.dayNumber) }
                        .map { it.splitName }
                        .distinct()

                    DaySchedulePill(
                        dayInfo = dayInfo,
                        isToday = isToday,
                        splits = splitsForDay
                    )
                }
            }
        }
    }
}

@Composable
private fun DaySchedulePill(
    dayInfo: DayInfo,
    isToday: Boolean,
    splits: List<String>
) {
    val borderColor = if (isToday) CoralOrange else DarkBorder
    val backgroundColor = if (isToday) DarkSurfaceElevated else Color(0xFF18181A)

    Box(
        modifier = Modifier
            .width(82.dp)
            .background(backgroundColor, RoundedCornerShape(14.dp))
            .padding(1.dp)
    ) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            border = BorderStroke(if (isToday) 1.5.dp else 1.dp, borderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isToday) {
                    Text(
                        text = "HARI INI",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = CoralOrange,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = dayInfo.shortName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isToday) TextWhite else TextGrayLight
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (splits.isNotEmpty()) {
                    splits.take(2).forEach { split ->
                        Text(
                            text = split,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (split.equals("Istirahat", ignoreCase = true)) TextGrayMuted else ActiveGreen,
                            maxLines = 1,
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }
                    if (splits.size > 2) {
                        Text(
                            text = "+${splits.size - 2}",
                            fontSize = 10.sp,
                            color = TextGrayMuted
                        )
                    }
                } else {
                    Text(
                        text = "Istirahat",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextGrayMuted
                    )
                }
            }
        }
    }
}
