package ir.parvaz.calendar.ui.screens.weather

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.core.city.Cities
import ir.parvaz.calendar.data.CityStore
import ir.parvaz.calendar.data.WeatherPrefs
import ir.parvaz.weather.WeatherData
import ir.parvaz.weather.WeatherService
import kotlinx.coroutines.launch
import kotlinx.coroutines.rememberCoroutineScope

private val BarBlue = Color(0xFF0E6BA8)

private fun fa(n: Int): String {
    return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
}

@Composable
fun WeatherScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val cityStore = remember { CityStore(context) }
    val weatherPrefs = remember { WeatherPrefs(context) }
    val scope = rememberCoroutineScope()

    val city = Cities.byId(cityStore.savedCityId())

    var weather by remember { mutableStateOf<WeatherData?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var source by remember { mutableStateOf(weatherPrefs.source) }
    var accuKey by remember { mutableStateOf(weatherPrefs.accuWeatherKey) }
    var showSettings by remember { mutableStateOf(false) }

    fun load() {
        val c = city ?: return
        loading = true
        error = null
        scope.launch {
            val result = when (source) {
                0 -> WeatherService.fetchOpenMeteo(c.lat, c.lng)
                1 -> if (accuKey.isNotBlank()) {
                    WeatherService.fetchAccuWeather(c.lat, c.lng, accuKey)
                } else {
                    null
                }
                else -> null
            }
            weather = result
            loading = false
            if (result == null) {
                error = if (source == 1 && accuKey.isBlank()) {
                    "برای استفاده از AccuWeather باید API Key وارد کنید."
                } else {
                    "دریافت اطلاعات ناموفق بود. اینترنت را بررسی کنید."
                }
            }
        }
    }

    LaunchedEffect(source, city?.id) {
        load()
    }

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
                text = "آب‌وهوا ${city?.name ?: ""}",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { load() }) {
                Icon(Icons.Default.Refresh, contentDescription = "بروزرسانی", tint = Color.White)
            }
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
                    Text("منبع آب‌وهوا", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BarBlue)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SourceChip("Open-Meteo (بدون کلید)", source == 0) {
                            source = 0
                            weatherPrefs.source = 0
                        }
                        SourceChip("AccuWeather", source == 1) {
                            source = 1
                            weatherPrefs.source = 1
                            showSettings = true
                        }
                    }

                    if (showSettings || source == 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = accuKey,
                            onValueChange = {
                                accuKey = it
                                weatherPrefs.accuWeatherKey = it
                            },
                            label = { Text("AccuWeather API Key") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (weather != null) {
                val w = weather!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = w.icon,
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${fa(w.temp.toInt())}°",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = BarBlue
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = w.description,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("رطوبت", fontSize = 13.sp, color = Color.Gray)
                                Text("${fa(w.humidity)}٪", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("باد", fontSize = 13.sp, color = Color.Gray)
                                Text("${fa(w.windSpeed.toInt())} کیلومتر", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(error!!, fontSize = 15.sp, color = Color(0xFFE53935))
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { load() }) {
                            Text("تلاش دوباره")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (selected) BarBlue else Color(0xFFE0E0E0),
                androidx.compose.foundation.shape.CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp
        )
    }
}
