package ir.parvaz.calendar.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.PersianDate
import ir.parvaz.calendar.core.events.EventsRepository
import ir.parvaz.calendar.ui.screens.daydetail.DayDetailScreen

private val BarBlue = Color(0xFF0E6BA8)
private val HolidayPink = Color(0xFFF48A8A)
private val TodayCircle = Color(0xFF0E6BA8)

private fun fa(n: Int): String {
    return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
}

@Composable
fun CalendarScreen(onBack: () -> Unit) {
    var year by remember { mutableStateOf(CalendarProvider.today().persian.year) }
    var month by remember { mutableStateOf(CalendarProvider.today().persian.month) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    if (selectedDay != null) {
        DayDetailScreen(
            year = year,
            month = month,
            day = selectedDay!!,
            onBack = { selectedDay = null }
        )
        return
    }

    val monthNames = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    val weekdayNames = listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه")

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BarBlue)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت", tint = Color.White)
            }
            Text(
                text = "تقویم",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            if (month == 1) {
                                month = 12
                                year--
                            } else {
                                month--
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "ماه قبل")
                        }

                        Text(
                            text = "${monthNames[month - 1]} ${fa(year)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = {
                            if (month == 12) {
                                month = 1
                                year++
                            } else {
                                month++
                            }
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "ماه بعد")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        weekdayNames.forEach { name ->
                            Text(
                                text = name,
                                modifier = Modifier.weight(1f),
                                color = Color.Gray,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val len = CalendarProvider.monthLength(year, month)
                    val lead = CalendarProvider.firstDayOffset(year, month)
                    val today = CalendarProvider.today()

                    val cells: List<Int?> = List(lead) { null } + (1..len).toList()
                    cells.chunked(7).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (i in 0 until 7) {
                                val day = row.getOrNull(i)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 6.dp)
                                        .then(
                                            if (day != null) {
                                                Modifier.clickable { selectedDay = day }
                                            } else {
                                                Modifier
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (day != null) {
                                        val isToday = year == today.persian.year &&
                                            month == today.persian.month &&
                                            day == today.persian.day

                                        val g = CalendarProvider.jalaliToGregorian(year, month, day)
                                        val h = CalendarProvider.hijriOf(g)
                                        val ev = EventsRepository.todayEvents(
                                            PersianDate(year, month, day), h, g
                                        )
                                        val isHoliday = ev.any { it.holiday }
                                        val hasEvent = ev.isNotEmpty()

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isToday) TodayCircle else Color.Transparent
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = fa(day),
                                                    color = when {
                                                        isToday -> Color.White
                                                        isHoliday -> HolidayPink
                                                        else -> Color.Black
                                                    },
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (hasEvent) HolidayPink else Color.Transparent
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
