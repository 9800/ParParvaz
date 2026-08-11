package ir.parvaz.calendar.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.adhan.AdhanScheduler
import ir.parvaz.calendar.data.AdhanPrefs
import ir.parvaz.calendar.data.AppearancePrefs
import ir.parvaz.calendar.data.NotificationPrefs
import ir.parvaz.calendar.notification.DateNotificationHelper

private val BarBlue = Color(0xFF0E6BA8)
private val palette = listOf(
    Color(0xFF0E6BA8),
    Color(0xFF00BCD4),
    Color(0xFFE53935),
    Color(0xFF43A047),
    Color(0xFF8E24AA),
    Color(0xFF212121),
    Color(0xFFFAFAFA),
    Color(0xFFFFB300)
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenPermissions: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { NotificationPrefs(context) }
    val adhanPrefs = remember { AdhanPrefs(context) }
    val appearancePrefs = remember { AppearancePrefs(context) }

    var enabled by remember { mutableStateOf(prefs.enabled) }
    var bg by remember { mutableStateOf(prefs.bgColor) }
    var text by remember { mutableStateOf(prefs.textColor) }
    var box by remember { mutableStateOf(prefs.boxColor) }
    var boxText by remember { mutableStateOf(prefs.boxTextColor) }

    var fajr by remember { mutableStateOf(adhanPrefs.fajrEnabled) }
    var dhuhr by remember { mutableStateOf(adhanPrefs.dhuhrEnabled) }
    var maghrib by remember { mutableStateOf(adhanPrefs.maghribEnabled) }
    var sound by remember { mutableStateOf(adhanPrefs.soundIndex) }
    var vib by remember { mutableStateOf(adhanPrefs.vibration) }

    var fontSize by remember { mutableStateOf(appearancePrefs.fontSize) }
    var notifStyle by remember { mutableStateOf(appearancePrefs.notifStyle) }

    fun applyNotif() {
        DateNotificationHelper.refresh(context)
    }

    fun applyAdhan() {
        AdhanScheduler.scheduleAll(context)
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
                text = "تنظیمات",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                    Text("اذان", color = BarBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    SwitchRow("اذان صبح", fajr) {
                        fajr = it; adhanPrefs.fajrEnabled = it; applyAdhan()
                    }
                    SwitchRow("اذان ظهر", dhuhr) {
                        dhuhr = it; adhanPrefs.dhuhrEnabled = it; applyAdhan()
                    }
                    SwitchRow("اذان مغرب", maghrib) {
                        maghrib = it; adhanPrefs.maghribEnabled = it; applyAdhan()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("صدای اذان", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in 0..2) {
                            val label = "اذان ${i + 1}"
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (sound == i) BarBlue else Color(0xFFE0E0E0))
                                    .clickable {
                                        sound = i
                                        adhanPrefs.soundIndex = i
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    label,
                                    color = if (sound == i) Color.White else Color.Black,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                    Text(
                        "اگر فایل صوتی اذان داخل اپ قرار نگرفته باشد، صدای زنگ گوشی پخش می‌شود.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    SwitchRow("لرزش هنگام اذان", vib) {
                        vib = it; adhanPrefs.vibration = it
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
                    Text("نوار اعلان تاریخ", color = BarBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    SwitchRow("نوار اعلان دائمی تاریخ", enabled) {
                        enabled = it; prefs.enabled = it; applyNotif()
                    }

                    if (enabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("مدل نمایش نوار اعلان", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        NotifStylePreview(
                            style = 0,
                            selected = notifStyle == 0,
                            bg = bg,
                            text = text,
                            box = box,
                            boxText = boxText
                        ) {
                            notifStyle = 0
                            appearancePrefs.notifStyle = 0
                            applyNotif()
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        NotifStylePreview(
                            style = 1,
                            selected = notifStyle == 1,
                            bg = bg,
                            text = text,
                            box = box,
                            boxText = boxText
                        ) {
                            notifStyle = 1
                            appearancePrefs.notifStyle = 1
                            applyNotif()
                        }

                        ColorPicker("رنگ پس‌زمینه نوار", bg) {
                            bg = it; prefs.bgColor = it; applyNotif()
                        }
                        ColorPicker("رنگ قلم تاریخ", text) {
                            text = it; prefs.textColor = it; applyNotif()
                        }
                        ColorPicker("رنگ پس‌زمینه عدد", box) {
                            box = it; prefs.boxColor = it; applyNotif()
                        }
                        ColorPicker("رنگ قلم عدد", boxText) {
                            boxText = it; prefs.boxTextColor = it; applyNotif()
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
                    Text("ظاهر", color = BarBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("اندازه فونت: ${"٪${(fontSize * 100).toInt().let { faNum(it) }}"}", fontSize = 14.sp)
                    Slider(
                        value = fontSize,
                        onValueChange = {
                            fontSize = it
                            appearancePrefs.fontSize = it
                        },
                        valueRange = 0.8f..1.4f,
                        steps = 5
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("پایداری برنامه", color = BarBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = onOpenPermissions) {
                        Text("مدیریت دسترسی‌ها")
                    }
                }
            }
        }
    }
}

@Composable
private fun SwitchRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 15.sp)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun ColorPicker(title: String, current: Int, onSelect: (Int) -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(title, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        palette.forEach { color ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color)
                    .then(
                        if (current == color.toArgb()) {
                            Modifier.border(2.dp, Color(0xFF123B5C), CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .clickable { onSelect(color.toArgb()) }
            )
        }
    }
}

@Composable
private fun NotifStylePreview(
    style: Int,
    selected: Boolean,
    bg: Int,
    text: Int,
    box: Int,
    boxText: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Color(bg))
            .then(
                if (selected) {
                    Modifier.border(3.dp, BarBlue, CircleShape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("سه‌شنبه", color = Color(text), fontSize = 11.sp)
                Text("۲۰ مرداد ۱۴۰۵", color = Color(text), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (style == 1) {
                    Text("۱۱ اوت ۲۰۲۶ • ۲۶ صفر ۱۴۴۸", color = Color(text), fontSize = 10.sp)
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(box)),
                contentAlignment = Alignment.Center
            ) {
                Text("۲۰", color = Color(boxText), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun faNum(n: Int): String {
    return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
}
