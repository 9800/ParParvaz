package ir.parvaz.calendar.ui.screens.permissions

import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun PermissionsScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    var tick by remember { mutableStateOf(0) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) tick++
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    val batteryOk = remember(tick) { isBatteryOk(context) }
    val notifOk = remember(tick) { NotificationManagerCompat.areNotificationsEnabled(context) }
    val exactOk = remember(tick) { isExactOk(context) }
    val fullOk = remember(tick) { isFullOk(context) }
    val overlayOk = remember(tick) { Settings.canDrawOverlays(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("بازگشت") }
            Spacer(modifier = Modifier.weight(1f))
            Text("مدیریت دسترسی‌ها", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        PermissionRow(
            title = "بهینه‌سازی باتری",
            desc = "برای پخش مطمئن اذان، بهینه‌سازی باتری را برای اپ غیرفعال کنید",
            granted = batteryOk
        ) { requestBattery(context) }

        PermissionRow(
            title = "اعلان‌ها",
            desc = "مجوز نمایش اعلان برای نوار تاریخ و هشدار اذان",
            granted = notifOk
        ) { openNotifications(context) }

        PermissionRow(
            title = "زمان‌بندی دقیق",
            desc = "برای پخش اذان دقیقاً سر وقت",
            granted = exactOk
        ) { requestExact(context) }

        PermissionRow(
            title = "اعلان تمام‌صفحه",
            desc = "نمایش صفحه اذان وقتی گوشی قفل است",
            granted = fullOk
        ) { requestFull(context) }

        PermissionRow(
            title = "نمایش روی برنامه‌ها",
            desc = "برای نمایش صفحه اذان روی سایر برنامه‌ها",
            granted = overlayOk
        ) { requestOverlay(context) }

        PermissionRow(
            title = "آغاز خودکار",
            desc = "در گوشی‌های شیائومی و برخی برندها، آغاز خودکار را فعال کنید",
            granted = true
        ) { openAutostart(context) }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    desc: String,
    granted: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = if (granted) "فعال" else "نیاز به اقدام",
                    color = if (granted) Color(0xFF43A047) else Color(0xFFE53935),
                    fontSize = 13.sp
                )
            }
            Text(desc, fontSize = 12.sp, color = Color.Gray)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onClick) { Text("باز کردن تنظیمات") }
            }
        }
    }
}

private fun isBatteryOk(context: Context): Boolean {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
}

private fun isExactOk(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.canScheduleExactAlarms()
    } else true
}

private fun isFullOk(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        NotificationManagerCompat.canUseFullScreenIntent(context)
    } else true
}

private fun safeStart(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (exception: Exception) {
        openDetails(context)
    }
}

private fun openDetails(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${context.packageName}")
    )
    try {
        context.startActivity(intent)
    } catch (exception: Exception) {
    }
}

private fun requestBattery(context: Context) {
    val intent = Intent(
        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        Uri.parse("package:${context.packageName}")
    )
    safeStart(context, intent)
}

private fun openNotifications(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    safeStart(context, intent)
}

private fun requestExact(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(
            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
            Uri.parse("package:${context.packageName}")
        )
        safeStart(context, intent)
    } else {
        openDetails(context)
    }
}

private fun requestFull(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val intent = Intent(
            Settings.ACTION_MANAGE_FULL_SCREEN_INTENT,
            Uri.parse("package:${context.packageName}")
        )
        safeStart(context, intent)
    } else {
        openDetails(context)
    }
}

private fun requestOverlay(context: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${context.packageName}")
    )
    safeStart(context, intent)
}

private fun openAutostart(context: Context) {
    val candidates = listOf(
        Intent().setComponent(
            ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
        ),
        Intent().setComponent(
            ComponentName(
                "com.samsung.android.lool",
                "com.samsung.android.sm.battery.ui.BatteryActivity"
            )
        )
    )
    for (intent in candidates) {
        try {
            context.startActivity(intent)
            return
        } catch (exception: Exception) {
        }
    }
    openDetails(context)
}
