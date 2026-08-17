package ir.parvaz.calendar.ui.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.parvaz.calendar.R
import ir.parvaz.calendar.core.city.City
import ir.parvaz.calendar.core.city.Cities

private val HeaderBlue = Color(0xFF0E6BA8)
private val HolidayPink = Color(0xFFF48A8A)
private val TodayCircle = Color(0xFFBBD6EE)
private val White70 = Color(0xB3FFFFFF)

private fun fa(n: Int): String {
    return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HeaderBlue)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "منو", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "پر پرواز",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(48.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = state.weekday,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = state.hijriDate, color = White70, fontSize = 13.sp)
                }

                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fa(state.todayDay),
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = state.monthTitle,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = state.gregorianDate, color = White70, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                state.weekdayNames.forEach { name ->
                    Text(
                        text = name,
                        modifier = Modifier.weight(1f),
                        color = White70,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dayCells: List<DayCell?> = List(state.leadingBlanks) { null } + state.monthDays
            dayCells.chunked(7).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (i in 0 until 7) {
                        val cell = row.getOrNull(i)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (cell != null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (cell.isToday) TodayCircle else Color.Transparent
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = fa(cell.day),
                                            color = when {
                                                cell.isToday -> Color(0xFF123B5C)
                                                cell.isHoliday -> HolidayPink
                                                else -> Color.White
                                            },
                                            fontSize = 15.sp
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (cell.hasEvent) HolidayPink else Color.Transparent
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.today_events_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.events.isEmpty()) {
                        Text(
                            text = "مناسبتی برای امروز ثبت نشده است.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        state.events.forEach { ev ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = ev.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (ev.holiday) {
                                    Text(
                                        text = "تعطیل",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "اوقات شرعی ${state.cityName}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        TextButton(onClick = { viewModel.openCityPicker() }) {
                            Text("تغییر شهر")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val times = state.prayerTimes
                    if (times == null) {
                        Text(
                            text = "برای نمایش اوقات شرعی، شهر را انتخاب کنید.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        PrayerRow("اذان صبح", times.fajr)
                        PrayerRow("طلوع آفتاب", times.sunrise)
                        PrayerRow("اذان ظهر", times.dhuhr)
                        PrayerRow("عصر", times.asr)
                        PrayerRow("غروب آفتاب", times.sunset)
                        PrayerRow("اذان مغرب", times.maghrib)
                        PrayerRow("نیمه‌شب شرعی", times.midnight)
                    }
                }
            }
        }
    }

    if (state.showCityPicker) {
        var query by remember { mutableStateOf("") }
        var province by remember { mutableStateOf<String?>(null) }

        val searchResults = if (query.isBlank()) {
            null
        } else {
            Cities.all.filter { it.name.contains(query) }
        }

        AlertDialog(
            onDismissRequest = {
                if (state.cityName.isNotEmpty()) viewModel.closeCityPicker()
            },
            title = { Text("انتخاب شهر") },
            text = {
                Column {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("جستجوی شهر") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        if (searchResults != null) {
                            items(searchResults) { city ->
                                CityRow(city) { viewModel.selectCity(city.id) }
                            }
                        } else if (province == null) {
                            items(Cities.provinces) { p ->
                                TextButton(
                                    onClick = { province = p },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(p, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            item {
                                TextButton(
                                    onClick = { province = null },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("بازگشت به استان‌ها")
                                }
                            }
                            items(Cities.byProvince(province!!)) { city ->
                                CityRow(city) { viewModel.selectCity(city.id) }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                if (state.cityName.isNotEmpty()) {
                    TextButton(onClick = { viewModel.closeCityPicker() }) {
                        Text("بستن")
                    }
                }
            }
        )
    }
}

@Composable
private fun CityRow(city: City, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(city.name)
    }
}

@Composable
private fun PrayerRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
