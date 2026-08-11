package ir.parvaz.calendar.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
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

        val views = RemoteViews(context.packageName, R.layout.notification_date)
        views.setTextViewText(R.id.notif_weekday, today.weekday)
        views.setTextViewText(R.id.notif_persian, DateFormatter.format(today.persian))
        views.setTextViewText(
            R.id.notif_others,
            "${DateFormatter.format(today.gregorian)}  •  " +
                (today.hijri?.let { DateFormatter.format(it) } ?: "")
        )
        views.setTextViewText(R.id.notif_day, fa(today.persian.day))

        views.setInt(R.id.notif_root, "setBackgroundColor", prefs.bgColor)
        views.setTextColor(R.id.notif_weekday, prefs.textColor)
        views.setTextColor(R.id.notif_persian, prefs.textColor)
        views.setTextColor(R.id.notif_others, prefs.textColor)
        views.setInt(R.id.notif_day_box, "setBackgroundColor", prefs.boxColor)
        views.setTextColor(R.id.notif_day, prefs.boxTextColor)

        val openIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(views)
            .setCustomBigContentView(views)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
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
