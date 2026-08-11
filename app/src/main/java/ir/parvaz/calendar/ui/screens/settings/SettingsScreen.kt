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
import ir.parvaz.calendar.data.NotificationPrefs
import ir.parvaz.calendar.notification.DateNotificationHelper

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
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { NotificationPrefs(context) }

    var enabled by remember { mutableStateOf(prefs.enabled) }
    var bg by remember { mutableStateOf(prefs.bgColor) }
    var text by remember { mutableStateOf(prefs.textColor) }
    var box by remember { mutableStateOf(prefs.boxColor) }
    var boxText by remember { mutableStateOf(prefs.boxTextColor) }

    fun apply() {
        DateNotificationHelper.refresh(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("بازگشت") }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "تنظیمات",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("نوار اعلان دائمی تاریخ", fontSize = 16.sp)

            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = it
                    prefs.enabled = it
                    apply()
                }
            )
        }

        if (enabled) {
            ColorPicker("رنگ پس‌زمینه نوار", bg) {
                bg = it; prefs.bgColor = it; apply()
            }
            ColorPicker("رنگ قلم تاریخ", text) {
                text = it; prefs.textColor = it; apply()
            }
            ColorPicker("رنگ پس‌زمینه عدد", box) {
                box = it; prefs.boxColor = it; apply()
            }
            ColorPicker("رنگ قلم عدد", boxText) {
                boxText = it; prefs.boxTextColor = it; apply()
            }
        }
    }
}

@Composable
private fun ColorPicker(
    title: String,
    current: Int,
    onSelect: (Int) -> Unit
) {
    Spacer(modifier = Modifier.height(20.dp))

    Text(text = title, fontSize = 14.sp)

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
