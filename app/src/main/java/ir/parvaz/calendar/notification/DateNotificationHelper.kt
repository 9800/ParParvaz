package ir.parvaz.calendar.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ir.parvaz.calendar.MainActivity
import ir.parvaz.calendar.R
import ir.parvaz.calendar.core.date.CalendarProvider
import ir.parvaz.calendar.core.date.DateFormatter
import ir.parvaz.calendar.data.NotificationPrefs
import ir.parvaz.calendar.receiver.SystemReceiver
import java.util.Calendar

object DateNotificationHelper {

    const val CHANNEL_ID = "parvaz_date"
    const val NOTIFICATION_ID = 1001

    fun refresh(context: Context) {
        val prefs = NotificationPrefs(context)
        if (!prefs.enabled) {
            cancel(context)
            return
        }
        post(context)
        scheduleMidnight(context)
    }

    fun cancel(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }

    fun post(context: Context) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "نوار تاریخ",
                NotificationManager.IMPORTANCE_MIN
            )
            channel.setShowBadge(false)
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)
        }

        val prefs = NotificationPrefs(context)
        val today = CalendarProvider.today()
        val hijri = today.hijri?.let { DateFormatter.format(it) } ?: ""

        val openIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "${today.weekday} ${DateFormatter.format(today.persian)}"
        val body = "${DateFormatter.format(today.gregorian)} • $hijri"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setSubText(fa(today.persian.day))
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title\n$body"))
            .setColor(prefs.bgColor)
            .setColorized(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }

    fun scheduleMidnight(context: Context) {
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 1)

        val intent = Intent(context, SystemReceiver::class.java)
        intent.action = SystemReceiver.ACTION_MIDNIGHT

        val pi = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarm.canScheduleExactAlarms()) {
                alarm.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.timeInMillis, pi)
            } else {
                alarm.set(AlarmManager.RTC, cal.timeInMillis, pi)
            }
        } else {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC, cal.timeInMillis, pi)
        }
    }

    private fun fa(n: Int): String {
        return n.toString().map { c -> '۰' + (c - '0') }.joinToString("")
    }
}
