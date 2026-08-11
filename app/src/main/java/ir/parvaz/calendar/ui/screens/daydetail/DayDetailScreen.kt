package ir.parvaz.calendar.ui.screens.daydetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
import ir.parvaz.calendar.core.date.PersianDate
import ir.parvaz.calendar.core.events.EventsRepository

private val BarBlue = Color(0xFF0E6BA8)

@Composable
fun DayDetailScreen(year: Int, month: Int, day: Int, onBack: () -> Unit) {
    val persian = PersianDate(year, month, day)
    val gregorian = CalendarProvider.jalaliToGregorian(year, month, day)
    val hijri = CalendarProvider.hijriOf(gregorian)
    val events = EventsRepository.todayEvents(persian, hijri, gregorian)

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
                text = "جزئیات روز",
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
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = DateFormatter.format(persian),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = DateFormatter.format(gregorian),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    if (hijri != null) {
                        Text(
                            text = DateFormatter.format(hijri),
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "مناسبت‌ها",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (events.isEmpty()) {
                        Text(
                            text = "مناسبتی برای این روز ثبت نشده است.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    } else {
                        events.forEach { event ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = event.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = event.source,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (event.holiday) {
                                    Text(
                                        text = "تعطیل",
                                        color = Color(0xFFE53935),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
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
